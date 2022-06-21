<%-- 
    Document   : takeAttendance
    Created on : 2020年5月23日, 下午10:37:14
    Author     : tonyf
--%>
<%@ page import = "java.io.*" %>
<%@ page import="java.sql.*"%>
<%@ page import="java.util.*"%>
<%@page session="true"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    if(request.getParameter("all_student") == null){
        out.println("hi");
        String student_id = request.getParameter("student_id");
        String class_id = request.getParameter("class_id");
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        }catch(Exception e){
            System.out.println("create class fall:  " + e);
        }
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try{
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/ast20201_individual?user=root&password=root&useSSL=false&serverTimezone=UTC"
            );
            stmt = conn.createStatement();
        }catch(SQLException e){
            System.out.println("connection error: " + e);
        }

        String sql = "UPDATE `attendance` SET `state`='absence'  WHERE student_id = '" + student_id + "' AND class_id= '" + class_id + "'";
        int i =stmt.executeUpdate(sql);
        if(i ==1){
            out.println("update success");
        }
    }else{
        out.println(request.getParameter("all_student"));
        String class_id = request.getParameter("class_id");
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        }catch(Exception e){
            System.out.println("create class fall:  " + e);
        }
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        try{
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/ast20201_individual?user=root&password=root&useSSL=false&serverTimezone=UTC"
            );
            st = conn.createStatement();
        }catch(SQLException e){
            System.out.println("connection error: " + e);
        }

        String sql = "UPDATE `attendance` SET `state`=IF( `state` = 'ATTEND','ATTEND' ,'absence') WHERE class_id= '" + class_id + "'";
        out.println(sql);
        int i =st.executeUpdate(sql);


        
        if(i ==1){
            out.println("update success");
        }else{
                out.println("update fail");
        }
    }
%>
