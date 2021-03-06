package com.olympus.rest.util;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class User implements Serializable{
	
private static final long serialVersionUID = 1L;
	
	private int id;
	private String name;
	private String address;
	private String picture;
	private String birthdate;
	private String email;
	private String password;
	private int discount;
	
	public User(){}
	
	public User(int id, String name, String address, String picture, String birthdate,
			String email, String password, int discount) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.picture = picture;
		this.birthdate = birthdate;
		this.email = email;
		this.password = password;
		this.discount = discount;
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

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public float getDiscountValue() {
		return (float)((100.00f-(float)this.discount)/100.00f);
	}

	@Override
	public String toString() {
		return "User [id="+id+", name=" + name + ", address=" + address + ", picture="
				+ picture + ", birthdate=" + birthdate + ", email=" + email
				+ ", password=" + password + ", discount=" + discount + "]";
	}
}
