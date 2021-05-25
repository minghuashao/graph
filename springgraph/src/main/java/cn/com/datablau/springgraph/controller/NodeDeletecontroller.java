package cn.com.datablau.springgraph.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import cn.com.datablau.springgraph.servers.NodeDeleteServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * @author 邵明华
 * create date 2021年5月20日
 *
 */

@RestController
@RequestMapping("/v3")
@Api(value = "v3", tags = "NodeDeletecontroller", description = "Node 删除节点接口")
public class NodeDeletecontroller {
	
	@Autowired
	private NodeDeleteServer nodeDeleteServer;
	
    @ApiOperation(value = "删除节点", notes = " 根据 businessId 删除节点 \n")
    @RequestMapping(value = "/DeleteNode", method = { RequestMethod.POST },produces="application/json;charset=UTF-8")
    public void DeleteNode(@RequestParam(value = "businessId") String businessId) {
    	nodeDeleteServer.deleteServer(businessId);
    }

}
