package org.airsql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class demo {

    public static void main(String[] args) throws Exception {
        //加载MySql驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://120.26.187.19:3307/demo", "root", "123456");
        //3.操作数据库，实现增删改查
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM demo");
        //如果有数据，rs.next()返回true
        while(rs.next()){
        }
    }


}
