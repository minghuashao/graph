package cn.com.datablau.springgraph.servers;

import java.util.HashMap;
import java.util.Map;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import cn.com.datablau.springgraph.node.GraphNode;

/**
 * 
 * @author 邵明华
   2021年5月24日
 *
 */

@Service
public class NodeServer {
	@Value("${spring.neo4j.uri}")
    private String url;

    @Value("${spring.neo4j.authentication.username}")
    private String username;

    @Value("${spring.neo4j.authentication.password}")
    private String password;
    
    private Driver createDrive() {
		return GraphDatabase.driver(url, AuthTokens.basic(username, password));
	   }
    public String saveNode(GraphNode graphNode) {
    	Long  starttime=System.currentTimeMillis();
    	Map<String,Object> values=new HashMap<String, Object>();
    	values=graphNode.getValues();
    	String businessId=graphNode.getBusinessId();
		String Id =graphNode.getId();
		String Name =graphNode.getName();
	    String Type = graphNode.getType();
	  //*************************************
	    GraphNode nodeobject=new GraphNode();
	    nodeobject.setId(Id);
	    nodeobject.setName(Name);
	    nodeobject.setType(Type);
	    nodeobject.setBusinessId(businessId);
	    createNode(nodeobject,values);
	    String return_businessId=businessIdSearch(nodeobject.getBusinessId());
    	System.out.println(return_businessId);
    	Long endtime=System.currentTimeMillis();
    	System.out.println((endtime-starttime)+"ms"+
    			(endtime-starttime)/1000+"s");
	    return return_businessId;    	
    }
  //******************************************
    private String businessIdSearch(String businessId) {
    	String return_businessId=null;
		try {
			Driver  driver=createDrive();
			Session session= driver.session();
			String cyphersql= String.format(
					"match(a) where a.businessId=\"%s\"  return a",businessId );
			System.out.println("businessIdSearch= "+cyphersql);
			Result result=session.run(cyphersql);
			if(result.hasNext()) {
				Record record=result.next();
				org.neo4j.driver.Value value = record.get("a");					
				Map<String, Object> nodeProperties = value.asMap();
				return_businessId=(String) nodeProperties.get("businessId");
			}
			session.close();
			driver.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return return_businessId;
		
	}
	//******************************************
    private void createNode(GraphNode nodeobject, Map<String, Object> values) {
    	Map<String, Object> map=values;
    	try {
			Driver driver=createDrive();
			String cypherSql = String.format(
					"CREATE(n:Node {Id:\"%s\",Name:\"%s\",Type:\"%s\",businessId:\"%s\"})",
					nodeobject.getId(), nodeobject.getName(),nodeobject.getType(),nodeobject.getBusinessId());
			System.out.println("createNode= "+cypherSql);
			boolean isNode=queryNode(nodeobject.getBusinessId());
			if(!isNode) {
				try(Session session=driver.session()){
					session.writeTransaction( tx -> {
			            tx.run(cypherSql);
			            return 1;
			        });
				  }
			}else {
				System.out.println("节点已经存在！不用再次创建");
			}
			
			if(map!=null && map.size()>0) {
				System.out.println("map.size() "+map.size());
				creatSuperSet(nodeobject,map); 
			}
			
			
			driver.close();			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
  //******************************************
    private boolean queryNode(String businessId) {
    	boolean isNode=false;
    	try {
			Driver  driver=createDrive();
			Session session= driver.session();
			String cyphersql= String.format(
					"match(a) where a.businessId=\"%s\"  return a",businessId );
			System.out.println("queryNode= "+cyphersql);
			Result result=session.run(cyphersql);
			if(result.hasNext()) {
				isNode=true;
			}
			session.close();
			driver.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isNode;
    }
    //******************************************
    private void creatSuperSet(GraphNode nodeobject, Map<String, Object> map) {
    	try {
			
    		Driver driver=createDrive();
			Session  session= driver.session();
			
			for (String key : map.keySet()) {
				 String cypherQuerySql= String.format("match(n) where n.businessId=\"%s\" set n."+key+"="+ "\""+map.get(key)+"\""  + "  return n ", 
						 nodeobject.getBusinessId());
                  System.out.println(cypherQuerySql);
                  session.run(cypherQuerySql);
			 }
			System.out.println("已经创建扩展属性");
			session.close();
			driver.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
  //******************************************
}
