package com.olympus.rest.sap;

import java.util.ArrayList;

import javax.servlet.ServletContext;

import com.olympus.rest.util.Product;
import com.sap.mw.jco.JCO;

public class SAPProductModel {
	
	private static SAPProductModel instance = null;
	
	private JCO.Client mConnection = null;

	private SAPFunctionPreperator sfp = null;
	private JCOClientCreator jcc = null;
	
	private SAPProductModel(ServletContext context) {
		sfp = new SAPFunctionPreperator();
		jcc = JCOClientCreator.getInstanceOf(context);
	}
	
	public static SAPProductModel getInstanceOf(ServletContext context) {
		if(instance == null)
			instance = new SAPProductModel(context);
		return instance;
	}
	
	public void createConnection() throws Exception {
		mConnection = jcc.createJCOClient();
		mConnection.connect();
	}
	
	public void disconnect() {
		if(mConnection != null) {
			mConnection.disconnect();
			mConnection = null;
		}
	}
	
	public void insertProduct(Product insertProduct) throws Exception {
		this.createConnection();
		JCO.Function insert = sfp.getFunction("ZBAPI_OLYMPUS_INSERTPRODUCT", this.mConnection);
		JCO.Function commit = sfp.getCommitFunction(mConnection);
		ArrayList<Object> values = sfp.prepareArrayList(
				insertProduct.getName(), insertProduct.getPrice(), 
				insertProduct.getQuantity(), sfp.getFormatedRelDate(insertProduct.getReleaseDate()),
				insertProduct.getInterpret(), insertProduct.getType(),
				insertProduct.getGenre(), insertProduct.getDescription(),
				insertProduct.getImage());
		ArrayList<String> names = sfp.prepareStringArrayList("INSNAME", "INSPRICE", "INSQTY", "INSRELDATE",
				"INSINTERPRET", "INSTYPE", "INSGENRE", "INSDESCRIPTION", "INSIMG");
		sfp.setInsertParameter(insert, values, names);
		sfp.executeFunction(insert, mConnection);
		sfp.executeFunction(commit, mConnection);
		this.disconnect();
	}
	
	public ArrayList<Product> getAllProducts() throws Exception {
		ArrayList<Product> products = null;
		this.createConnection();
		
		JCO.Function get = sfp.getFunction("ZBAPI_OLYMPUS_GETPRODUCT", this.mConnection);
		JCO.Function commit = sfp.getCommitFunction(mConnection);
		sfp.executeFunction(get, this.mConnection);
		sfp.executeFunction(commit, this.mConnection);
		products = sfp.getProducts(get);
		
		this.disconnect();
		return products;
	}

	public ArrayList<Product> getProductsByName(String name) throws Exception {
		ArrayList<Product> products = null;
		
		this.createConnection();
		
		JCO.Function get = sfp.getFunction("ZBAPI_OLYMPUS_GETPRODUCTBYNAME", this.mConnection);
		JCO.Function commit = sfp.getCommitFunction(mConnection);
		
		ArrayList<Object> values = sfp.prepareArrayList(name);
		ArrayList<String> names = sfp.prepareStringArrayList("INSNAME");
		sfp.setInsertParameter(get, values, names);
		
		sfp.executeFunction(get, this.mConnection);
		sfp.executeFunction(commit, this.mConnection);
		products = sfp.getProducts(get);
		
		this.disconnect();
		return products;
	}
	
	public ArrayList<Product> getLatestProductsPerType(String type) throws Exception {
		ArrayList<Product> products = null;
		
		this.createConnection();
		
		JCO.Function get = sfp.getFunction("ZBAPI_OLYMPUS_GETPRODUCTLATEST", this.mConnection);
		JCO.Function commit = sfp.getCommitFunction(mConnection);
		
		ArrayList<Object> values = sfp.prepareArrayList(type);
		ArrayList<String> names = sfp.prepareStringArrayList("INSTTYPE");
		sfp.setInsertParameter(get, values, names);
		
		sfp.executeFunction(get, this.mConnection);
		sfp.executeFunction(commit, this.mConnection);
		products = sfp.getProducts(get);
		
		this.disconnect();
		return products;
	}
	
	public int updateQuantity(Product p) throws Exception {
		int qtyProducts = 0;
		
		this.createConnection();
		
		JCO.Function upd = sfp.getFunction("ZBAPI_OLYMPUS_UPDATEPCOUNT", this.mConnection);
		JCO.Function commit = sfp.getCommitFunction(mConnection);
		
		ArrayList<Object> values = sfp.prepareArrayList(p.getId(), p.getQuantity());
		ArrayList<String> names = sfp.prepareStringArrayList("PID", "MINUSQUANTITY");
		sfp.setInsertParameter(upd, values, names);
		
		sfp.executeFunction(upd, this.mConnection);
		sfp.executeFunction(commit, this.mConnection);
		
		qtyProducts = sfp.getRemainingQuantity(upd);
		this.disconnect();
		
		return qtyProducts;
	}
}
