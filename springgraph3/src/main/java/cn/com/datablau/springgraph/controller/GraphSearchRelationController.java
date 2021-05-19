package cn.com.datablau.springgraph.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import cn.com.datablau.springgraph.servers.QueryRelationServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;

/**
 * 
 * @author 邵明华
 * create date 2021年5月18日
 *
 */

@RestController
@RequestMapping("/v5")
@Api(value = "v5", tags = "GraphSearchRelationController", description = "关系查询接口")
public class GraphSearchRelationController {
	@Value("${spring.neo4j.uri}")
    private String url;

    @Value("${spring.neo4j.authentication.username}")
    private String username;

    @Value("${spring.neo4j.authentication.password}")
    private String password;
    
	private Driver createDrive() {
		return GraphDatabase.driver(url, AuthTokens.basic(username, password));
	}
	
	private QueryRelationServer queryRelationServer;
	@ApiOperation(value = "查询节点的关系", notes = "根据当前节点的Id，Type \n 获取 下一节点的信息")
	@RequestMapping(value = "/SearchRelation", method = { RequestMethod.GET })	
	public JSONArray SearchRelation(@RequestParam(value = "Id",required = true) Long Id, @RequestParam(value = "Type",required = true)Long Type ) {
		System.out.println(Id+"------"+Type);
		return searchIdAndTypeRelation(Id, Type);
		
	}
	private JSONArray searchIdAndTypeRelation(Long id, Long type) {
		System.out.println("searchIdAndTypeRelation"+id+"......."+type);
		List<Map<String, Object>>  listmap = new ArrayList<Map<String, Object>>();
		try {
			Driver  driver =createDrive();
			Session  session = driver.session();
			String   cypherSQL=String.format("match(a)-[r]->(b) where a.Id=\"%s\" and a.Type=\"%s\" return a,type(r) as type,b",
					id, type);
			System.out.println(cypherSQL);
			Result result=session.run(cypherSQL);
			while(result.hasNext()) {
				Record  record= result.next();
                Map<String, Object> nodeMap= new HashMap<String, Object>();
								
				org.neo4j.driver.Value startvalue = record.get("a");					
				Map<String, Object> startProperties = startvalue.asMap();
				Long sourceUUID=(Long) startProperties.get("UUID");
				String sourceId=(String) startProperties.get("Id");
				String source=(String) startProperties.get("Name");
				String sourceType=(String) startProperties.get("Type");
				String sourcebusinessId=(String) startProperties.get("businessId");
				
				nodeMap.put("sourceUUID", sourceUUID);
				nodeMap.put("sourceId", sourceId);
				nodeMap.put("source", source);
				nodeMap.put("sourceType", sourceType);
				nodeMap.put("sourcebusinessId", sourcebusinessId);
												
				org.neo4j.driver.Value endvalue = record.get("b");
				Map<String, Object> endProperties = endvalue.asMap();
				Long targetUUID=(Long) endProperties.get("UUID");
				String targetId=(String) endProperties.get("Id");
				String target=(String) endProperties.get("Name");
				String targetType=(String) endProperties.get("Type");
				String targetbusinessId=(String) endProperties.get("businessId");
								
				nodeMap.put("targetUUID", targetUUID);
				nodeMap.put("targetId", targetId);
				nodeMap.put("target", target);
				nodeMap.put("targetType", targetType);
				nodeMap.put("targetbusinessId", targetbusinessId);
				
				nodeMap.put("rela", record.get("type").asString());
				listmap.add(nodeMap);
				
				System.out.println(record.get("type").asString());
			}
			
			session.close();
			driver.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		for(int i=0;i<listmap.size();i++) {
			Map<String, Object>  map1=listmap.get(i);
			for(int j=i+1;j<listmap.size();j++) {
				Map<String, Object>  map2=listmap.get(j);
				if(map1.equals(map2)) {
					listmap.remove(j);
					continue;
				}
			}
		}
		JSONArray jsonArray=JSONArray.fromObject(listmap);
		System.out.println(jsonArray);
		return  jsonArray;
	}

}
