package com.xt.together.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Restaurant implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -790124162359628419L;
	
	private String name      = null;
	private String average   = null;
	private String specialty = null;
	private String like      = null;
	private String address   = null;
	private String phone     = null;
	private String image     = null;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAverage() {
		return average;
	}
	public void setAverage(String average) {
		this.average = average;
	}
	public String getSpecialty() {
		return specialty;
	}
	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}
	public String getLike() {
		return like;
	}
	public void setLike(String like) {
		this.like = like;
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
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public Restaurant(String name, String average, String like
			, String specialty, String address, String phone, String image) {
		this.name = name;
		this.average = average;
		this.like = like;
		this.specialty = specialty;
		this.address = address;
		this.phone = phone;
		this.image = image;
	}
	
	public static Restaurant getRestaurant() {
		return new Restaurant("name", "average", "like", "specialty",
				"address", "phone", 
				"http://b.hiphotos.baidu.com/baike/w%3D268/sign=783b52ce3bdbb6fd255be2203125aba6/b219ebc4b74543a9abc71cf91c178a82b9011422.jpg");
	}
	
	public static List<Restaurant> getRestaurantList() {
		List<Restaurant> list = new ArrayList<Restaurant>();
	/*	list.add(new Restaurant("湖锦酒楼", 
				"85", "0", "辣的跳", 
				"洪山区珞喻路1037号华中科技大学洪山校区88号光谷体育馆对面", 
				"027-87781777", "http://i3.dpfile.com/pc/ad5fc6a4accab28d39c6971d5809698d/35249213_m.jpg"));
		list.add(new Restaurant("光谷之窗旋转餐厅", 
				"179", "1", "甜点", 
				"洪山区珞喻路726号鲁巷购物广场中心华美达光谷大酒店内(近地铁2号线光谷站出口处)", 
				"027-87806888", "http://i2.dpfile.com/2008-12-28/1365952_m.jpg"));
		list.add(new Restaurant("娘惹裙厨(光谷店)", 
				"59", "2", "香兰叶包鸡", 
				"洪山区光谷世界城步行街D区5楼", 
				"027-87052583", "http://i2.dpfile.com/2011-06-25/8413465_m.jpg"));
		list.add(new Restaurant("牛太郎自助烧烤(光谷天地店)", 
				"56", "3", "牛肉", 
				"洪山区关山大道光谷天地1楼(近国美电器)", 
				"027-87731377", "http://i1.dpfile.com/pc/1e1ce691a00bcff3f6f57ef75e7cd052/39987765_m.jpg"));
		list.add(new Restaurant("秀玉红茶坊(光谷店)", 
				"51", "2", "水果沙拉", 
				"洪山区鲁巷光谷步行街D区5楼", 
				"027-87781777", "http://i1.dpfile.com/2010-03-07/3836292_m.jpg"));
				*/
		return list;
	}
}
