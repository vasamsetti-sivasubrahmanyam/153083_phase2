package com.capgemini.paytm.repo;


import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.capgemini.paytm.beans.Customer;
import com.capgemini.paytm.beans.Wallet;
import com.capgemini.paytm.exception.InvalidInputException;

public class WalletRepoImpl implements WalletRepo {
	Connection con;
	public WalletRepoImpl(){
		
		String url="jdbc:mysql://localhost:3306/test";
		String uid="root";
		String pwd="corp123";
		try{
			this.con=DriverManager.getConnection(url,uid,pwd);
		}catch(SQLException e){
			e.printStackTrace();
		}
	} 
	private Map<String, Customer> data = new HashMap<>();

	public Map<String, Customer> getData() {
		return data;
	}

	public void setData(Map<String, Customer> data) {
		this.data = data;
	}

	Customer cust = new Customer();

	public WalletRepoImpl(Map<String, Customer> data) {
		super();
		this.data = data;
	}

	

	@Override
	public boolean save(Customer customer) {

		String query="insert into Customer(name,mobileNo,balance)values(?,?,?)";
		try {
			PreparedStatement pstmt=con.prepareStatement(query);
			pstmt.setString(1,customer.getName());
			pstmt.setString(2,customer.getMobileNo());
			Wallet w1=customer.getWallet();
			BigDecimal bal=w1.getBalance();
			pstmt.setBigDecimal(3, bal);
			pstmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String mobileNo = customer.getMobileNo();
		if (data.get(mobileNo) == null) {
			data.put(mobileNo, customer);
			return true;
		} else
			return false;
	}

	@Override
	public Customer findOne(String mobileNo) {
		String query="select * from Customer where mobileNo=?";
		Customer e=null;
		try
		{
			PreparedStatement pstmt=con.prepareStatement(query);
			pstmt.setString(1,mobileNo);
		ResultSet rs=pstmt.executeQuery();
		
		
		while(rs.next())
		{
			e=new Customer();
			e.setMobileNo(rs.getString(2));
			e.setName(rs.getString(1));
			Wallet w=new Wallet();
			w.setBalance(rs.getBigDecimal(3));
			e.setWallet(w);
			if (e == null)
				throw new InvalidInputException("Account not found!!");
			
		}
		}catch(SQLException e1){
		e1.printStackTrace();
		}
			return e;
	}

	@Override
	public Customer updateBal(Customer customer) {
		// TODO Auto-generated method stub

		String query="update Customer set balance=? where mobileNo=?";
		try {
			PreparedStatement pstmt=con.prepareStatement(query);
			Wallet w1=customer.getWallet();
			BigDecimal bal=w1.getBalance();
			pstmt.setBigDecimal(1, bal);
			pstmt.setString(2, customer.getMobileNo());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return customer;
	}

}
