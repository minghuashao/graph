package cn.com.datablau.springgraph.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import cn.com.datablau.springgraph.servers.RelationDeleteServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * @author 邵明华
 * create date 2021年5月20日
 *
 */

@RestController
@RequestMapping("/v4")
@Api(value = "v4", tags = "RelationDeleteController", description = "Relation 删除关系接口")
public class RelationDeleteController {

	@Autowired
	private RelationDeleteServer relationDeleteServer;
    @ApiOperation(value = "删除关系", notes = " 根据 businessId 删除关系 \n")
    @RequestMapping(value = "/DeleteRelation", method = { RequestMethod.POST },produces="application/json;charset=UTF-8")
    public void DeleteRelation(@RequestParam(value = "businessId") String businessId) {
    	relationDeleteServer.deleteRelation(businessId);
    	
    }
}
