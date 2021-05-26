package cn.com.datablau.springgraph.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import cn.com.datablau.springgraph.node.GraphNode;
import cn.com.datablau.springgraph.servers.NodeServer;
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

	@Autowired
	private NodeServer nodeServer;
	
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
		String  result=nodeServer.saveNode(graphNode);
    	return result;
    	
    }	

}
