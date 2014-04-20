package com.olympus.rest.oracle;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.olympus.rest.util.CartItem;
import com.olympus.rest.util.Order;
import com.olympus.rest.util.OrderItem;
import com.olympus.rest.util.User;

public class Database implements Serializable
{
	private static final long serialVersionUID = -2948438178157372271L;
	private DataSource ds=null;
	private java.sql.Connection conn=null;


	public Database()
	{
		Context ctx;
		try {
			ctx = new InitialContext();
			Context envContext = (Context) ctx.lookup("java:/comp/env");
			ds = (DataSource) envContext.lookup("jdbc/resStore");
		} catch (NamingException e) {
			e.printStackTrace();
		}

	}

	private void Connect() throws SQLException
	{
		conn=ds.getConnection();
	}

	private void CloseConnection() throws SQLException
	{
		conn.close();
	}

	public User getUser(String name)
	{
		User u=null;
		try
		{
			this.Connect();
			PreparedStatement stmt=null;
			String select ="select adress,url,to_Char(birthdate, 'YYYY/MM/DD') birthdate,email,passwort,discount, u_id from users where username=?";
			stmt = conn.prepareStatement(select);
			
			stmt.setString(1, name);
			
			ResultSet rs=stmt.executeQuery();
			
			
			if(rs.next())
			{
				int id = Integer.parseInt(rs.getString("u_id"));
				String address = rs.getString("adress");
				String url = rs.getString("url");
				String birthdate = rs.getString("birthdate");
				String email = rs.getString("email");
				String password = rs.getString("passwort");
				int discount = rs.getInt("discount");
				System.out.println("------> discount: " + discount);
				System.out.println("String discount: " + rs.getString("discount"));
				u= new User(id,name,address, url, birthdate, email, password, discount);
			}
			
			System.out.println("-------->USER FROM DATABASE: " + u);
			this.CloseConnection();
		}catch(Exception e)
		{
			e.printStackTrace();
		}

		return u;
	}

