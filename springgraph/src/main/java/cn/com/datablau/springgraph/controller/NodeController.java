package cn.com.datablau.springgraph.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import cn.com.datablau.springgraph.node.GraphNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * @author 邵明华
 * create date 2021年5月13日
 *
 */
@RestController
@RequestMapping("/v3")
@Api(value = "v3", tags = "NodeController", description = "Node 交互接口")
public class NodeController {
	private String url="bolt://localhost:7687";
    private String username="neo4j";
    private String password="datablau";
    private Driver createDrive() {
		return GraphDatabase.driver(url, AuthTokens.basic(username, password));
	   }
    @ApiOperation(value = "保存节点数据", 
			notes = "获取 GraphNODE   \n "
					+"Long UUID , 自动生成 不需要传送 \n"
					+"String id=Dam object native id  由DAM产生， \n "
					+"String name=Dam object name, \n"
					+"String Type=Dam object type 寓意用于分组，\n "
					+"String businessId= return Type + Id  \n "		
//					+"isDual = false  if isDual = ture  Is double direction \n  "
					+"返回值  \n "
//					+ "String businessId= return Type + Id  \n "
					+"获取 Relation \n"
					+"Relation 是 GraphNODE 子类   \n"
					+"String Defination= Dam object definition 寓意用于关系名称        \n   "
					+"String StartbusinessId= Start object businessId ,  \n"
					+"String EndbusinessId= End object businessId  ,  \n"
//					+"isDual = false  if isDual = ture  Is double direction \n "
//					+"返回值  \n "
//					+ "String businessId=  StartUUID+EndUUID \n"
			     )
    @RequestMapping(value = "/SaveNode", method = { RequestMethod.POST },produces="application/json;charset=UTF-8")
    public String SaveNode(@RequestBody(required = false) GraphNode graphNode ) {
		
    	Long UUID=uuidUtil();
    	String businessId=graphNode.getBusinessId();
		String Id =graphNode.getId();
		String Name =graphNode.getName();
	    String Type = graphNode.getType();
	    //String Defination =graphNode.getDefination(); 	    
	   // Boolean isDual = node.getIsDual();	    
	    //*************************************
	    GraphNode nodeobject=new GraphNode();
	    nodeobject.setUUID(UUID);
	    nodeobject.setId(Id);
	    nodeobject.setName(Name);
	    nodeobject.setType(Type);
	    nodeobject.setBusinessId(businessId);
	    //nodeobject.setDefination(Defination);
	   // nodeobject.setIsDual(isDual);
	    //*************************************
	    createNode(nodeobject);
	    String return_businessId=businessIdSearch(nodeobject.getBusinessId());
    	System.out.println(return_businessId);
	    return return_businessId;
	  //*************************************
    	
    }
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
				Value value = record.get("a");					
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
	private void createNode(GraphNode nodeobject) {
		try {
			Driver driver=createDrive();
			String cypherSql = String.format(
					"CREATE(n:Node {UUID:%d,Id:\"%s\",Name:\"%s\",Type:\"%s\",businessId:\"%s\" })",
					nodeobject.getUUID(), nodeobject.getId(), nodeobject.getName(),nodeobject.getType(),nodeobject.getBusinessId() );
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
			driver.close();			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
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
	private Long uuidUtil() {
		String trandNo = String.valueOf((Math.random() * 9 + 1) * 1000000);
		String sdf = new SimpleDateFormat("yyyyMMddHHMMSS").format(new Date());
		//十九位 10的19幂  修改位10的16次，以免重复
		//String orderNo = trandNo.toString().substring(0, 4);		
		String orderNo = trandNo.toString().substring(0, 1);
		orderNo = orderNo + sdf;
		Long result = Long.valueOf(orderNo);
		return result;
	}

}
