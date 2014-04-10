package com.xt.together.model;

import java.io.Serializable;

public class Trends implements Serializable{
	private String id;
	private String name;
	private String address;
	private String description;
	private TrendsLike[] trendslike;
	private String image;
	
	public Trends(String id, String name, String address, String description,
			TrendsLike[] trendslike , String image) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.description = description;
		this.trendslike = trendslike;
		this.image = image;
	}

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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TrendsLike[] getTrendslike() {
		return trendslike;
	}

	public void setTrendslike(TrendsLike[] trendslike) {
		this.trendslike = trendslike;
	}
	
	public void setImage( String image){
		this.image = image;
	}
	
	public String getImage( ){
		return image;
	}
}
