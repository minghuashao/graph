package cn.com.datablau.springgraph.servers;

import java.util.ArrayList;
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
import cn.com.datablau.springgraph.node.GraphNode;
import net.sf.json.JSONArray;

//@Service
public class QueryRelationServer {
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

	/*
	 * @Autowired private QueryRelation queryRelation;
	 */
	public JSONArray  searchRelation(Long Id, Long Type) {
		List<Map<String, Object>>  listmap = new ArrayList<Map<String, Object>>();
		System.out.println("Long Id, Long Type"+Id+"......."+Type);
		List<GraphNode> list=searchIdAndTypeRelation(Id, Type);
		for(int i=0;i<list.size();i++) {
			System.out.println(list.get(i).getId());
			System.out.println(list.get(i).getType());
		}
		return null;
		
	}

	public List<GraphNode> searchIdAndTypeRelation(Long id, Long type) {
		System.out.println("searchIdAndTypeRelation"+id+"......."+type);
		try {
			Driver  driver =createDrive();
			Session  session = driver.session();
			String   cypherSQL=String.format("match(a)-[r]->(b) where a.Id=\"%s\" and b.Type=\"%s\" return a,type(r) as type,b",
					id, type);
			System.out.println(cypherSQL);
			Result result=session.run(cypherSQL);
			while(result.hasNext()) {
				Record  record= result.next();
				System.out.println(record.get("type").asString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
