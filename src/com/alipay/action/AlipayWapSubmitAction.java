package com.alipay.action;

import java.io.PrintWriter;
import java.net.URLDecoder;
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

import com.alipay.config.AlipayWapConfig;
import com.alipay.form.BasePayment;
import com.alipay.util.AlipayConstants;
import com.alipay.util.AlipayNotify;
import com.alipay.util.AlipayWapSubmit;
import com.alipay.util.UtilDate;
import com.yuyue.cu.core.web.action.AbstractDispatchAction;

public abstract class AlipayWapSubmitAction extends AbstractDispatchAction{

	public static final String ISO_8859_1 = "ISO-8859-1";
	public static final String CHAR_SET = "UTF-8";
	private static final Logger LOG = Logger.getLogger(AlipayWapSubmitAction.class);
	
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
		
		//支付宝网关地址
		String ALIPAY_GATEWAY_NEW = "http://wappaygw.alipay.com/service/rest.htm?";

		////////////////////////////////////调用授权接口alipay.wap.trade.create.direct获取授权码token//////////////////////////////////////
		
		//返回格式
		String format = "xml";
		//必填，不需要修改
		
		//返回格式
		String v = "2.0";
		//必填，不需要修改
		
		//请求号
		String req_id = UtilDate.getOrderNum();
		//必填，须保证每次请求都是唯一
		
		//req_data详细信息
		
		//服务器异步通知页面路径
		String notify_url = new String(("http://"+request.getServerName()+getNotifyUrl()+".do").getBytes(ISO_8859_1),CHAR_SET);
		//需http://格式的完整路径，不能加?id=123这类自定义参数

		//页面跳转同步通知页面路径
		String call_back_url = new String(("http://"+request.getServerName()+getReturnUrl()+".do").getBytes(ISO_8859_1),CHAR_SET);
		//需http://格式的完整路径，不能加?id=123这类自定义参数，不能写成http://localhost/

		//操作中断返回地址
		String merchant_url = new String(("http://"+request.getServerName()).getBytes(ISO_8859_1),CHAR_SET);
		//用户付款中途退出返回商户的地址。需http://格式的完整路径，不允许加?id=123这类自定义参数

		//卖家支付宝帐户
		String seller_email = new String(AlipayConstants.sellerEmail.getBytes(ISO_8859_1),CHAR_SET);
		//必填

		//商户订单号
		String out_trade_no = basePayment.getOutTradeNo();
		//商户网站订单系统中唯一订单号，必填

		//订单名称
		String subject = basePayment.getSubject();
		//必填

		//付款金额
		String total_fee = basePayment.getTotalFee();
		//必填
				
		//请求业务参数详细
		String req_dataToken = "<direct_trade_create_req><notify_url>" + notify_url + "</notify_url><call_back_url>" + call_back_url + "</call_back_url><seller_account_name>" + seller_email + "</seller_account_name><out_trade_no>" + out_trade_no + "</out_trade_no><subject>" + subject + "</subject><total_fee>" + total_fee + "</total_fee><merchant_url>" + merchant_url + "</merchant_url></direct_trade_create_req>";
		//必填
		
		String partner = new String(AlipayConstants.partner.getBytes(ISO_8859_1),CHAR_SET);
		
		//////////////////////////////////////////////////////////////////////////////////
		
		//把请求参数打包成数组
		Map<String, String> sParaTempToken = new HashMap<String, String>();
		sParaTempToken.put("service", "alipay.wap.trade.create.direct");
		sParaTempToken.put("partner", partner);
		sParaTempToken.put("_input_charset", AlipayWapConfig.input_charset);
		sParaTempToken.put("sec_id", AlipayWapConfig.sign_type);
		sParaTempToken.put("format", format);
		sParaTempToken.put("v", v);
		sParaTempToken.put("req_id", req_id);
		sParaTempToken.put("req_data", req_dataToken);
		
		//建立请求
		String sHtmlTextToken = AlipayWapSubmit.buildRequest(ALIPAY_GATEWAY_NEW,"", "",sParaTempToken);
		//URLDECODE返回的信息
		sHtmlTextToken = URLDecoder.decode(sHtmlTextToken,AlipayWapConfig.input_charset);
		//获取token
		String request_token = AlipayWapSubmit.getRequestToken(sHtmlTextToken);
		//out.println(request_token);
		
		////////////////////////////////////根据授权码token调用交易接口alipay.wap.auth.authAndExecute//////////////////////////////////////
		
		//业务详细
		String req_data = "<auth_and_execute_req><request_token>" + request_token + "</request_token></auth_and_execute_req>";
		//必填
		
		//把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", "alipay.wap.auth.authAndExecute");
		sParaTemp.put("partner", partner);
		sParaTemp.put("_input_charset", AlipayWapConfig.input_charset);
		sParaTemp.put("sec_id", AlipayWapConfig.sign_type);
		sParaTemp.put("format", format);
		sParaTemp.put("v", v);
		sParaTemp.put("req_data", req_data);
		
		//建立请求
		String sHtmlText = AlipayWapSubmit.buildRequest(ALIPAY_GATEWAY_NEW, sParaTemp, "get", "确认");
		request.setAttribute("sHtmlText", sHtmlText);
		sParaTemp = null;
		sParaTempToken = null;
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
			String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

			//支付宝交易号
			String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

			//交易状态
			String trade_status = new String(request.getParameter("result").getBytes("ISO-8859-1"),"UTF-8");
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
		return mapping.findForward("home");
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
		String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

		//支付宝交易号
		String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

		//交易状态
		String trade_status = new String(request.getParameter("result").getBytes("ISO-8859-1"),"UTF-8");
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
