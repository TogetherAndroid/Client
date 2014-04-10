package com.xt.together.model;

import java.io.Serializable;

public class Food implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String Id;
	private String name;
	private String shop;
	private String price;
	private String share;
	private String image;
	private String address;
	private String description;
	private FoodLike[] foodlike;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getShop() {
		return shop;
	}
	public void setShop(String shop) {
		this.shop = shop;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getShare() {
		return share;
	}
	public void setShare(String share) {
		this.share = share;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
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
	
	public FoodLike[] getFoodlike() {
		return foodlike;
	}
	public void setFoodlike(FoodLike[] foodlike) {
		this.foodlike = foodlike;
	}
		
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public Food(String name, String shop, String price, String share,
			String image, String address, String description, FoodLike[] foodlike) {
		super();
		this.name = name;
		this.shop = shop;
		this.price = price;
		this.share = share;
		this.image = image;
		this.address = address;
		this.description = description;
		this.foodlike = foodlike;
	}
	
	public Food(String name, String shop, String price, String share, String image , String address, String description) {
		this.name = name;
		this.shop = shop;
		this.price = price;
		this.share = share;
		this.image = image;
		this.address = address;
		this.description = description;
	}	
	
	public Food(String id, String name, String shop, String price,
			String share, String image, String address, String description,
			FoodLike[] foodlike) {
		this.Id = id;
		this.name = name;
		this.shop = shop;
		this.price = price;
		this.share = share;
		this.image = image;
		this.address = address;
		this.description = description;
		this.foodlike = foodlike;
	}
	
	public static Food getFood() {
		return new Food("name", "shop", "price", "share", "http://b.hiphotos.baidu.com/baike/w%3D268/sign=783b52ce3bdbb6fd255be2203125aba6/b219ebc4b74543a9abc71cf91c178a82b9011422.jpg", "木有地址", "木有描述");
	}
}
