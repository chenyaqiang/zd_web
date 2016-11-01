package com.cn.Utils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.aspectj.lang.annotation.After;
import com.cn.pojo.Logger_pojo;
import com.cn.pojo.User;
import com.cn.service.Impl.Logger_ServiceImpl;
import com.cn.service.Impl.NetworkUtil;
import com.cn.service.Impl.SequenceGenerator;

@Aspect
@Component
public class AspectjLogger {
 
	//本地异常日志记录对象
	private static final Logger logger = LoggerFactory.getLogger(AspectjLogger.class);
	 private String name; 
	  private String description;
	  private String method; //调用方法
	 private  String  ip; //调用者IP
	 
	@Autowired(required=false)
	  NetworkUtil network;
	
	@Autowired(required=false)
	 Logger_ServiceImpl logser; 
	
	@Autowired(required=false)
	 SequenceGenerator seq;
	
	
	//业务层日志
	@Pointcut("@annotation(com.cn.Utils.AopServiceLog)")
	public void serviceAspect(){
		
	}
	
	//控制层日志
	@Pointcut("@annotation(com.cn.Utils.AopControllerLog)")
	public void controllerAspect(){
	
	}
	
	/**
	 * 进入控制层日志
	 *
	 **/
	@After("controllerAspect()")
	public void doAfter(){
		System.out.println("调用完毕------------------");
		Logger_pojo log=SpringContextHolder.getBean(Logger_pojo.class);
		
	String name= getadminuser();
	 log.setId(seq.logger_seq());

	 log.setIp(ip);
	 log.setDescription(description);
	 log.setMethod(method);
	 log.setName(name);
	 System.out.println("调用完毕后------------------------------------"+log.toString());
	 logser.addlogger(log);
	
	}
	
	@Before("controllerAspect()")
	public void doBefore(JoinPoint joinPoint){
		System.out.println("进入控制层-------------------------------------------");
		
		try {
			 description=getControllerMethod(joinPoint);
		} catch (Exception e) {
			logger.error("Aspectj error"+e);
		
		}
		logger.info("方法"+joinPoint.getTarget().getClass().getName()
				+"."+joinPoint.getSignature().getClass()+"()");
		
	
	 
		
		try {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();  
		
			this.ip= network.getIpAddress( request);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		method=("方法"+joinPoint.getTarget().getClass().getName()
				+"."+joinPoint.getSignature().getClass()+"()");
		
	
	
	
	
		
	}
	
	/**
	 *  获取登录管理员
	 */
	
	
	 private static String getadminuser(){
		 String name = null;
		try {
			Class<?> clazz = Class.forName("com.cn.controller.Admin_indexController");
			
			Object object = clazz.newInstance();
			
			Method getmethod=clazz.getMethod("getusername", null);
			
		name =(String) getmethod.invoke(object, null);	
		} catch (ClassNotFoundException e) {
			 logger.error("找不到加载的类------------------------"+e);
		} catch (InstantiationException e) {
			 logger.error("实例化类失败-------------------------"+e);
		} catch (IllegalAccessException e) {
			 logger.error("实例化类失败-------------------------"+e);
		} catch (NoSuchMethodException e) {
			
			e.printStackTrace();
		} catch (SecurityException e) {
		
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 return name;
	 }
	
	
	/**
	 *  Controller层方法的详细信息
	 */
	  private static String getControllerMethod(JoinPoint joinpoint) throws Exception{
		  String targetName=joinpoint.getTarget().getClass().getName();
		  String methodName = joinpoint.getSignature().getName();
		  Object [] arguments = joinpoint.getArgs();
		  Class targetClass = Class.forName(targetName);
		  Method [] methods = targetClass.getMethods();
		  
		  String description ="";
		  
		  for(Method method : methods){
			   if(method.getName().equals(methodName)){
				   Class[]classz = method.getParameterTypes();
				   
				   if(classz.length==arguments.length){
					  description =method.getAnnotation(AopControllerLog.class).description();
					  break;
				   }
			   }
		  }
		  
		  
		  
		  return description;
	  }
	
}
