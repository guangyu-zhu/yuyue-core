package com.yuyue.cu.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

import com.yuyue.cu.core.abstractor.AbstractCriteriaBuilder;
import com.yuyue.cu.core.bo.Pagination;
import com.yuyue.cu.core.inf.AjaxProxy;
import com.yuyue.cu.core.inf.EnumFace;
import com.yuyue.cu.core.inf.PageProxy;
import com.yuyue.cu.core.inf.SaveProxy;
import com.yuyue.cu.core.inf.TreeType;
import com.yuyue.cu.core.web.jo.PreferenceJo;
import com.yuyue.cu.service.SimpleModuleService;
import com.yuyue.util.CommConstants;
import com.yuyue.util.StreamUtils;

public class WebUtils {
	
	private static Logger log = Logger.getLogger(WebUtils.class);
	
	public static void printJsonObject(HttpServletResponse response,
			JSONObject jsonObject) throws IOException {
		PrintWriter out = null;
		try{
			response.setContentType("application/json;charset=utf-8"); 
			response.setCharacterEncoding("utf-8");
			out = response.getWriter();  
			out.print(jsonObject);  
			out.flush();  
		}finally{
			if(out != null){
				out.close(); 
			}
		}
	}
	
	public static void printHtmlObject(HttpServletResponse response, String content) throws IOException {
		PrintWriter out = null;
		try{
			response.setContentType("charset=utf-8"); 
			response.setCharacterEncoding("utf-8");
			out = response.getWriter();  
			out.print(content);  
			out.flush();  
		}finally{
			if(out != null){
				out.close(); 
			}
		}
	}
	
	public static void createImageWithLanguage(HttpServletRequest request,
			Locale locale, long id, String parmName, String imageNamePrefix) {
		String imagePath = request.getServletContext().getRealPath(
				CommConstants.IMAGE_SRC);
		String imageName = request.getParameter(parmName);
		String language = locale.getLanguage();
		String newImageName = imageNamePrefix + "-" + language + "-" + id
				+ ".jpg";
		if (imageName != null) {
			StreamUtils.renameTo(imagePath + File.separator + imageName
					+ ".jpg", imagePath + File.separator + newImageName);
		}
	}

	public static void deleteImageWithLanguage(HttpServletRequest request,
			Locale locale, long id, String imageNamePrefix) {
		String language = locale.getLanguage();
		String imageName = imageNamePrefix + "-" + language + "-" + id + ".jpg";
		String imagePath = request.getServletContext().getRealPath(
				CommConstants.IMAGE_SRC);
		StreamUtils.deleteFile(imagePath + File.separator + imageName);
	}

	public static void popupToastMessage(HttpServletRequest request, int type,
			String message) {
		if(message != null){
			request.getSession().setAttribute(CommConstants.TOAST_TYPE_SESS_ATTR, type);
			request.getSession().setAttribute(CommConstants.TOAST_MESSAGE_SESS_ATTR, message);
		}
	}

	public static String generateRandomString(int size) {
		char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
				'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
				'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		return generateRandomInternal(size, str);
	}
	
	public static String generateRandomNumber(int size) {
		char[] str = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		return generateRandomInternal(size, str);
	}
	
	private static String generateRandomInternal(int size, char[] str) {
		// 35是因为数组是从0开始的，26个字母+10个数字
		final int maxNum = 36;
		int i; // 生成的随机数
		int count = 0; // 生成的密码的长度

		StringBuilder pwd = new StringBuilder("");
		Random r = new Random();
		while (count < size) {
			// 生成随机数，取绝对值，防止生成负数，

			i = Math.abs(r.nextInt(maxNum)); // 生成的数最大为36-1

			if (i >= 0 && i < str.length) {
				pwd.append(str[i]);
				count++;
			}
		}
		return pwd.toString();
	}
	
	public static void sendMail(String host, String user, String passsword,
			String toAddress, String fromAddress, String subject, String content, String title){
		sendMail(host, "25", user, passsword, toAddress, fromAddress, subject, content, false, title);
	}
	
	public static void sendMailBcc(String host, String user, String passsword,
			String toAddress, String fromAddress, String subject, String content, String title){
		sendMail(host, "25", user, passsword, toAddress, fromAddress, subject, content, true, title);
	}
	
	public static void sendMail(String host, String user, String passsword,
			String toAddress, String fromAddress, String subject, String content){
		sendMail(host, "25", user, passsword, toAddress, fromAddress, subject, content, false, "酷建站[cooljz]");
	}
	
