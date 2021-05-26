package cn.com.datablau.springgraph.servers;

import java.util.HashMap;
import java.util.Map;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.com.datablau.springgraph.node.Relation;

/**
 * 
 * @author 邵明华
   2021年5月24日
 *
 */
@Service
public class RelationServer {
	@Autowired
	private Session  session;
    public String saveRelation(Relation relation) {
    	String StartbusinessId=relation.getStartbusinessId();
    	String EndbusinessId=relation.getEndbusinessId();
    	String relationName=relation.getRelationName();
    	String Id=relation.getId();
    	String Name=relation.getName();
    	String businessId =relation.getBusinessId();
    	String type=relation.getType();
    	Map<String,Object> values=new HashMap<String, Object>();
    	values=relation.getValues();
    	try {
    		
			boolean isstartExit=queryRelation(StartbusinessId);
			boolean isendExit=queryRelation(EndbusinessId);
			if(isstartExit && isendExit ) {
				if(values !=null && values.size()>0) {
					String createMap="";
					for (String key : values.keySet()) {
		        		createMap+=key+":"+ "\""+values.get(key)+"\""+",";
		        	}
		        	String createMaptotal=createMap.substring(0, createMap.length()-1);
		        	System.out.println("createMap=  "+ createMaptotal);
					String cypherCreateSQL=String.format(
							"match(a),(b) where a.businessId=\"%s\" and b.businessId=\"%s\" create (a)-[r:%s{businessId:\"%s\",Id:\"%s\",Name:\"%s\",Type:\"%s\",startbusinessId:\"%s\",endbusinessId:\"%s\",relationName:\"%s\",%s}]->(b)  return a,r,b ",
							StartbusinessId, EndbusinessId, relationName,businessId,Id,Name,type,StartbusinessId,EndbusinessId,relationName,createMaptotal );	
					System.out.println(cypherCreateSQL);
					session.run(cypherCreateSQL);
				}else {
					String cypherCreateSQL=String.format(
							"match(a),(b) where a.businessId=\"%s\" and b.businessId=\"%s\" create (a)-[r:%s{businessId:\"%s\",Id:\"%s\",Name:\"%s\",Type:\"%s\",startbusinessId:\"%s\",endbusinessId:\"%s\",relationName:\"%s\"}]->(b)  return a,r,b ",
							StartbusinessId, EndbusinessId, relationName,businessId,Id,Name,type,StartbusinessId,EndbusinessId,relationName );	
					System.out.println(cypherCreateSQL);
					session.run(cypherCreateSQL);
				}
				
				System.out.println("带属性的关系已经创建...");
			}else {
				System.out.println("孤立节点无法建立关系或不存在该节点");
			}
			
			String cypherSQL=String.format(
					"match(a)-[r]->(b) where a.businessId=\"%s\" and b.businessId=\"%s\" "+
					"WITH a, b, TAIL (COLLECT (r)) as rr " + 
					"FOREACH (r IN rr | DELETE r) ",
					StartbusinessId, EndbusinessId);
			System.out.println(cypherSQL);
			session.run(cypherSQL);
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
    	
		}
    	
    	return StartbusinessId+" "+EndbusinessId;
    	
    }

    	private boolean queryRelation(String setStartbusinessId) {
    		boolean isexit=false;
    		try {
    			
    			String querycypher=String.format("match(a) where a.businessId=\"%s\" return a", setStartbusinessId);		
    			System.out.println("queryRelation "+querycypher);
    			Result result= session.run(querycypher);
    			if(result.hasNext()) {
    				isexit=true;
    			}
    			
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		return isexit;
    	}	
}
