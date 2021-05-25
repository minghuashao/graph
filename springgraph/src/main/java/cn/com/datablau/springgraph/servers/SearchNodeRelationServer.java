package cn.com.datablau.springgraph.servers;

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
import org.springframework.stereotype.Service;
import net.sf.json.JSONArray;

/**
 * 
 * @author 邵明华
   2021年5月24日
 *
 */
@Service
public class SearchNodeRelationServer {
	@Value("${spring.neo4j.uri}")
    private String url;

    @Value("${spring.neo4j.authentication.username}")
    private String username;

    @Value("${spring.neo4j.authentication.password}")
    private String password;
    
	private Driver createDrive() {
		return GraphDatabase.driver(url, AuthTokens.basic(username, password));
	}
	public JSONArray searchNodeRelation(String Id,String  Type) {
		List<Map<String, Object>>  listmap = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>>  resultmap = new ArrayList<Map<String, Object>>();
		
		try {
			Driver  driver =createDrive();
			Session  session = driver.session();
			String   cypherSQL=String.format("match(a)-[r]->(b) where a.Id=\"%s\" or a.Type=\"%s\" return properties(a) as startNode,properties(r) as  relation ,properties(b) as endNode", Id, Type);
			
			System.out.println("cypherSQL= "+cypherSQL);
			
			Result result=session.run(cypherSQL);
			
			while(result.hasNext()) {
				
				Map<String, Object> map=new HashMap<String, Object>();
				Record record=result.next();
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("startNode", record.get("startNode").asObject());
				resultMap.put("relation", record.get("relation").asObject());
				resultMap.put("endNode", record.get("endNode").asObject());
				listmap.add(resultMap);
				
				/*
				 * if(!record.get("relation").isEmpty()) { map.put("startNode",
				 * record.get("startNode").asObject()); System.out.println("startNode= "
				 * +record.get("startNode").asObject()); map.put("relation",
				 * record.get("relation").asObject()); System.out.println("pro==  "
				 * +record.get("relation").asObject()); map.put("endNode",
				 * record.get("endNode").asObject()); System.out.println("endNode= "
				 * +record.get("endNode").asObject()); listmap.add(map); }
				 */
				
				System.out.println(map.toString());
				
				System.out.println("****************************");
			}
			session.close();
			driver.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JSONArray jsonArray=JSONArray.fromObject(listmap);
		System.out.println(jsonArray);
		return  jsonArray;
		
	}
	

}
