package com.olympus.rest.util;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Order implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<OrderItem> items;
	private Date orderDate;
	private int orderId;
	private int discount;

	public Order() {}
	
	public Order(int orderId, Date orderDate, int discount) {
		super();
		this.orderDate = orderDate;
		this.orderId = orderId;
		this.discount = discount;
	}
	
	public Order(ArrayList<OrderItem> items, Date orderDate, int orderId, int discount) {
		super();
		this.items = items;
		this.orderDate = orderDate;
		this.orderId = orderId;
		this.discount = discount;
	}
	
	public String getFormattedPrice() {
		DecimalFormat df = new DecimalFormat("0.00");
		float fullPrice = this.getFullPrice();
		fullPrice = fullPrice * this.getDiscountMultiplier();
		return df.format(fullPrice);
	}
	
	private float getDiscountMultiplier() {
		return (float)((100.00f-this.discount)/100.00f);
	}

private float getFullPrice() {
		float priceSum = 0;
		for(OrderItem oi : this.items)
			priceSum += oi.getFullPrice();
		return priceSum;
	}

//*************************************************************************************
	public ArrayList<OrderItem> getItems() {
		return items;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setItems(ArrayList<OrderItem> items) {
		this.items = items;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	
	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}
	
}
