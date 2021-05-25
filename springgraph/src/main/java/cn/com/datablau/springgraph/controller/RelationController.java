package cn.com.datablau.springgraph.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import cn.com.datablau.springgraph.node.Relation;
import cn.com.datablau.springgraph.servers.RelationServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 
 * @author 邵明华
 * create date 2021年5月13日
 *
 */
@RestController
@RequestMapping("/v4")
@Api(value = "v4", tags = "RelationController", description = "Relation 交互接口")
public class RelationController {
	
	@Autowired
	private RelationServer relationServer;
    @ApiOperation(value = "保存关系数据", notes = "获取 Relation   \n"
    		+ "Relation 是 GraphNODE 子类   \n"
    		+ "String Defination= Dam object definition 寓意用于关系名称        \n "
    		+ "String StartbusinessId= Start object businessId ,  \n"
    		+ "String EndbusinessId= End object businessId  ,  \n")
    @RequestMapping(value = "/SaveRelation", method = { RequestMethod.POST },produces="application/json;charset=UTF-8")
    public String SaveRelation(@RequestBody(required = false) Relation relation) {
		String result=relationServer.saveRelation(relation);
    	return result;
    	
    }

    
}
