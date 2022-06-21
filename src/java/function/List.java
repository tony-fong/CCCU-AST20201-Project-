/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package function;

/**
 *
 * @author tonyf
 */

import java.sql.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
public class List {
    String title;
    String start;
    String end;
    String course_id;
    String teacher_id;
    String Class_id;
    public void setTitle(String title){
        this.title = title;
    }
    public void setStart(String start){
        this.start = start;
    }
    public void setEnd(String end){
        this.end = end;
    }
    public void setCourseId(String course_id){
        this.course_id = course_id;
    }
    public void setTeacherId(String teacher_id){
        this.teacher_id = teacher_id;
    }
    public String getClassId(){
        return Class_id;
    }
    public boolean addclass() throws SQLException{
        
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        }catch(Exception e){
            System.out.println("create class fall:  " + e);
        }
        Connection conn = null;
        ResultSet rs = null;

        try{
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/ast20201_individual?user=root&password=root&useSSL=false&serverTimezone=UTC"
            );
        }catch(SQLException e){
            System.out.println("connection error: " + e);
        }
        String link = Linkgenerator(title);
        
        String sql = "INSERT INTO class_list (`class_title`,`course_id`,`teacher_id`,`start`,`end`,`link`,`class_id`) VALUE (?,?,?,?,?,?,?)";
        PreparedStatement pre = conn.prepareStatement(sql);
        pre.setString(1,title);
        pre.setString(2,course_id);
        pre.setString(3,teacher_id);
        pre.setString(4,start);
        pre.setString(5,end);
        pre.setString(6,link);
        pre.setString(7,link);
        int res = pre.executeUpdate();
        if(res == 1){
            return true;
        }else{
            return false;
        }
      
    }
     public String Linkgenerator(String title){
        Random random = new Random();
        
        String hash = "";
        char[] arr = new char[title.length()];
        for(int i=0;i<title.length();i++){
            arr[i] = title.charAt(i);     
        }
        for(int i=0;i < arr.length;i++){
            int times = random.nextInt(1700 - 1000 + 1) + 1000;
            int ascii = arr[i] * times % 100;
            System.out.println(ascii);
            if(ascii >= 65 && ascii <= 90 || ascii >= 97 && ascii <= 122){
                hash = hash + (char)ascii;
            }   
        }
        while(hash.length() < 16){
            int num = random.nextInt(17000 - 10000 + 1) + 10000;
            int times = random.nextInt(1700 - 1000 + 1) + 1000;
            int ascii = num * times % 1000;
            System.out.println(ascii);
            if(ascii >= 65 && ascii <= 90 || ascii >= 97 && ascii <= 122){
                hash = hash + (char)ascii;
            }
        }
        Class_id = hash;
        return hash;
    }
     
    public void addrecord() throws SQLException{
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
        }catch(SQLException e){
            System.out.println("connection error: " + e);
        }
        
        
        try{
            String id = "select student_id from student";
            PreparedStatement pre1 = conn.prepareStatement(id);
            rs = pre1.executeQuery();
        }catch(SQLException e){
            System.out.println(e);
        }
        String class_id = getClassId();
        while(rs.next()){
            String sql = "INSERT INTO attendance (`student_id`,`class_id`,`state`) VALUES (?,?,?)";
            PreparedStatement pre2 = conn.prepareStatement(sql);
            pre2.setString(1,rs.getString("student_id"));
            pre2.setString(2,class_id);
            pre2.setString(3,"not attend");
            pre2.executeUpdate();
        }
    }
}
