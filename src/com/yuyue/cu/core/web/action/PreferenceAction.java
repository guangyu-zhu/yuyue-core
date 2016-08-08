package com.yuyue.cu.core.web.action;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionMapping;

import com.yuyue.cu.core.abstractor.AbstractCriteriaBuilder;
import com.yuyue.cu.core.bo.ConfigPreference;
import com.yuyue.cu.core.inf.SimpleObject;
import com.yuyue.cu.core.web.action.abstractor.AbstractBaseAction;
import com.yuyue.cu.core.web.criteria.PreferenceCriteriaBuilder;
import com.yuyue.cu.core.web.jo.PreferenceJo;
import com.yuyue.cu.util.Constants.AdminPageTab;
import com.yuyue.cu.util.Constants.EnumTabFace;
import com.yuyue.cu.util.WebUtils;

public class PreferenceAction extends AbstractBaseAction {

	// GLOBAL CONFIG BEGIN
	@Override
	public EnumTabFace getPageTab() {
		return AdminPageTab.PREFERENCE;
	}
	@Override
	public AbstractCriteriaBuilder getPageCriteriaBuilder(
			HttpServletRequest request) {
		return new PreferenceCriteriaBuilder(request);
	}
	@Override
	public boolean validateAuthorization(ActionMapping mapping,
			HttpServletRequest request) {
		return isAdminLoginAccount(mapping,request) == Boolean.TRUE;
	}
	// GLOBAL CONFIG END
	
	// LIST PAGE BEGIN
	@Override
	public long getListPagePaginationRecordAmount() {
		return 20;
	}
	@Override
	public void buildListPageCriteria(HttpServletRequest request,
			AbstractCriteriaBuilder builder) throws Exception {
		String s = request.getParameter("s");
		if(s != null){
			builder.beginBracket();
			builder.addLkCriteria("uri", LABEL_LIKE+s+LABEL_LIKE);
			builder.endBracket();
		}
	}
	// LIST PAGE END
	
	// DETAIL PAGE BEGIN
	// ... nothing so far
	// DETAIL PAGE END
	
	// SAVE PAGE BEGIN
	@Override
	public String getCreateSuccessMessage(HttpServletRequest request, SimpleObject object) {
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return MessageFormat.format(resourceBundle.getString("msg.create.success"),((PreferenceJo)object).getLabelKey());
	}
	@Override
	public String getUpdateSuccessMessage(HttpServletRequest request,
			SimpleObject object) {
		Locale locale = (Locale)request.getSession().getAttribute(Globals.LOCALE_KEY);
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		ConfigPreference.getInstance().getPreferenceMap(locale).put(((PreferenceJo)object).getLabelKey(), WebUtils.getPreferenceContent(((PreferenceJo)object)));
		ConfigPreference configPreference = ConfigPreference.getInstance();
		Map<Long, String> preferenceMap = configPreference.getPreferenceMap(locale);
		preferenceMap.put(((PreferenceJo)object).getLabelKey(), WebUtils.getPreferenceContent((PreferenceJo)object));
		configPreference.setMap(preferenceMap, locale);
		return MessageFormat.format(resourceBundle.getString("msg.update.success"),((PreferenceJo)object).getLabelKey());
	}
	@Override
	public void buildCreatePageCriteria(HttpServletRequest request, SimpleObject object, AbstractCriteriaBuilder builder) throws Exception {
		PreferenceJo obj = (PreferenceJo)object;
		obj.setUpdateDate(new Date());
	}
	@Override
	public void buildUpdatePageCriteria(HttpServletRequest request, SimpleObject object, AbstractCriteriaBuilder builder) throws Exception {
		PreferenceJo obj = (PreferenceJo)object;
		obj.setUpdateDate(new Date());
	}
	// SAVE PAGE END
	
	// REMOVE PAGE BEGIN
	@Override
	public String getRemoveSuccessMessage(HttpServletRequest request, SimpleObject object) {
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return MessageFormat.format(resourceBundle.getString("msg.remove.success"),((PreferenceJo)object).getLabelKey());
	}
	// REMOVE PAGE END

}
