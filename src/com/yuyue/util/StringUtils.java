package com.yuyue.util;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

public class StringUtils {
	
	public static String getContextPath(String contextPath){
		if("/".equals(contextPath)){
			contextPath = "";
		}
		return contextPath;
	}
	
	public static String convertListToString(List<String> list){
		return list.toString().substring(1,list.toString().length()-1);
	}
	
	public static String stringLess50(String content){
		if(content != null && content.length() > 50){
			return content.substring(0,50) + "...";
		}else{
			return content!=null?content:"";
		}
	}
	
	public static String stringLess(String content, int max){
		if(content != null && content.length() > max){
			return content.substring(0,max) + "...";
		}else{
			return content!=null?content:"";
		}
	}
	
	public static String generateParamForJs(Object[] params, String linkType){
		if(params != null && params.length > 0){
			StringBuilder paramBuilder = new StringBuilder();
			paramBuilder.append("'");
			paramBuilder.append(linkType);
			paramBuilder.append("'");
			for(Object param : params){
				paramBuilder.append(",");
				paramBuilder.append("'");
				paramBuilder.append(param.toString());
				paramBuilder.append("'");
			}
			return paramBuilder.toString();
		}else{
			return "";
		}
	}
	
	public static String convertToDbField(String name) {
		name = name.replace("A", "_a");
		name = name.replace("B", "_b");
		name = name.replace("C", "_c");
		name = name.replace("D", "_d");
		name = name.replace("E", "_e");
		name = name.replace("F", "_f");
		name = name.replace("G", "_g");
		name = name.replace("H", "_h");
		name = name.replace("I", "_i");
		name = name.replace("J", "_j");
		name = name.replace("K", "_k");
		name = name.replace("L", "_l");
		name = name.replace("M", "_m");
		name = name.replace("N", "_n");
		name = name.replace("O", "_o");
		name = name.replace("P", "_p");
		name = name.replace("Q", "_q");
		name = name.replace("R", "_r");
		name = name.replace("S", "_s");
		name = name.replace("T", "_t");
		name = name.replace("U", "_u");
		name = name.replace("V", "_v");
		name = name.replace("W", "_w");
		name = name.replace("X", "_x");
		name = name.replace("Y", "_y");
		name = name.replace("Z", "_z");
		return name;
	}
	
	public static String revertFromDbField(String name) {
		name = name.replace("_a","A");
		name = name.replace("_b","B");
		name = name.replace("_c","C");
		name = name.replace("_d","D");
		name = name.replace("_e","E");
		name = name.replace("_f","F");
		name = name.replace("_g","G");
		name = name.replace("_h","H");
		name = name.replace("_i","I");
		name = name.replace("_j","J");
		name = name.replace("_k","K");
		name = name.replace("_l","L");
		name = name.replace("_m","M");
		name = name.replace("_n","N");
		name = name.replace("_o","O");
		name = name.replace("_p","P");
		name = name.replace("_q","Q");
		name = name.replace("_r","R");
		name = name.replace("_s","S");
		name = name.replace("_t","T");
		name = name.replace("_u","U");
		name = name.replace("_v","V");
		name = name.replace("_w","W");
		name = name.replace("_x","X");
		name = name.replace("_y","Y");
		name = name.replace("_z","Z");
		return name;
	}
	
	public static long parseLong(String value){
		if(value == null){
			return 0;
		}else{
			return Long.parseLong(value);
		}
	}
	
	public static long parseParamAttrLong(HttpServletRequest request, String key){
		String value = request.getParameter(key);
		if(value == null){
			Long v = (Long)request.getAttribute(key);
			if(v == null){
				return 0;
			}else{
				return (Long)request.getAttribute(key);
			}
		}else{
			return Long.parseLong(value);
		}
	}
	
	public static int parseInt(String value){
		if(value == null){
			return 0;
		}else{
			return Integer.parseInt(value);
		}
	}
	
	public static int parseParamAttrInt(HttpServletRequest request, String key){
		String value = request.getParameter(key);
		if(value == null){
			Integer v = (Integer)request.getAttribute(key);
			if(v == null){
				return 0;
			}else{
				return (Integer)request.getAttribute(key);
			}
		}else{
			return Integer.parseInt(value);
		}
	}
	
	public static boolean parseBoolean(String value){
		if(value == null){
			return false;
		}else{
			return Boolean.parseBoolean(value);
		}
	}
	
	public static boolean isEmpty(String value){
		if(value != null && !value.isEmpty()){
			return false;
		}else{
			return true;
		}
	}
	
	public static String toFirstLetterUpperCase(String str) {  
	    if(str == null || str.length() < 2){  
	        return str;  
	    }  
	    String firstLetter = str.substring(0, 1).toUpperCase();  
	    return firstLetter + str.substring(1, str.length());  
	}
	
	public static String toHtmlString(String text){
		String result = "";
		if(text != null){
			text = text.replaceAll("\n", "<br>");
			result = text.trim();
		}
		return result;
	}
	
	public static String toTextAreaString(String text){
		String result = "";
		if(text != null){
			text = text.replaceAll("<br>", "\n");
			result = text.trim();
		}
		return result;
	}
	
	public static String toHtmlString(float text){
		String result = "";
		if(text != 0f){
			result = String.valueOf(text);
		}
		return result;
	}
	
	public static String parseGender(Boolean gender){
		if(gender!=null && gender){
			return "男";
		}else{
			return "女";
		}
	}
	
	public static String parseYesOrNo(Boolean yesOrNo){
		if(yesOrNo!=null && yesOrNo){
			return "是";
		}else{
			return "否";
		}
	}
	
	public static int[] randomCommon(int min, int max, int n) {
		if (n > (max - min + 1) || max < min) {
			return null;
		}
		int[] result = new int[n];
		int count = 0;
		while (count < n) {
			int num = (int) (Math.random() * (max - min)) + min;
			boolean flag = true;
			for (int j = 0; j < n; j++) {
				if (num == result[j]) {
					flag = false;
					break;
				}
			}
			if (flag) {
				result[count] = num;
				count++;
			}
		}
		return result;
	}

	public static void randomSet(int min, int max, int n, Set<Integer> set) {
		if (n > (max - min + 1) || max < min) {
			return;
		}
		for (int i = 0; i < n; i++) {
			// 调用Math.random()方法
			int num = (int) (Math.random() * (max - min)) + min;
			set.add(num);// 将不同的数存入HashSet中
		}
		int setSize = set.size();
		// 如果存入的数小于指定生成的个数，则调用递归再生成剩余个数的随机数，如此循环，直到达到指定大小
		if (setSize < n) {
			randomSet(min, max, n - setSize, set);// 递归
		}
	}
}
