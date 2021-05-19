package cn.com.datablau.springgraph.controller;

import java.util.HashMap;
import java.util.Map;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;
import cn.com.datablau.springgraph.node.Relation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 
 * @author 邵明华
 * create date 2021年5月13日
 *
 */
@RestController
@RequestMapping("/v4")
@Api(value = "v4", tags = "RelationController", description = "Relation 交互接口")
public class RelationController {
	/*
	 * private String url="bolt://localhost:7687"; private String username="neo4j";
	 * private String password="datablau";
	 */
	@Value("${spring.neo4j.uri}")
    private String url;

    @Value("${spring.neo4j.authentication.username}")
    private String username;

    @Value("${spring.neo4j.authentication.password}")
    private String password;
    
    private Driver createDrive() {
		return GraphDatabase.driver(url, AuthTokens.basic(username, password));
	   }
    
    @ApiOperation(value = "保存关系数据", notes = "获取 Relation   \n"
    		+ "Relation 是 GraphNODE 子类   \n"
    		+ "String Defination= Dam object definition 寓意用于关系名称        \n "
    		+ "String StartbusinessId= Start object businessId ,  \n"
    		+ "String EndbusinessId= End object businessId  ,  \n")
    @RequestMapping(value = "/SaveRelation", method = { RequestMethod.POST },produces="application/json;charset=UTF-8")
    public String SaveRelation(@RequestBody(required = false) Relation relation) {
    	
    	String StartbusinessId=relation.getStartbusinessId();
    	String EndbusinessId=relation.getEndbusinessId();
    	String relationName=relation.getRelationName();
		/*
		 * System.out.println("relation.getBusinessId() "+relation.getBusinessId());
		 * System.out.println("relation.getId() "+relation.getId());
		 * System.out.println("relation.getName() "+relation.getName());
		 * System.out.println("relation.getRelationName() "+relation.getRelationName());
		 * System.out.println("relation.getStartbusinessId() "+relation.
		 * getStartbusinessId());
		 * System.out.println("relation.getEndbusinessId() "+relation.getEndbusinessId()
		 * ); System.out.println("relation.getType() "+relation.getType());
		 * System.out.println("relation.getUUID() "+relation.getUUID());
		 */
		//boolean isDual=relation.getIsDual();
		try {			
			Driver  driver=  createDrive();
			Session session =driver.session();
			boolean isstartExit=queryRelation(StartbusinessId);
			boolean isendExit=queryRelation(EndbusinessId);
			//Map<String, String> startMap=queryNode(StartbusinessId);
			//Map<String, String> endMap=queryNode(EndbusinessId);
			//System.out.println(startMap.get("defination"));
			if(isstartExit && isendExit ) {
				String cypherSQL=String.format(
						"match(a),(b) where a.businessId=\"%s\" and b.businessId=\"%s\" create (a)-[r:%s]->(b)  ",
						StartbusinessId, EndbusinessId, relationName );	
				System.out.println(cypherSQL);
				session.run(cypherSQL);
				
				/*
				 * if(!isDual) { String cypherSQL=String.format(
				 * "match(a),(b) where a.businessId=\"%s\" and b.businessId=\"%s\" create (a)-[r:%s]->(b)  "
				 * , StartbusinessId, EndbusinessId, startMap.get("defination") );
				 * System.out.println(cypherSQL); session.run(cypherSQL); }else { String
				 * cypherSQL=String.format(
				 * "match(a),(b) where a.businessId=\"%s\" and b.businessId=\"%s\" create (a)-[r:%s]->(b)  "
				 * , StartbusinessId, EndbusinessId, startMap.get("defination") );
				 * System.out.println(cypherSQL); session.run(cypherSQL); String
				 * cypherSQL1=String.format(
				 * "match(a),(b) where a.businessId=\"%s\" and b.businessId=\"%s\" create (a)-[r:%s]->(b) "
				 * , EndbusinessId,StartbusinessId, endMap.get("defination") );
				 * System.out.println(cypherSQL1); session.run(cypherSQL1); }
				 */
				
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
			/*
			 * Result result =session.run(cypherSQL); while(result.hasNext()) { Record
			 * record=result.next();
			 * System.out.println("relation==  "+record.get("type").asString()); //Value
			 * value = record.get("type");
			 * 
			 * }
			 */
			//Node endtnode=queryNode(EndbusinessId);		    
			session.close();
			driver.close();	
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return StartbusinessId+EndbusinessId;
    	
    }
    
	private Map<String, String> queryNode(String startbusinessId) {
		
		Map<String, String> map = new HashMap<String, String>();
		
		try {
			
			Driver  driver= createDrive();
			Session  session= driver.session();
			String querycypher=String.format("match(a) where a.businessId=\"%s\" return a,a.Defination as defination  ", startbusinessId);		
			System.out.println("querycypher "+querycypher);
			Result result= session.run(querycypher);
			if(result.hasNext()) {
				Record record = result.next();
				map.put("defination", record.get("defination").asString());
					
//				map.put("isDual", record.get("isDual").asString());
				System.out.println(record.get("defination").asString());
//				System.out.println(record.get("isDual").asString());
				/*
				 * Value value = record.get("a"); Map<String, Object> nodeProperties =
				 * value.asMap();
				 * 
				 * Long UUID=(Long) nodeProperties.get("UUID"); Long Id=(Long)
				 * nodeProperties.get("Id"); String Name=(String) nodeProperties.get("Name");
				 * String Type=(String) nodeProperties.get("Type"); String Defination=(String)
				 * nodeProperties.get("Defination"); String businessId=(String)
				 * nodeProperties.get("businessId"); boolean isDual=(boolean)
				 * nodeProperties.get("isDual");
				 * 
				 * node.setUUID(UUID); node.setId(Id); node.setName(Name); node.setType(Type);
				 * node.setDefination(Defination); node.setBusinessId(businessId);
				 * node.setIsDual(isDual);
				 */								
			}
			session.close();
			driver.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	private boolean queryRelation(String setStartbusinessId) {
		boolean isexit=false;
		try {
			Driver  driver= createDrive();
			Session  session= driver.session();
			String querycypher=String.format("match(a) where a.businessId=\"%s\" return a", setStartbusinessId);		
			System.out.println("queryRelation "+querycypher);
			Result result= session.run(querycypher);
			if(result.hasNext()) {
				isexit=true;
			}
			session.close();
			driver.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isexit;
	}

}
