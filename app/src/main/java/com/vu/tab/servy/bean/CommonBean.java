package com.vu.tab.servy.bean;

public class CommonBean {
private Integer id;
private String name;
public Integer getId() {
	return id;
}
public void setId(Integer id) {
	this.id = id;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public CommonBean(Integer id, String name) {
	super();
	this.id = id;
	this.name = name;
}
public CommonBean() {
	super();
}


}
