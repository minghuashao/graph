package cn.com.datablau.springgraph.controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import cn.com.datablau.springgraph.servers.SearchNodeRelationServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 
 * @author 邵明华
 * create date 2021年5月21日
 *
 */
@RestController
@RequestMapping("/v6")
@Api(value = "v6", tags = "SearchRelationController", description = "查询接口")
public class SearchRelationController {
	
	@Autowired
	private SearchNodeRelationServer searchNodeRelationServer;
	
	@ApiOperation(value = "查询节点及关系", notes = "根据当前关系的Id，Type \n 获取 下一节点的信息")
	@RequestMapping(value = "/SearchNodeRelation", method = { RequestMethod.GET })	
	public List<Map<String, Object>> SearchNodeRelation(@RequestParam(value = "Id",required = true) String Id,
			@RequestParam(value = "Type",required = true)String Type,			
			@RequestParam(value = "layers",required = true)Integer  layers) {
		System.out.println(layers);
		List<Map<String, Object>> jsonArray=searchNodeRelationServer.searchNodeRelation(Id, Type,layers);
	    return  jsonArray;
	}

}
