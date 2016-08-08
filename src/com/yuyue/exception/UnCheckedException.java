package com.yuyue.exception;

public class UnCheckedException extends RuntimeException {
    private static final long serialVersionUID = 5456971488091669737L;
	private String messageKey;
	
    private Object[] formatedArgs;
      
	public UnCheckedException(){
		this(null,null,(Object[])null);
	}

	
	public UnCheckedException(Throwable cause,String key,Object...args){
		super(cause);
		setMessageKey(key);
		setFormatedArgs(args);
	}
	
	public UnCheckedException(String key,Object... args){
		this(null,key,args);
	}
	
	public UnCheckedException(String key){
		this(null,key,(Object[])null);
	}	
	
	public String getMessage(){
		return super.getMessage();
	}

	public String getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	public Object[] getFormatedArgs() {
		return formatedArgs;
	}

	public void setFormatedArgs(Object[] formatedArgs) {
		this.formatedArgs = formatedArgs;
	}
	
	@Override
	public String toString(){
		return "Key:" + messageKey + "\n" + getMessage();
	}
}
