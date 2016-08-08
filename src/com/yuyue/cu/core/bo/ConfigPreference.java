package com.yuyue.cu.core.bo;

import java.util.Locale;
import java.util.Map;


public class ConfigPreference {
	
	private static ConfigPreference instance = null; 
	private Map<Long,String> preferenceMap;
	private Map<Long,String> preferenceMap_en;
	
	private ConfigPreference(){
		initialConfigPreference();
	}

	public void initialConfigPreference() {
	}
	
	private static synchronized void syncInit() {  
        if (instance == null) {  
            instance = new ConfigPreference();  
        }  
    }
	
	public static ConfigPreference getInstance() {  
        if (instance == null) {  
            syncInit();  
        }  
        return instance;  
    }
	
	public void setMap(Map<Long,String> preferenceMap, Locale locale){
		if(preferenceMap != null){
			if(locale != null && locale.equals(Locale.ENGLISH)){
				this.preferenceMap_en = preferenceMap;
			}else{
				this.preferenceMap = preferenceMap;
			}
		}
	}
	
	public Map<Long,String> getPreferenceMap(Locale locale){
		if(locale != null && locale.equals(Locale.ENGLISH)){
			return preferenceMap_en;
		}else{
			return preferenceMap;
		}
	}
	
	
}
