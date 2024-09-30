package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GetDBConnection {
    public static Connection connectDB(String DBname,String id,String password){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            // TODO: handle exception
        }
        Connection con = null;
        String url = "jdbc:mysql://localhost:3306/" + DBname + "?" +
                "useSSL=false&serverTimezone=CST&characterEncoding=utf-8&useServerPrepStmts=true";

        try{
            con = DriverManager.getConnection(url,id, password);
            System.out.println("连接成功");
        }
        catch(SQLException e){
            System.out.println("连接失败");
            System.out.println(e);
        }
        return con;
    }
}
