package com.olympus.rest.util;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RetBool{
	private boolean value;
	
	public RetBool() {}
	
	public RetBool(boolean value) {
		this.value = value;
	}

	public boolean isValue() {
		return value;
	}

	public void setValue(boolean value) {
		this.value = value;
	}
}
