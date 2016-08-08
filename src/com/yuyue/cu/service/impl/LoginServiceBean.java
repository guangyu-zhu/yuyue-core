package com.yuyue.cu.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuyue.cu.dao.LoginDao;
import com.yuyue.cu.service.LoginService;

@Transactional
@Service("loginService")
public class LoginServiceBean implements LoginService {

	@Resource(name="loginDao")
	private LoginDao loginDao;
	
	@Override
	public void updateAdminPwd(String pwd) {
		loginDao.updateAdminPwd(pwd);
	}

	@Override
	public String getAdminPwd() {
		return loginDao.getAdminPwd();
	}

}
