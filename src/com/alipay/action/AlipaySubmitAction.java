package com.alipay.action;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.alipay.config.AlipayConfig;
import com.alipay.form.BasePayment;
import com.alipay.util.AlipayConstants;
import com.alipay.util.AlipayNotify;
import com.alipay.util.AlipaySubmit;
import com.yuyue.cu.core.web.action.AbstractDispatchAction;

public abstract class AlipaySubmitAction extends AbstractDispatchAction{

	private static final Logger LOG = Logger.getLogger(AlipaySubmitAction.class);
	
	static{
		try{
			if(AlipayConstants.sellerEmail == null || AlipayConstants.sellerEmail.isEmpty()){
				ResourceBundle config = ResourceBundle.getBundle(AlipayConstants.ALIPAY_CONFIG_RESOURCE);
				AlipayConstants.sellerEmail = config.getString(AlipayConstants.SELLER_EMAIL);
				AlipayConstants.partner = config.getString(AlipayConstants.PARTNER);
				AlipayConstants.key = config.getString(AlipayConstants.KEY);
			}
		}catch(Exception e){}
	}
	
	protected ResourceBundle alipayconfig = ResourceBundle.getBundle(AlipayConstants.ALIPAY_CONFIG_RESOURCE);
	
	public ActionForward doSubmit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		BasePayment basePayment = submitHandler(form, request);
		
		if(basePayment == null){
			return mapping.findForward("pay_submit_fail");
		}
		
		//支付类型
		String payment_type = alipayconfig.getString(AlipayConstants.PAYMENT_TYPE);
		//必填，不能修改
		//服务器异步通知页面路径
		String notify_url = "http://"+request.getServerName()+getNotifyUrl()+".do";
		//需http://格式的完整路径，不能加?id=123这类自定义参数
		
		//页面跳转同步通知页面路径
		String return_url = "http://"+request.getServerName()+getReturnUrl()+".do";
		//需http://格式的完整路径，不能加?id=123这类自定义参数，不能写成http://localhost/

		//卖家支付宝帐户
		String seller_email = AlipayConstants.sellerEmail;
		//必填
		
		//防钓鱼时间戳
//		String anti_phishing_key = "";
		//若要使用请调用类文件submit中的query_timestamp函数

		//客户端的IP地址
//		String exter_invoke_ip = this.alipayconfig.getString(AlipayConstants.EXTER_INVOKE_IP);
		String exter_invoke_ip = AlipaySubmit.getRemoteAddress(request);
		//非局域网的外网IP地址，如：221.0.0.1
		LOG.debug("exter_invoke_ip = " + exter_invoke_ip);
		String partner = AlipayConstants.partner;
		//////////////////////////////////////////////////////////////////////////////////
		//服务 必须
		String service = alipayconfig.getString(AlipayConstants.SERVICE);
		//把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", service);
        sParaTemp.put("partner", partner);
        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
		sParaTemp.put("payment_type", payment_type);
		sParaTemp.put("notify_url", notify_url);
		sParaTemp.put("return_url", return_url);
		sParaTemp.put("seller_email", seller_email);
		sParaTemp.put("exter_invoke_ip", exter_invoke_ip);
		
		sParaTemp.put("out_trade_no", basePayment.getOutTradeNo());
		sParaTemp.put("subject", basePayment.getSubject());
		sParaTemp.put("total_fee", basePayment.getTotalFee());
		sParaTemp.put("body", basePayment.getBody());
		sParaTemp.put("show_url", basePayment.getShowUrl());
		//建立请求
		String sHtmlText = AlipaySubmit.buildRequest(sParaTemp,"post","确认");
		request.setAttribute("sHtmlText", sHtmlText);
		LOG.debug(sHtmlText);
		sParaTemp = null;
		return mapping.findForward("pay_submit_success");
	}

	@SuppressWarnings("rawtypes")
	public ActionForward doReturn(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		try{
			//获取支付宝GET过来反馈信息
			LOG.debug("开始处理支付宝票返回结果...");
			
			Map<String,String> params = new HashMap<String,String>();
			Map requestParams = request.getParameterMap();
			
			for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i]
							: valueStr + values[i] + ",";
				}
				params.put(name, valueStr);
			}
			
			//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
			//商户订单号
			String out_trade_no = request.getParameter("out_trade_no");

			//支付宝交易号
			String trade_no = request.getParameter("trade_no");

			//交易状态
			String trade_status = request.getParameter("trade_status");
			//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
			
			//计算得出通知验证结果
			boolean verify_result = AlipayNotify.verify(params);
			LOG.debug("Return - 订单号："+out_trade_no+", 支付宝返回结果验证"+(verify_result ? "成功" : "失败"));
			if(verify_result){
				boolean success = returnHandler(request, out_trade_no, trade_no, trade_status);
				if(success){
					params = null;
					return mapping.findForward("pay_return_success");
				}
			}
		}catch(Exception e){
			log.error(e);
		}
		return mapping.findForward("pay_return_failed");
	}

	@SuppressWarnings("rawtypes")
	public ActionForward doNotify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		//获取支付宝Post过来反馈信息
		LOG.debug("开始处理支付宝票返回结果...");
		
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			params.put(name, valueStr);
		}
		
		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		//商户订单号
		String out_trade_no = request.getParameter("out_trade_no");

		//支付宝交易号
		String trade_no = request.getParameter("trade_no");

		//交易状态
		String trade_status = request.getParameter("trade_status");
		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
		
		boolean verify_result = AlipayNotify.verify(params);
		LOG.debug("Notify - 订单号："+out_trade_no+", 支付宝返回结果验证"+(verify_result ? "成功" : "失败"));
		if(verify_result){
			boolean success = notifyHandler(request, out_trade_no, trade_no, trade_status);
			if(success){
				LOG.debug("Notify - 交易成功，订单号:"+out_trade_no+"; 支付宝交易号:"+trade_no);
				PrintWriter out = null;
				try{
					out = response.getWriter();  
					out.print("success");  
				}finally{
					if(out != null){
						out.close(); 
					}
				}
			}
		}
		params = null;
		return null;
	}

	protected abstract BasePayment submitHandler(ActionForm form, HttpServletRequest request) throws Exception;
	protected abstract boolean returnHandler(HttpServletRequest request, String out_trade_no, String trade_no, String trade_status) throws Exception;
	protected abstract boolean notifyHandler(HttpServletRequest request, String out_trade_no, String trade_no, String trade_status) throws Exception;
	protected abstract String getNotifyUrl();
	protected abstract String getReturnUrl();
}
