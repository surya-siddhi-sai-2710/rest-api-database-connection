package com.example.demo.service;

import java.sql.Connection;
import java.sql.ResultSet;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.model.Cars;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import oracle.jdbc.OraclePreparedStatement;

@Component
public class CarService {
	
	@Autowired
	private DataSource dataSource;     	// DataSource is the configured object in yml file to connect the DB 
	
	@Autowired
	private ObjectMapper objectMapper;	//used to read and write json data
	
	// to add a new car
	public void addCars(Cars car) {		
		
		Connection conn = null;
		OraclePreparedStatement pstmt = null;
		
		try {
			// query to add into table
			String queryText = "insert into cars values(?,?)";
			// getting connection
			conn = dataSource.getConnection();
			pstmt = (OraclePreparedStatement) conn.prepareStatement(queryText);
			pstmt.setInt(1, car.getModelno());
			pstmt.setString(2, car.getCname());
			//execute query
			pstmt.execute();
		}
		
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayNode viewAllCars() {
		Connection conn = null;
		OraclePreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			
			String queryText = "select * from cars";
			conn = dataSource.getConnection();
            pstmt = (OraclePreparedStatement) conn.prepareStatement(queryText);
			
            rs = pstmt.executeQuery();
			ArrayNode getAllCarsObjectList = objectMapper.createArrayNode();
			while(rs.next())
			{
				ObjectNode getAllCarsObject = objectMapper.createObjectNode();
				getAllCarsObject.put("modelno", rs.getInt(1));
				getAllCarsObject.put("cname", rs.getString(2));
				getAllCarsObjectList.add(getAllCarsObject);
			}	
			return getAllCarsObjectList;
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ObjectNode viewCarByModelno(Long id) {
		Connection conn = null;
		OraclePreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			
			String queryText = "select * from cars where modelno = ?";
			conn = dataSource.getConnection();
            pstmt = (OraclePreparedStatement) conn.prepareStatement(queryText);
            pstmt.setLong(1, id);
			
			rs = pstmt.executeQuery();
			if(rs.next())
			{
				ObjectNode getCarByModelnoObject = objectMapper.createObjectNode();
				getCarByModelnoObject.put("modelno", rs.getInt(1));
				getCarByModelnoObject.put("cname", rs.getString(2));
				return getCarByModelnoObject;
			}	
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
