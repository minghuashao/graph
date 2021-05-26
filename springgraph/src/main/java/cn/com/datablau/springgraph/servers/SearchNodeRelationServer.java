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

import net.sf.json.JSONArray;

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
			//String   cypherSQL=String.format("match(a)-[r*0..%d]->(b) where a.Id=\"%s\" or a.Type=\"%s\" return properties(a) as startNode,properties(r) as  relation ,properties(b) as endNode", Id, Type);
			String   cypherSQL=String.format("match(a)-[r*0..%d]->(b) where a.Id=\"%s\" and a.Type=\"%s\" return id(a) as ida,properties(a) as startNode, r ,properties(b) as endNode,id(b) as idb",layers, Id, Type);
			System.out.println(cypherSQL);
			Result result=session.run(cypherSQL);
			System.out.println("*********************");
			System.out.println("*********************"+result.hasNext() );
			while(result.hasNext()) {
				Map<String, Object> map=new HashMap<String, Object>();
				Record record=result.next();				
				map.put("startNode", record.get("startNode").asObject());
				Long ida=record.get("ida").asLong();
				Long idb=record.get("idb").asLong();
				Map<String, Object> relation= creatRelationProperties(ida,idb);
				map.put("relation", relation);
				map.put("endNode", record.get("endNode").asObject());
				System.out.println(map.toString());
				listmap.add(map);
				
			}
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JSONArray jsonArray=JSONArray.fromObject(listmap);
		System.out.println(jsonArray);
		return  listmap;
		
	}
	
	private Map<String, Object> creatRelationProperties(Long ida, Long idb) {
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			String queryRelation=String.format("match(a)-[r]->(b) where id(a)=%d and id(b)=%d return  properties(r) as  pro", ida,idb);
			System.out.println(queryRelation);
			Result relationResult=session.run(queryRelation);
			
			if(relationResult.hasNext()) {
				Record  record=relationResult.next();
				map= record.get("pro").asMap();
			}
		} catch (Exception e) {
			e.printStackTrace();
		
		}
		return map;
		
		
	}

}
