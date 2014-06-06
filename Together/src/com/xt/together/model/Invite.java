package com.xt.together.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Invite implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8828074149463173688L;
	private String id;
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Invite(String id, String name, String address, String date, 
			String invite, String invited, String phone, String image) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.date = date;
		this.invite = invite;
		this.invited = invited;
		this.phone = phone;
		this.image = image;
	}
}
