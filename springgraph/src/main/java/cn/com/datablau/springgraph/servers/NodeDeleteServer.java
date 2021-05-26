package cn.com.datablau.springgraph.servers;

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
public class NodeDeleteServer {
	@Autowired
	private Session  session;
    public  void deleteServer(String businessId) {
    	try {
			String cypherSQL=String.format("match(n)-[r]-(t) where n.businessId=\"%s\" delete n ,r    ", businessId);
			System.out.println(cypherSQL);
			Result result= session.run(cypherSQL);
			if(!result.hasNext()) {
				String cypherSQLNode=String.format("match(n) where n.businessId=\"%s\" delete n   ", businessId);
				System.out.println(cypherSQLNode);
				session.run(cypherSQL);		
			}
			System.out.println("已删除该节点及关系");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
