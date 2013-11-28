package com.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class MysqlConnectionManager {
    
    Connection conn;
	public ArrayList<Double> listXY = new ArrayList<Double>();

	public MysqlConnectionManager() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String url = "jdbc:mysql://ec2-50-19-213-178.compute-1.amazonaws.com:3306/HackeratiDB";
            conn = DriverManager.getConnection(url, "couillette", "followthesun");
            System.out.println("\nCONNECTED TO Mysql on the cloud");
            
            dropTable("stockexchange");
            createTable("stockexchange");
            dataGenerator(200, 100);
            DataLoading();  
            
        } catch (ClassNotFoundException ex) {
            System.err.println(ex.getMessage());
        } catch (IllegalAccessException ex) {
            System.err.println(ex.getMessage());
        } catch (InstantiationException ex) {
            System.err.println(ex.getMessage());
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
	protected void dropTable(String name){
		String query = "DROP TABLE "+name;
		
		try {
            PreparedStatement st = conn.prepareStatement(query);
            st.executeUpdate();
            System.out.println("Database droped successfully");
            
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
	}
	
	protected void createTable(String name){
		
		String query = "CREATE TABLE "+name
				+ "(dollar DOUBLE)" ;
		
		try {
            PreparedStatement st = conn.prepareStatement(query);
            st.executeUpdate();
            System.out.println("Created successfully");
            
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
	}
    
	

    protected void dataGenerator(int datanumber, double base) throws SQLException {
        System.out.print("\n[Performing INSERT] ... ");
        
        double value = base;
        int i=0;
        
        while(i<datanumber){
        	value = value * (1 + (Math.random() - 0.495) / 10.0);
        	String query = "INSERT INTO stockexchange(dollar) VALUES (?)";
             try {
                 PreparedStatement st = conn.prepareStatement(query);
                 st.setDouble(1, value);
                 st.executeUpdate();
                 
             } catch (SQLException ex) {
                 System.err.println(ex.getMessage());
             }
             i++;
        }
    }
    
    
    protected void DataLoading() throws SQLException {
    	System.out.println("\n[Performing SELECT] ... loading data ");
        String query = "SELECT dollar FROM StockExchange";

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                double x = rs.getDouble("dollar");
                listXY.add(x);
            }
            System.out.println("Data had been successfully load in ArrayList");

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    
    public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

    public ArrayList<Double> getListXY() {
		return listXY;
	}
    
	public void setListXY(ArrayList<Double> listXY) {
		this.listXY = listXY;
	}


}


