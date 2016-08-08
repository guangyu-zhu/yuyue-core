package com.yuyue.cu.core.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HtmlDispatcherServlet extends HttpServlet {

	private static final long serialVersionUID = -44340840705388224L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String uri = request.getRequestURI();
		System.out.println(uri);
	}
	
	public static void main(String[] args){
		String uri = "/cafe/web/1620-1623-1648-0-0-0-0-0-0-0-1-1-1-1-2-2811-2857-0.html";
		uri = uri.substring(uri.lastIndexOf("/")+1,uri.lastIndexOf(".html"));
		String[] idArray = uri.split("-");
		if(idArray != null){
			if(idArray.length == 1){
				// show one product detail
			}else{
				// show list
				// 1620-1623-1648: category
				// 0-0-0-0-0-0-0-1: criteria
				// 1-1-1-1-2-2811-2857-0: sort or asc
			}
		}
		System.out.print(idArray.length);
	}

}
