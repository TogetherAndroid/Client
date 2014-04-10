package com.xt.together.model;

import java.io.Serializable;

public class FriendsCircle implements Serializable{
	
	private String name;
	private String address;
	private String description;
	private String image;
	private FriendsCirCleUser friendsCirCleUser;
	private FoodLike[] foodLike;
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
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public FriendsCirCleUser getFriendsCirCleUser() {
		return friendsCirCleUser;
	}
	public void setFriendsCirCleUser(FriendsCirCleUser friendsCirCleUser) {
		this.friendsCirCleUser = friendsCirCleUser;
	}
	public FoodLike[] getFoodLike() {
		return foodLike;
	}
	public void setFoodLike(FoodLike[] foodLike) {
		this.foodLike = foodLike;
	}
	public FriendsCircle(String name, String address, String description,
			String image, FriendsCirCleUser friendsCirCleUser,
			FoodLike[] foodLike) {
		super();
		this.name = name;
		this.address = address;
		this.description = description;
		this.image = image;
		this.friendsCirCleUser = friendsCirCleUser;
		this.foodLike = foodLike;
	}
}
