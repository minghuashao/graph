package cn.com.datablau.springgraph.node;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 
 * @author 邵明华
 * create date 2021年5月13日
 *
 */
@Data
@ApiModel
public class GraphNode {
	private Long UUID; //Type+Id mapping to uuid
	private String   Id;  //Dam object native id
    private String Name;//Dam object name;
    private String Type;//Dam object type;
//    private String Defination; // Dam object definition
    private String businessId; //return Type + Id
	public Long getUUID() {
		return UUID;
	}
	public void setUUID(Long uUID) {
		UUID = uUID;
	}

	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}
	
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
    

}
