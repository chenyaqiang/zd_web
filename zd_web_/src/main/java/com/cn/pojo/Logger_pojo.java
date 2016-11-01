package com.cn.pojo;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * 日志Bean  记录 方法的调用和各种违规的操作
 * @author 
 *
 */
@Component()
@Data
public class Logger_pojo {
     private Long id;
	 private String description; //方法描述
	 private String method; //调用方法
	 private String  ip; //调用者IP
	 private String name;// 姓名
	
	
}