	public static void sendMailBcc(String host, String user, String passsword,
			String toAddress, String fromAddress, String subject, String content){
		sendMail(host, "25", user, passsword, toAddress, fromAddress, subject, content, true, "酷建站[cooljz]");
	}
	
	public static void sendMail(String host, String port, String user, String passsword,
			String toAddress, String fromAddress, String subject, String content, boolean isBcc, String title){
		Transport transport = null;
		Message msg = null;
		try{
			Properties props = new Properties();
			props.setProperty("mail.smtp.auth", "true");
			props.setProperty("mail.smtp.host", host);
			props.setProperty("mail.smtp.port", port); 
			Session session = Session.getInstance(props, null);
			session.setDebug(false);
			msg = new MimeMessage(session);
			
//			msg.setFrom(new InternetAddress(fromAddress));
			//设置自定义发件人昵称  
	        String nick="";  
	        try {  
	            nick=javax.mail.internet.MimeUtility.encodeText(title);  
	        } catch (UnsupportedEncodingException e) {  
	            e.printStackTrace();  
	        }   
	        msg.setFrom(new InternetAddress(nick+" <"+fromAddress+">"));  
			
			msg.setSubject(subject);
			String[] str = toAddress.split(";");
			InternetAddress[] address = new InternetAddress[str.length];
			for (int i = 0; i < str.length; i++){
				if(!str[i].isEmpty()){
					address[i] = new InternetAddress(str[i]);
				}
			}
			if(isBcc){
				msg.setRecipients(RecipientType.BCC,address);
			}else{
				msg.setRecipients(RecipientType.TO,address);
			}
			msg.setSentDate(new Date());
			msg.setContent(content,"text/html;charset=UTF-8");
			transport = session.getTransport("smtp");
			transport.connect( host, user, passsword);
			if(log.isDebugEnabled()){
				log.debug("Ready to send message");
			}
			transport.sendMessage(msg, msg.getAllRecipients());
			if(log.isDebugEnabled()){
				log.debug("End of send message");
			}
		}catch(Exception e){
			log.error(CommConstants.ERROR_LEVEL_1 +"SmtpMsg sendMail failed which caused by '"+e+"'! {toAddress:"+toAddress+";subject:"+subject);
		}finally{
			if(transport != null){
				try {
					transport.close();
				} catch (MessagingException e) {
					log.error(CommConstants.ERROR_LEVEL_0 +"SmtpMsg sendMail failed which caused by '"+e+"'! {toAddress:"+toAddress+";subject:"+subject);
				}
			}
			transport = null;
			msg = null;
		}
	}
	
	public static String sendMsgToPhone(String host, String uid, String key, String phoneNumber,String content) {
		PostMethod post = null;
		HttpClient client = null;
		try{
			client = new HttpClient();
			post = new PostMethod(host);
			post.addRequestHeader("Content-Type",
					"application/x-www-form-urlencoded;charset=gbk");
			NameValuePair[] data = { new NameValuePair("Uid", uid),
					new NameValuePair("Key", key),
					new NameValuePair("smsMob", phoneNumber),
					new NameValuePair("smsText", content) };
			post.setRequestBody(data);
			
			client.executeMethod(post);
			String result = new String(post.getResponseBodyAsString().getBytes(
					"UTF-8"));
			return result;
		}catch(Exception e){
			log.error(CommConstants.ERROR_LEVEL_1+"SMSMsg sendMsgToPhone failed which caused by '"+e+"'! {phoneNumber:"+phoneNumber+";content:"+content);
			return "error";
		}finally{
			if(post != null){
				try{
					post.releaseConnection();
				}catch(Exception e){
					log.error(CommConstants.ERROR_LEVEL_0 + "when try to close connection, error happen",e);
				}
			}
			post = null;
			client = null;
		}
	}
	
	public static String getProductKey(String tid, long id){
		if(tid != null && id > 0){
			return tid+id;
		}else{
			return null;
		}
	}
	
