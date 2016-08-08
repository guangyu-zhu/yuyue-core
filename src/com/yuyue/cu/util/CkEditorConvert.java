package com.yuyue.cu.util;

public class CkEditorConvert {
	
	public CkEditorConvert(String code, boolean isHtmlCode){
		if(isHtmlCode){
			this.htmlCode = code;
		}else{
			this.ckCode = code;
		}
	}
	
	
	private String htmlCode;
	private String ckCode;
	private boolean converted;
	
	public String getCkCode(){
		if(!converted){
			convert();
		}
		if(ckCode != null){
			return ckCode;
		}else{
			return "";
		}
	}
	
	public String getHtmlCode(){
		if(!converted){
			convert();
		}
		if(htmlCode != null){
			return htmlCode;
		}else{
			return "";
		}
	}

	private void convert() {
		if(htmlCode != null && ckCode == null){
			ckCode = htmlCode.replaceAll("<", "&lt;");
			ckCode = ckCode.replaceAll(">", "&gt;");
			ckCode = ckCode.replaceAll("\"", "&quot;");
		}else if(htmlCode == null && ckCode != null){
			htmlCode = ckCode.replaceAll("&lt;", "<");
			htmlCode = htmlCode.replaceAll("&gt;", ">");
			htmlCode = htmlCode.replaceAll("&quot;", "\"");
		}
	}
	
}
