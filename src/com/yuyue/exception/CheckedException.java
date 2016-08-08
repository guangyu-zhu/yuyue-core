package com.yuyue.exception;

public class CheckedException extends Exception {
		
    private static final long serialVersionUID = -668201172231360638L;
    private String messageKey;
    
    private Object[] formatedArgs;
      
	public CheckedException(){
		this(null,null,(Object[])null);
	}
	
	public CheckedException(Throwable cause,String key,Object...args){
		super(cause);
		setMessageKey(key);
		setFormatedArgs(args);
	}
	
	public CheckedException(String key,Object...args){
		this(null,key,args);
	}
	
	public CheckedException(String key){
		this(key,null,(Object[])null);
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
