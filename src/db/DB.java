package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DB {
    //ConnectionFactory
    private static Connection conn = null;

    public static Connection getConnection(){
        if(conn == null){
            try {
                Properties props = loadProperties();
                String url = props.getProperty("URL");
                conn = DriverManager.getConnection(url, props);
            }catch (SQLException ex){
                throw new DbException(ex.getMessage());
            }
        }

        return conn;
    }
    public static void closeConnection(){
        if(conn != null){
            try {
                conn.close();
            }catch (SQLException ex){
                throw new DbException(ex.getMessage());
            }
        }
    }
    public static void closeConnection(Statement stmt){
        if(stmt != null){
            try{
                stmt.close();
            }catch (SQLException ex){
                throw new DbException(ex.getMessage());
            }
        }
    }
    public static void closeConnection(ResultSet rs){
        if(rs != null){
            try{
                rs.close();
            }catch (SQLException ex){
                throw new DbException(ex.getMessage());
            }
        }
    }
    public static void closeConnection(Statement stmt, ResultSet rs){
        closeConnection(stmt);
        if(rs != null){
            try{
                rs.close();
            }catch (SQLException ex){
                throw new DbException(ex.getMessage());
            }
        }
    }
    public static void closeConnection(Connection conn, Statement stmt, ResultSet rs){
        closeConnection(stmt, rs);
        if(conn != null){
            try{
                conn.close();
            }catch (SQLException ex){
                throw new DbException(ex.getMessage());
            }
        }
    }

    private static Properties loadProperties(){
        try(FileInputStream fs = new FileInputStream("src/db.properties")){
            Properties props = new Properties();
            props.load(fs);
            return props;
        }catch (IOException ex){
            throw new DbException(ex.getMessage());
        }
    }



}
