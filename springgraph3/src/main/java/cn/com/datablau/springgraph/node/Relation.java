package cn.com.datablau.springgraph.node;

public class Relation extends GraphNode {
	private String  StartbusinessId; //Start object uuid
	private String  EndbusinessId;	//End object uuid
	private String  RelationName ; // relation's name
	public String getStartbusinessId() {
		return StartbusinessId;
	}
	public void setStartbusinessId(String startbusinessId) {
		StartbusinessId = startbusinessId;
	}
	public String getEndbusinessId() {
		return EndbusinessId;
	}
	public void setEndbusinessId(String endbusinessId) {
		EndbusinessId = endbusinessId;
	}
	public String getRelationName() {
		return RelationName;
	}
	public void setRelationName(String relationName) {
		RelationName = relationName;
	}
	
	
	
}
