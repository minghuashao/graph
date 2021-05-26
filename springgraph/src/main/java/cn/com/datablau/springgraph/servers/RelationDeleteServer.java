package cn.com.datablau.springgraph.servers;

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
public class RelationDeleteServer {
	@Autowired
	private Session  session;
    public  void deleteRelation(String businessId) {
    	try {
    		
			String cypherSQL=String.format("match(n)-[r]-(t) where r.businessId=~\"%s\" delete  r    ", businessId);
    		System.out.println("DeleteRelation "+cypherSQL);
    		session.run(cypherSQL);
    		System.out.println("节点的关系已删除");
    	
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

}
