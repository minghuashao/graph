package cn.com.datablau.springgraph.servers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * @author 邵明华
   2021年5月24日
 *
 */
@Service
public class SearchNodeRelationServer {
	@Autowired
	private Session  session;
	public List<Map<String, Object>> searchNodeRelation(String Id,String  Type,Integer layers) {
		List<Map<String, Object>>  listmap = new ArrayList<Map<String, Object>>();
		
		try {
			
			String   cypherSQL=String.format("match(a)-[r]->(b) where a.Id=\"%s\" or a.Type=\"%s\" return properties(a) as startNode,properties(r) as  relation ,properties(b) as endNode", Id, Type);
			System.out.println("cypherSQL= "+cypherSQL);
			Result result=session.run(cypherSQL);
			while(result.hasNext()) {
				Record record=result.next();
				
				if(!record.get("relation").isEmpty()) {
					Map<String, Object> resultMap = new HashMap<String, Object>();
					resultMap.put("startNode", record.get("startNode").asObject());
					resultMap.put("relation", record.get("relation").asObject());
					resultMap.put("endNode", record.get("endNode").asObject());
					listmap.add(resultMap);
					System.out.println(resultMap.toString());
				}
				
				
				System.out.println("****************************");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/*JSONArray jsonArray=JSONArray.fromObject(listmap);
		System.out.println(jsonArray);
		return  jsonArray;*/
		return  listmap;
		
	}
	

}
