package cn.com.datablau.springgraph.servers;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 
 * @author 邵明华
   2021年5月24日
 *
 */

@Service
public class NodeDeleteServer {
	@Value("${spring.neo4j.uri}")
    private String url;

    @Value("${spring.neo4j.authentication.username}")
    private String username;

    @Value("${spring.neo4j.authentication.password}")
    private String password;
    
    private Driver createDrive() {
		return GraphDatabase.driver(url, AuthTokens.basic(username, password));
	   }
    public  void deleteServer(String businessId) {
    	try {
			
    		Driver  driver=createDrive();
			Session  session=driver.session();
			
			String cypherSQL=String.format("match(n)-[r]-(t) where n.businessId=\"%s\" delete n ,r    ", businessId);
			System.out.println(cypherSQL);
			Result result= session.run(cypherSQL);
			if(!result.hasNext()) {
				String cypherSQLNode=String.format("match(n) where n.businessId=\"%s\" delete n   ", businessId);
				System.out.println(cypherSQLNode);
				session.run(cypherSQL);		
			}
			System.out.println("已删除该节点及关系");
			session.close();
			driver.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
