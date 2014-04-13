package com.olympus.rest.sap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.olympus.rest.util.Product;
import com.sap.mw.jco.IFunctionTemplate;
import com.sap.mw.jco.JCO;
import com.sap.mw.jco.JCO.Function;

public class SAPFunctionPreperator {

	private static final String format = "yyyyMMdd";
	
	private SimpleDateFormat sdf;
	
	public SAPFunctionPreperator() {
		this.sdf = new SimpleDateFormat(format);
	}
	
	public Function getCommitFunction(JCO.Client con) throws Exception {
		return this.getFunction("BAPI_TRANSACTION_COMMIT", con);
	}
	
	public JCO.Function getFunction(String nameOfFunction,
			JCO.Client con) throws Exception {
		JCO.Repository repo = new JCO.Repository("MyRepository", con);
		IFunctionTemplate ft = repo.getFunctionTemplate(nameOfFunction.toUpperCase());
		if (ft == null)
			throw new Exception("function not found in SAP Repository: " + nameOfFunction);
		return ft.getFunction();
	}
	
	public void setInsertParameter(JCO.Function fun, ArrayList<Object> values,
			ArrayList<String> names) throws SAPException {
		
		if(values.size() != names.size())
			throw new SAPException("Count of Function-Parameters and Function-Parameter-Names"
					+ " are not equal");
		
		for(int i = 0; i<values.size(); i++) {
			fun.getImportParameterList().setValue(values.get(i), names.get(i));
		}
	}
	
	public void executeFunction (JCO.Function _sapFunction, JCO.Client con) throws Exception {
		con.execute(_sapFunction);
	}
	
	/*public static void setInsertPupilParameter(Function insert, String newPName,
			String newPMotivation, String newPDate, String newDNo) {
		insert.getImportParameterList().setValue(newDNo, "IDNO");
		insert.getImportParameterList().setValue(newPName, "IPNAME");
		insert.getImportParameterList().setValue(newPMotivation, "IMOTIV");
		insert.getImportParameterList().setValue(newPDate, "IPDATE");
	}*/
	
	public ArrayList<Object> prepareArrayList(Object... vals) {
		ArrayList<Object> values = new ArrayList<>();
		for(Object o : vals)
			values.add(o);
		return values;
	}
	
	public ArrayList<String> prepareStringArrayList(String... vals) {
		ArrayList<String> values = new ArrayList<>();
		for(String o : vals)
			values.add(o);
		return values;
	}

	public ArrayList<Product> getProducts(Function get) throws ParseException {
		ArrayList<Product> products = new ArrayList<Product>();
		JCO.Table productsTable = get.getTableParameterList().getTable("PRODUCTS");
		for(int i = 0; i<productsTable.getNumRows(); i++, productsTable.setRow(i)){
			int id = productsTable.getInt("ID");
			String name = productsTable.getString("NAME");
			float price = productsTable.getFloat("PRICE");
			int qty = productsTable.getInt("QTY");
			Date reldate = this.getParsedRelDate(productsTable.getString("RELDATE"));
			String interpret = productsTable.getString("INTERPRET");
			String type = productsTable.getString("TYPE");
			String genre = productsTable.getString("GENRE");
			String description = productsTable.getString("DESCRIPTION");
			String image = productsTable.getString("IMG");
			products.add(new Product(id, price, name, qty, reldate, interpret, genre, description, image, type));
		}
		return products;
	}

	public int getRemainingQuantity(Function upd) {
		int remaining = upd.getExportParameterList().getInt("EXPORTQTY");
		return remaining;
	}
	
	public String getFormatedRelDate(Date releaseDate) {
		return sdf.format(releaseDate);
	}
	
	public Date getParsedRelDate(String releaseDate) throws ParseException {
		return sdf.parse(releaseDate);
	}
}
