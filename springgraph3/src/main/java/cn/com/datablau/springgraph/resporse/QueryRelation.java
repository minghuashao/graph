package cn.com.datablau.springgraph.resporse;

import java.util.List;


import org.springframework.stereotype.Repository;
import cn.com.datablau.springgraph.node.GraphNode;
import cn.com.datablau.springgraph.node.Relation;


/**
 * 
 * @author 邵明华
 * create date 2021年5月18日
 *
 */


//
@Repository

public interface QueryRelation {
	//match(n:Node{Type:{\"82800010\"},Id:{\"admin\"}})-[r]->(m)  return n,r,m
	//@Query("MATCH (n:Node) RETURN n ")
	//@Query("match(n:Node{Id:{Id},Type:{Type}})-[r]->(m)  return n,type(r) as type,m")
    //@Query("match(n:Node{Id:{Id},Type:{Type}})-[r]->(m)  return n,type(r) as type,m")
    List<GraphNode>  searchRelation(Long Id, Long Type);
	
}
