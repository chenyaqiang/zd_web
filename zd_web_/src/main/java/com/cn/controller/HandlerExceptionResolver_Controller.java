package com.cn.controller;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
/**
 * 
 * @author 周杰
 *  全局异常捕获  service dao  controller 
 *  没有具体完成
 */

import net.sf.json.JSONObject;  
@Controller
@RequestMapping(value="exception")
public class HandlerExceptionResolver_Controller implements HandlerExceptionResolver {

	//Logger log;
	boolean flag=true;
	
	@RequestMapping()
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object object,
			Exception es) {
	
		 if(es instanceof org.apache.shiro.authz.HostUnauthorizedException	  
			 
			 return  new ModelAndView("noauthroized");

		 }
		 else if(es instanceof org.apache.shiro.authz.AuthorizationException){

		  return  new ModelAndView("404");
		 }
		 else if(es instanceof org.apache.shiro.authc.UnknownAccountException){
	
			
			 flag=false;
			 unexcep();
		  return  new ModelAndView("404");
		 }
		 
		 
		 return  new ModelAndView();
	}
	

 
}

