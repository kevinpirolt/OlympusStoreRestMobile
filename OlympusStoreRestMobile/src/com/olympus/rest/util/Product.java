package com.olympus.rest.util;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Product {

	private int id;
	private String name;
	private float price;
	private int quantity;
	private Date releaseDate;
	private String interpret;
	private String genre;
	private String type;
	private String description;
	private String image;

	public Product() {
	}
	
	public Product(int id, int quantity) {
		super();
		this.id = id;
		this.price = 0f;
		this.name = "";
		this.quantity = quantity;
		this.releaseDate = new Date();
		this.interpret = "";
		this.genre = "";
		this.description = "";
		this.image = "";
		this.type = "";
	}

	public Product(int id, float price,String name, int quantity, Date releaseDate,
			String interpret, String genre, String description, String image, String type) {
		super();
		this.id = id;
		this.price = price;
		this.name = name;
		this.quantity = quantity;
		this.releaseDate = releaseDate;
		this.interpret = interpret;
		this.genre = genre;
		this.description = description;
		this.image = image;
		this.type = type;
	}
	
	public Product(float price,String name, int quantity, Date releaseDate,
			String interpret, String genre, String description, String image, String type) {
		super();
		this.price = price;
		this.name = name;
		this.quantity = quantity;
		this.releaseDate = releaseDate;
		this.interpret = interpret;
		this.genre = genre;
		this.description = description;
		this.image = image;
		this.type = type;
	}

	// -----------------------------------------------------------------------------------------------------
	public int getId() {
		return id;
	}

	public float getPrice() {
		return price;
	}

	public int getQuantity() {
		return quantity;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public String getInterpret() {
		return interpret;
	}

	public String getGenre() {
		return genre;
	}

	public String getDescription() {
		return description;
	}

	public String getImage() {
		return image;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public void setInterpret(String interpret) {
		this.interpret = interpret;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
