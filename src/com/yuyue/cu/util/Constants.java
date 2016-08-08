package com.yuyue.cu.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.yuyue.cu.core.inf.EnumFace;
import com.yuyue.util.CommConstants;

public class Constants {
	
	public static String HOSTS;
	public static boolean ALLOW_VISIT;
	public static String ENABLE_PREFERENCE;
	public static String ACTION_PROXY_FACADE;
	public static String AJAX_CLASSPATH_PREFIX;
	public static String PAGE_CLASSPATH_PREFIX;
	public static String SAVE_CLASSPATH_PREFIX;
	public static String JSP_PATH_PREFIX;
	public static String AJAX_LABEL_CONSTANTS;
	public static String PREFERENCE_SETUP;
	
	static {
		Properties configProps = null;
		InputStream is = null;
		try{
			configProps = new Properties();
			is = CommConstants.class.getResourceAsStream("/com/yuyue/config/config.properties");
			configProps.load(is);
			HOSTS = configProps.getProperty("hosts");
			if(configProps.getProperty("allow.visit") != null){
				ALLOW_VISIT = Boolean.parseBoolean(configProps.getProperty("allow.visit"));
			}
			ACTION_PROXY_FACADE = configProps.getProperty("action.proxy.facade");
			AJAX_CLASSPATH_PREFIX = configProps.getProperty("ajax.classpath.prefix");
			PAGE_CLASSPATH_PREFIX = configProps.getProperty("page.classpath.prefix");
			SAVE_CLASSPATH_PREFIX = configProps.getProperty("save.classpath.prefix");
			JSP_PATH_PREFIX = configProps.getProperty("jsp.path.prefix");
			AJAX_LABEL_CONSTANTS = configProps.getProperty("ajax.label.constants");
			ENABLE_PREFERENCE = configProps.getProperty("enable.preference");
			PREFERENCE_SETUP = configProps.getProperty("preference.setup");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}finally{
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
	
	public static String FORWARD_UNAUTHOR = "unauthorise";
	public static String FORWARD_ERROR = "error";
	public static String SERIAL_VERSION_UID = "serialVersionUID";
	
	public enum AdminPageTab implements EnumTabFace {
		ACCOUNT(1), ACCOUNT_INFORMATION(1.1f), ACCOUNT_MAIL(1.2f), ACCOUNT_MESSAGE(1.3f),
		PREFERENCE(2);
		private float value;

		private AdminPageTab(float value) {
			this.value = value;
		}

		public float getValue() {
			return value;
		}

		public void setValue(float value) {
			this.value = value;
		}
	}
	
	public enum AccountPage{
		ADDRESS, BIND_PHONE, EDIT_ACCOUNT, EDIT_PWD, FORGET_PWD, LOGIN, MY_ACCOUNT, REGISTER;
	}
	
	public interface EnumTabFace{
		float getValue();
    	void setValue(float value);
    }
	
	public enum Visibility implements EnumFace {
		DISPLAY("显示", 1), HIDDEN("隐藏", 0);
		private long value;
		private String name;

		private Visibility(String name, long value) {
			this.value = value;
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public void setName(String name) {
			this.name = name;
		}

		@Override
		public long getValue() {
			return value;
		}

		@Override
		public void setValue(long value) {
			this.value = value;
		}
		
		public Visibility getLabelConstants(long id) {
			Visibility[] values = Visibility.values();
			for (Visibility vb : values) {
				if (vb.getValue() == id)
					return vb;
			}
			return null;
		}
	}
	
}