	/**
	 * 
	 * @param productKey
	 * @return [tid,id]
	 */
	public static String[] getTidByProductKey(String productKey){
		if(productKey != null){
			Pattern tableIdProductIdPattern = Pattern.compile("(\\D+)(\\d+)");
			Matcher matcher = tableIdProductIdPattern.matcher(productKey);
			if(matcher.find()){
				return new String[]{matcher.group(1),matcher.group(2)};
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	
	public static double getAdjustRate(int maxWidth, int maxHeight, double width, double height){
		if(maxWidth >= width && maxHeight >= height){
			return 1;
		}else if(maxWidth >= width && maxHeight <= height){
			return (double)maxHeight/(double)height;
		}else if(maxWidth <= width && maxHeight >= height){
			return (double)maxWidth/(double)width;
		}else if(maxWidth <= width && maxHeight <= height){
			double rateW = (double)maxWidth/(double)width;
			double rateH = (double)maxHeight/(double)height;
			return rateW>rateH?rateH:rateW;
		}else{
			return 1;
		}
	}
	
	public static double getAdjustRate(int maxWidth, int maxHeight, int width, int height){
		if(maxWidth >= width && maxHeight >= height){
			return 1;
		}else if(maxWidth >= width && maxHeight <= height){
			return (double)maxHeight/(double)height;
		}else if(maxWidth <= width && maxHeight >= height){
			return (double)maxWidth/(double)width;
		}else if(maxWidth <= width && maxHeight <= height){
			double rateW = (double)maxWidth/(double)width;
			double rateH = (double)maxHeight/(double)height;
			return rateW>rateH?rateH:rateW;
		}else{
			return 1;
		}
	}
	
	public static Object[] getInitialDetailPageObjects(HttpServletRequest request){
		String t = request.getParameter("t");
		String readonly = "";
		String disabled = "";
		boolean isView = false;
		if("v".equals(t)){
			readonly = "readonly=\"readonly\"";
			disabled = "disabled=\"disabled\"";
			isView = true;
		}
		return new Object[]{readonly,disabled,isView};
	}
	
	public static void setStartAmount(HttpServletRequest request, long startAmount) {
		request.setAttribute(CommConstants.START_AMOUNT_KEY, startAmount);
	}
	
	public static void setStartAmount(HttpServletRequest request, long startAmount, String key) {
		request.setAttribute(CommConstants.START_AMOUNT_KEY+key, startAmount);
	}
	
	public static void reparePaginationBegin(HttpServletRequest request, long recordAmount) {
		request.setAttribute(CommConstants.RECORD_AMOUNT_KEY, recordAmount);
	}
	
	public static void reparePaginationBegin(HttpServletRequest request, long recordAmount, String key) {
		request.setAttribute(CommConstants.RECORD_AMOUNT_KEY+key, recordAmount);
	}
	
	public static void reparePaginationEnd(HttpServletRequest request,
			AbstractCriteriaBuilder builder, SimpleModuleService simpleModuleService) {
		long totalAmount = simpleModuleService.countSimpleObjects(builder);
		request.setAttribute(CommConstants.TOTAL_AMOUNT_KEY, totalAmount);
		Pagination pagination = new Pagination(request,totalAmount);
		pagination.setAttribute();
		pagination = null;
	}
	
	public static void reparePaginationEnd(HttpServletRequest request,
			AbstractCriteriaBuilder builder, SimpleModuleService simpleModuleService, String key) {
		long totalAmount = simpleModuleService.countSimpleObjects(builder);
		request.setAttribute(CommConstants.TOTAL_AMOUNT_KEY+key, totalAmount);
		Pagination pagination = new Pagination(request,totalAmount, key);
		pagination.setAttribute();
		pagination = null;
	}
	
	public static void preparePaginationEnd(HttpServletRequest request, String sql, 
			SimpleModuleService simpleModuleService, String key) {
		long totalAmount = simpleModuleService.countSql(sql);
		request.setAttribute(CommConstants.TOTAL_AMOUNT_KEY+key, totalAmount);
		Pagination pagination = new Pagination(request,totalAmount, key);
		pagination.setAttribute();
		pagination = null;
	}
	
	public static AjaxProxy getAjaxProxy(String proxy, SimpleModuleService simpleModuleService) throws InstantiationException,
		IllegalAccessException, InvocationTargetException,
		NoSuchMethodException, ClassNotFoundException {
		AjaxProxy ajaxProxy = null;
		if(proxy != null){
			ajaxProxy = (AjaxProxy)Class.forName(Constants.AJAX_CLASSPATH_PREFIX+"."+proxy).getConstructor(SimpleModuleService.class).newInstance(simpleModuleService);
		}
		return ajaxProxy;
	}
	
	public static PageProxy getPageProxy(String proxy, SimpleModuleService simpleModuleService) throws InstantiationException,
		IllegalAccessException, InvocationTargetException,
		NoSuchMethodException, ClassNotFoundException {
		PageProxy pageProxy = null;
		if(proxy != null){
			pageProxy = (PageProxy)Class.forName(Constants.PAGE_CLASSPATH_PREFIX+"."+proxy).getConstructor(SimpleModuleService.class).newInstance(simpleModuleService);
		}
		return pageProxy;
	}
	
	public static SaveProxy getSaveProxy(String proxy, SimpleModuleService simpleModuleService) throws InstantiationException,
		IllegalAccessException, InvocationTargetException,
		NoSuchMethodException, ClassNotFoundException {
		SaveProxy saveProxy = null;
		if(proxy != null){
			saveProxy = (SaveProxy)Class.forName(Constants.SAVE_CLASSPATH_PREFIX+"."+proxy).getConstructor(SimpleModuleService.class).newInstance(simpleModuleService);
		}
		return saveProxy;
	}
	
	public static Long getLongParameter(HttpServletRequest request, String param){
		return getLongParameter(request,param,null);
	}
	
	public static Long getLongParameter(HttpServletRequest request, String param, Long initialValue){
		String _value = request.getParameter(param);
		long value = 0;
		if(initialValue != null){
			value = initialValue;
		}
		if(_value != null){
			value = Long.parseLong(_value);
		}else{
			return null;
		}
		return value;
	}
	
	public static String generateEnumOption(HttpServletResponse response, EnumFace enumFace, int value){
		return "<option value=\""+enumFace.getValue()+"\" "+(value==enumFace.getValue()?"selected='selected'":"")+">"+enumFace.getName()+"</option>";
	}
	
	public static String generateEnumOption(HttpServletResponse response, EnumFace enumFace, long value){
		return "<option value=\""+enumFace.getValue()+"\" "+(value==enumFace.getValue()?"selected='selected'":"")+">"+enumFace.getName()+"</option>";
	}
	
	public static String getCkValue(String value){
		CkEditorConvert ckEditorConvert = new CkEditorConvert(value,true);
		String result = ckEditorConvert.getCkCode();
		ckEditorConvert = null;
		return result;
	}
	
	public static String getPreferenceContent(PreferenceJo preference){
		if(preference.getContentType() == CommConstants.PREFERENCE_TYPE_SINGLE_IMAGE){
			return preference.getSingleImage();
		}else if(preference.getContentType() == CommConstants.PREFERENCE_TYPE_MULTI_IMAGE){
			return preference.getMultiImage();
		}else if(preference.getContentType() == CommConstants.PREFERENCE_TYPE_INPUT){
			return preference.getInput();
		}else if(preference.getContentType() == CommConstants.PREFERENCE_TYPE_TEXTAREA){
			return preference.getTextarea();
		}else if(preference.getContentType() == CommConstants.PREFERENCE_TYPE_SINGLE_FILE){
			return preference.getSingleFile();
		}else if(preference.getContentType() == CommConstants.PREFERENCE_TYPE_MULTI_FILE){
			return preference.getMultiFile();
		}else if(preference.getContentType() == CommConstants.PREFERENCE_TYPE_CK_CONTENT){
			return preference.getCkContent();
		}else{
			return null;
		}
	}
	
	public static Map<Long,List<TreeType>> organizeTypeTree(List<? extends TreeType> treeTypeList){
		Map<Long,List<TreeType>> map = new TreeMap<Long,List<TreeType>>();
		if(treeTypeList != null){
			for(TreeType treeType : treeTypeList){
				long parentId = treeType.getParentId();
				List<TreeType> ttList = map.get(parentId);
				if(ttList == null){
					map.put(parentId, new ArrayList<TreeType>());
				}
				map.get(parentId).add(treeType);
			}
		}
		return map;
	}
	
	public static Set<Long> getChildrenIds(Collection<Long> ids, Map<Long,List<TreeType>> map){
		Set<Long> typeToCriteria = new HashSet<Long>();
		getChildrenIdsInternal(ids, map, typeToCriteria, 0, false);
		return typeToCriteria;
	}

	private static void getChildrenIdsInternal(Collection<Long> ids, Map<Long, List<TreeType>> map, Set<Long> typeToCriteria, long parentId, boolean parentStartCollection) {
		List<? extends TreeType> levelList = map.get(parentId);
		if(levelList != null){
			for(TreeType treeType : levelList){
				long levelId = treeType.getId();
				boolean appendable = treeType.getAppendable();
				boolean startToCollect = false;
				if(ids.contains(levelId)){
					startToCollect = true;
				}
				if(parentStartCollection | startToCollect){
					if(!appendable){
						typeToCriteria.add(levelId);
					}
				}
				getChildrenIdsInternal(ids, map, typeToCriteria, levelId, (parentStartCollection | startToCollect));
			}
		}
	}
	
}
