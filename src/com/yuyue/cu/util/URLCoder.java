package com.yuyue.cu.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public abstract class URLCoder {
	
	/**
	 * @param utf8Code
	 * @return decode by UTF-8
	 */
	public static String decode(String utf8Code){
		if(utf8Code != null){
			try {
				return URLDecoder.decode(utf8Code, "utf-8");
			} catch (UnsupportedEncodingException e) {
				return utf8Code;
			}
		}else{
			return null;
		}
	}
	
	/**
	 * @param utf8Code
	 * @return encode by UTF-8
	 */
	public static String encode(String zhCode){
		if(zhCode != null){
			try {
				return URLEncoder.encode(zhCode, "utf-8");
			} catch (UnsupportedEncodingException e) {
				return zhCode;
			}
		}else{
			return null;
		}
	}
	
	public static void main(String[] args){
//		String content = "%E5%AE%87%E8%B6%8A%E4%BF%A1%E6%81%AF%E6%8A%80%E6%9C%AF%E6%9C%89%E9%99%90%E5%85%AC%E5%8F%B8";
//		String mapContent =	URLCoder.decode(content);
	}
}
