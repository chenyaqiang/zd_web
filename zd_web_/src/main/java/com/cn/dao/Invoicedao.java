package com.cn.dao;

import java.util.List;


import com.cn.pojo.Invoice_pojo;


/*将销售人员提供的发票数据插入数据库*/

public interface Invoicedao{
 public int insert_Invoice(Invoice_pojo Invoice);	
 public  List<Invoice_pojo> select_Invoice();
}
