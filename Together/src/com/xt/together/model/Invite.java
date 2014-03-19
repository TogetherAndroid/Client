package com.xt.together.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Invite implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8828074149463173688L;
	private String name;
	private String address;
	private String date;
	private String invite;
	private String invited;
	private String phone;
	private String image;
	
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
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getInvite() {
		return invite;
	}
	public void setInvite(String invite) {
		this.invite = invite;
	}
	public String getInvited() {
		return invited;
	}
	public void setInvited(String invited) {
		this.invited = invited;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	
	public Invite(String name, String address, String date, 
			String invite, String invited, String phone, String image) {
		this.name = name;
		this.address = address;
		this.date = date;
		this.invite = invite;
		this.invited = invited;
		this.phone = phone;
		this.image = image;
	}
	
	public static List<Invite> getList() {
		List<Invite> list = new ArrayList<Invite>();
		Invite invite = new Invite("name", "address", "date", "invite", "invited", "phone", "http://b.hiphotos.baidu.com/baike/w%3D268/sign=783b52ce3bdbb6fd255be2203125aba6/b219ebc4b74543a9abc71cf91c178a82b9011422.jpg");
		list.add(invite);
		return list;
	}
}
