package com.yuyue.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class CommConstants {
	
	public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	public static final SimpleDateFormat SDF_8 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat SDF_9 = new SimpleDateFormat("yyyy-MM-dd HH:mm E");
	public static final SimpleDateFormat SDF_5 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	public static final SimpleDateFormat SDF_TIMER = new SimpleDateFormat("yyyyMMddHHmmss");
	public static final SimpleDateFormat SDF_4 = new SimpleDateFormat("yyyy-MM-dd E");
	public static final SimpleDateFormat SDF_1 = new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat SDF_2 = new SimpleDateFormat("MM/dd/yyyy");
	public static final SimpleDateFormat SDF_3 = new SimpleDateFormat("yyyy.MM.dd");
	public static final SimpleDateFormat SDF_10 = new SimpleDateFormat("yyyy年MM月dd日");
	public static final SimpleDateFormat SDF_6 = new SimpleDateFormat("yyyy-MM");
	public static final SimpleDateFormat SDF_7 = new SimpleDateFormat("MM/yyyy");
	public static final SimpleDateFormat SDF_MMM = new SimpleDateFormat("MMM");
	public static final SimpleDateFormat SDF_DD = new SimpleDateFormat("dd");
	public static final DecimalFormat MATH_DF2 = new DecimalFormat("0.00");
	
	public static final SimpleDateFormat[] SDFS = {
								SDF,SDF_8,SDF_9,SDF_5,SDF_TIMER,SDF_1,SDF_2,SDF_3,SDF_10,SDF_6,SDF_7,SDF_MMM,SDF_DD
						};
	
	public static final String START_AMOUNT_KEY = "startAmount";
	public static final String RECORD_AMOUNT_KEY = "recordAmount";
	public static final String TOTAL_AMOUNT_KEY = "totalAmount";
	public static final long DEFAULT_RECORD_AMOUNT = 30;
	public static final String PAGINATION_ATTR_BAR = "pagination_attr_bar";
	
	public static final String FIELD_LABEL_RESOURCE_PATH = "com.yuyue.config.FieldLabelResources";
	public static final String INTERNAL_RESOURCES_PATH = "com.yuyue.cu.config.Resources";
	public static final String TOAST_TYPE_SESS_ATTR = "toast_type";
	public static final String TOAST_MESSAGE_SESS_ATTR = "toast_message";
	public static final String ACTION_REDIRECT_PARAMS_ATTR = "redirectParams";
	public static final String PROP_LIST_PREFIX = "list_";
	public static final String PROP_DETAIL_PREFIX = "detail_";
	public static final int TOAST_TYPE_SUCCESS = 1;
	public static final int TOAST_TYPE_SUCCESS_STICKY = 2;
	public static final int TOAST_TYPE_NOTICE = 3;
	public static final int TOAST_TYPE_NOTICE_STICKY = 4;
	public static final int TOAST_TYPE_WARNING = 5;
	public static final int TOAST_TYPE_WARNING_STICKY = 6;
	public static final int TOAST_TYPE_ERROR = 7;
	public static final int TOAST_TYPE_ERROR_STICKY = 8;
	
	public static final String ERROR_LEVEL_0 = "Error-level-0 : ";
	public static final String ERROR_LEVEL_1 = "Error-level-1 : ";
	
	public static final String TEMP_SRC = "upload/temp";
	public static final String IMAGE_SRC = "upload/images";
	public static final String FILE_SRC = "upload/files";
	public static final String NO_IMAGE_SRC = "/admin/assets/img/no_image.gif";
	
	public static int MAX_MENU = 3;
	public static int MAX_TERM = 3;
	public static int MAX_CATEGORY = 3;
	public static int MAX_TAG = 3;
	public static int MAX_ATTRIBUTE = 10;
	
	public static final int PREFERENCE_TYPE_SINGLE_IMAGE = 1;
	public static final int PREFERENCE_TYPE_MULTI_IMAGE = 2;
	public static final int PREFERENCE_TYPE_INPUT = 3;
	public static final int PREFERENCE_TYPE_TEXTAREA = 4;
	public static final int PREFERENCE_TYPE_CK_CONTENT = 5;
	public static final int PREFERENCE_TYPE_SINGLE_FILE = 6;
	public static final int PREFERENCE_TYPE_MULTI_FILE = 7;
	
	public static final String WEB_ADMIN_AUTHOR_SESS_ATTR = "web-admin-author-session-attribute";
	public static final String WEB_SITE_AUTHOR_SESS_ATTR = "web-site-author-session-attribute";
	public static final String WEB_SITE_ACCOUNT_PAGE_FORWARD_ATTR = "web-site-aacount-page-forward-attribute";
	public static final String WEB_ADMIN_TABS_ATTR = "web-admin-tabs-attribute";
	public static final String PREFERENCE_MAP_ATTR = "preference-map-attribute";
	
	public static final String UPDATE_ADMIN_PWD = "update c_admin set pwd = ?";
	public static final String SELECT_ADMIN_PWD = "select pwd from c_admin";
	
	public static final String AJAX_SAVE_SEPARATOR_PARAM = "\\|=\\|=,=\\|=\\|";
	public static final String AJAX_SAVE_SEPARATOR_KV = "\\|=\\|=:=\\|=\\|";
}
