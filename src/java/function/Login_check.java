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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
public class Login_check {
    String username;
    String password;
    public void setUsername(String username){
        this.username = username;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public boolean userCheck(HttpServletRequest request) throws SQLException{
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
        String sql1 = "Select * from student where student_name = ? AND password = ?";
        PreparedStatement pre1 = conn.prepareStatement(sql1);
        pre1.setString(1,username);
        pre1.setString(2,password);
        rs = pre1.executeQuery();
        if(rs.next()){
            HttpSession session = request.getSession();
            session.setAttribute("role", "student");
            session.setAttribute("user_id", rs.getInt("student_id"));
            session.setAttribute("user_name", rs.getString("student_name"));
            return true;
        }
        rs = null;
        String sql2 = "SELECT * FROM `teacher` NATURAL JOIN course  where teacher_name = ? AND password = ?";
        PreparedStatement pre2 = conn.prepareStatement(sql2);
        pre2.setString(1,username);
        pre2.setString(2,password);
        rs = pre2.executeQuery();
        if(rs.next()){
            HttpSession session = request.getSession();
            session.setAttribute("role", "teacher");
            session.setAttribute("user_id", rs.getInt("teacher_id"));
            session.setAttribute("user_name", rs.getString("teacher_name"));
            session.setAttribute("course_id", rs.getString("course_id"));
            return true;
        }
        return false;
    }
}
