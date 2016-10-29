package com.cn.SearchService;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.cn.SearchService.*;


public class ElasticSearchHandler {
     
	 private  static Logger logger=Logger.getLogger(ElasticSearchHandler.class);
     private  static  ElasticSearchHandler  es=null;
	  private static Client client=null;
       
	 /*
	    public ElasticSearchHandler() throws UnknownHostException{    
	        //使用本机做为节点
	        this("127.0.0.1");
	    }
	  */
	  private ElasticSearchHandler(){}
	
	    public static ElasticSearchHandler  getEs(){
	        //集群连接超时设置
	        /*  
	              Settings settings = ImmutableSettings.settingsBuilder().put("client.transport.ping_timeout", "10s").build();
	            client = new TransportClient(settings);
	         */
	    	
	      if(es==null){	
	    	 es = new ElasticSearchHandler();  
	    	  
	    try {
			client = TransportClient.builder().build()
			        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	         return es;
	      }
	      
	    
	    return  es;
	          
	    }
	    
	    
	    /**
	     * 建立索引,索引建立好之后,会在elasticsearch-0.20.6\data\elasticsearch\nodes\0创建所以你看
	     * @param indexName  为索引库名，一个es集群中可以有多个索引库。 名称必须为小写
	     * @param indexType  Type为索引类型，是用来区分同索引库下不同类型的数据的，一个索引库下可以有多个索引类型。
	     * @param jsondata     json格式的数据集合
	     * 
	     * @return
	     */
	
	    public void createIndexResponse(String indexname, String type, List<String> jsondata){
	        //创建索引库 
	        IndexRequestBuilder requestBuilder = client.prepareIndex(indexname, type).setRefresh(true);
	        for(int i=0; i<jsondata.size(); i++){
	            requestBuilder.setSource(jsondata.get(i)).execute().actionGet();
	        }     
	         
	    }
	 
	    /**
	     * 创建索引
	     * @param client
	     * @param jsondata
	     * @return
	     */
	 
	    public IndexResponse createIndexResponse(String indexname, String type,String jsondata){
	        IndexResponse response = client.prepareIndex(indexname, type)
	            .setSource(jsondata)
	            .execute()
	            .actionGet();
	        return response;
	    }

	    
	    /**
	     *   返回总共搜索的数据的条数
	     */
	     public Long sum_data(Long data){ System.out.println("sum-------"+data); return data;}
	    
	    /**
	     * 执行搜索   返回搜索完成的数据
	     * @param queryBuilder
	     * @param indexname
	     * @param type
	     * @return
	     */
	  
	    public List<Medicine>  searcher(QueryBuilder queryBuilder, String indexname, String type){
	        List<Medicine> list = new ArrayList<Medicine>();
	        Long sum=null;
	        SearchResponse searchResponse = client.prepareSearch(indexname).setTypes(type)
	        .setQuery(queryBuilder)
	        .execute()
	        .actionGet();
	        SearchHits hits = searchResponse.getHits();
	        
	        sum_data( (sum=hits.getTotalHits()));
	 
	        SearchHit[] searchHists = hits.getHits();
	        if(searchHists.length>0){
	            for(SearchHit hit:searchHists){
	                Integer id = (Integer)hit.getSource().get("id");
	                String name =  (String) hit.getSource().get("name");
	                String function =  (String) hit.getSource().get("funciton");
	                list.add(new Medicine(id, name, function));
	            }
	        }
	        return list;
	    }
	     /**
	      *  给Controller 提供入口 并返回搜索完成的数据
	      * @param data
	      * @return
	      */
	   public List<Medicine> search_data( String data){
		     logger.info("用户检索数据---------------------------------------------------");
		    List<Medicine> list = new ArrayList<Medicine>();
		
			
			   QueryBuilder queryBuilder = QueryBuilders.boolQuery()
				          .must(QueryBuilders.termQuery("id", 2));
				        list = es.searcher(queryBuilder, data, "typedemo");

		
		return list;
	   }
	
	
}
