package com.xt.together.model;

import java.io.Serializable;

public class FoodLike implements Serializable{
	private String id ;
	private String name;
	private String head;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public FoodLike(String id, String name, String head) {
		super();
		this.id = id;
		this.name = name;
		this.head = head;
	}
	
	
}
