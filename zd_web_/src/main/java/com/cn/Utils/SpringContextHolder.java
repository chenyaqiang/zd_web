package com.cn.Utils;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 
 * @author 周杰  
 * 
 * 已静态变量保存Spring ApplicationContext, 可在任何代码任何地方取出ApplicationContext
 *
 */
public class SpringContextHolder implements ApplicationContextAware {

	 
	
	 private static ApplicationContext applicationContext;
	
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringContextHolder.applicationContext=applicationContext;
		
	}
	
	// 检查静态变量
	private static void checkApplicationContext(){
		 if(applicationContext == null)
		 throw new IllegalStateException("application为空!!!"); 	 
	}
	
	//取得存储在静态变量中的ApplicationContext
	public  static ApplicationContext getApplicationContext(){
		 checkApplicationContext();
		 
		 return applicationContext;
	}
	
	//从静态变量ApplicationContext中取得Bean，自动转型为所赋值对象的类型
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name){
		 checkApplicationContext();
		 
		 return (T)applicationContext.getBean(name);
	}
	
	
	//如果有多个Bean符合Class 取出第一个
	
	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<T> clazz){
		checkApplicationContext();
		@SuppressWarnings("rawtypes")
		Map beanMaps = applicationContext.getBeansOfType(clazz);
		
	 if(beanMaps!=null && !beanMaps.isEmpty())
		 return (T) beanMaps.values().iterator().next();
	 
	 else 
		  return null;
	}
	

}