	public boolean isPasswordCorrect(String name, String passwd) throws SQLException {
		this.Connect();
		String selectCount = "SELECT count(*) as cnt FROM users WHERE username=? AND passwort=? and deleted = 'false'";
		PreparedStatement stmt = conn.prepareStatement(selectCount,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
		stmt.setString(1, name);
		stmt.setString(2, passwd);
		ResultSet rs=stmt.executeQuery();
		rs.next();
		boolean ret=false;
		if(rs.getInt("cnt")>0)
			ret=true;
		this.CloseConnection();
		return ret;
	}

	public void createUser(String username, String adress, String url,String birthdate,String email,String pw ) throws SQLException
	{
		this.Connect();
		PreparedStatement stmt=conn.prepareStatement("insert into users values(seq_users.NEXTVAL,?,?,?,to_date(?,'DD.MM.YYYY'),?,?,?,?)");

		stmt.setString(1, username);
		stmt.setString(2, adress);
		stmt.setString(3, url);
		stmt.setString(4, birthdate);
		stmt.setString(5, email);
		stmt.setString(6, pw);
		stmt.setInt(7, 	0);
		stmt.setString(8, "false");


		stmt.executeUpdate();
		
		
		this.CloseConnection();
	}

	public void updateUser(User toUpdate) throws SQLException {
		this.Connect();
			String upd = "update users set username=?, adress=?, birthdate=to_date(?, 'YYYY/MM/DD'), "
					+ "email=?, passwort=? where u_id = ?";
			PreparedStatement prs = this.conn.prepareStatement(upd);
			prs.setString(1, toUpdate.getName());
			prs.setString(2, toUpdate.getAddress());
			prs.setString(3, toUpdate.getBirthdate());
			prs.setString(4, toUpdate.getEmail());
			prs.setString(5, toUpdate.getPassword());
			prs.setInt(6, toUpdate.getId());
			
			if(prs.executeUpdate() <= 0)
				throw new SQLException("User was not updated");
		this.CloseConnection();
	}
	
	public void insertCartAndCartItems(ArrayList<CartItem> items, User insertUser) throws SQLException {
		this.Connect();
		int newOrderId = this.getLatestOrderId();
		if(newOrderId < 0)
			throw new SQLException("OrderId error occured");
		this.insertOrder(newOrderId, insertUser.getId());
		this.insertOrderItems(newOrderId, items);
		this.CloseConnection();
	}
	
	public void insertCartAndCartItems(ArrayList<CartItem> items, User insertUser, int discount) throws SQLException {
		this.Connect();
		int newOrderId = this.getLatestOrderId();
		if(newOrderId < 0)
			throw new SQLException("OrderId error occured");
		this.insertOrder(newOrderId, insertUser.getId(), discount);
		this.insertOrderItems(newOrderId, items);
		this.resetDiscount(insertUser);
		this.CloseConnection();
	}
	
	private void resetDiscount(User user) throws SQLException {
		System.out.println("-------------------------------> in resetDiscount: " + user.getId());
		String update = "update users set discount = 0 where u_id = ?";
		PreparedStatement prs = this.conn.prepareStatement(update);
		prs.setInt(1, user.getId());
		if(prs.executeUpdate() <= 0)
			throw new SQLException("We could not apply your discount");
	}

	private void insertOrderItems(int newOrderId, ArrayList<CartItem> items) throws SQLException {
		String insert = "insert into orderitem values(?,?,?,?,?)";
		PreparedStatement prs = this.conn.prepareStatement(insert);
		for(CartItem ci : items) {
			prs.setInt(1,ci.getItem().getId());
			prs.setInt(2, newOrderId);
			prs.setString(3, ci.getItem().getName());
			prs.setFloat(4, ci.getItem().getPrice());
			prs.setInt(5, ci.getQuantety());
			if(prs.executeUpdate() <= 0)
				throw new SQLException("Could not insert item");
		}
	}

	private void insertOrder(int o_id, int u_id) throws SQLException {
		String insert = "insert into orders(o_id,datum,user_id) values(?,to_date(?,'DDMMYYYY'),?)";
		String date = this.getCurrentDateAsString();
		PreparedStatement prs = this.conn.prepareStatement(insert);
		prs.setInt(1, o_id);
		prs.setString(2, date);
		prs.setInt(3, u_id);
		int inserted = prs.executeUpdate();
		if(inserted <= 0)
			throw new SQLException("Ordern could not be created");
	}
	
	private void insertOrder(int o_id, int u_id, int discount) throws SQLException {
		String insert = "insert into orders values(?,to_date(?,'DDMMYYYY'),?,?)";
		String date = this.getCurrentDateAsString();
		PreparedStatement prs = this.conn.prepareStatement(insert);
		prs.setInt(1, o_id);
		prs.setString(2, date);
		prs.setInt(3, u_id);
		prs.setInt(4, discount);
		int inserted = prs.executeUpdate();
		if(inserted <= 0)
			throw new SQLException("Ordern could not be created");
	}
	
	private String getCurrentDateAsString() {
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
		return sdf.format(new Date());
	}

	private int getLatestOrderId() throws SQLException {
		int nextId = -99;
		String selId = "select seq_orders.nextval from dual";
		PreparedStatement prs = this.conn.prepareStatement(selId);
		ResultSet rs = prs.executeQuery();
		if(rs.next())
			nextId = rs.getInt(1);
		return nextId;
	}

	public void deleteUser(User toDelete) throws SQLException {
		this.Connect();
		String delete = "update users set deleted = 'true' where u_id = ?";
		PreparedStatement prs = this.conn.prepareStatement(delete);
		prs.setInt(1, toDelete.getId());
		int deleted = prs.executeUpdate();
		if(deleted <= 0)
			throw new SQLException("User could not be deleted");
		this.CloseConnection();
	}
	
	public ArrayList<Order> getOrders(User user) throws SQLException {
		ArrayList<Order> orders = new ArrayList<Order>();
		this.Connect();
		String select = "select * from orders where user_id = ?";
		PreparedStatement prs = this.conn.prepareStatement(select);
		prs.setInt(1, user.getId());
		ResultSet rs = prs.executeQuery();
		while(rs.next()) {
			int oId = rs.getInt(1);
			Date date = rs.getDate(2);
			int discount = rs.getInt(4);
			orders.add(new Order(oId, date, discount));
		}
		for(Order o : orders) {
			o.setItems(this.getOrderItems(o));
		}
		
		this.CloseConnection();
		return orders;
	}

	private ArrayList<OrderItem> getOrderItems(Order order) throws SQLException {
		ArrayList<OrderItem> items = new ArrayList<OrderItem>();
		String sel = "select i_id, i_name, price, amount from orderitem where o_id = ?";
		PreparedStatement prs = this.conn.prepareStatement(sel);
		prs.setInt(1, order.getOrderId());
		ResultSet rs = prs.executeQuery();
		while(rs.next()) {
			int iId = rs.getInt(1);
			String iName = rs.getString(2);
			float price = rs.getFloat(3);
			int amount = rs.getInt(4);
			items.add(new OrderItem(iId, iName, price, amount));
		}
		return items;
	}

}
