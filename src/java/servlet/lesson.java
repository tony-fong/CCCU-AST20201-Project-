/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import function.*;
import java.sql.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author tonyf
 */
public class lesson extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws java.sql.SQLException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        Role role = new Role();
        try (PrintWriter out = response.getWriter()) {
        String user_role = role.getRole(request);
        if(user_role.equals("unlogin")){
            out.println("<a href='login.html'>please login</a>");
            return;
        }
       
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        }catch(Exception e){
        out.println("create class fall:  " + e);
        }
            Connection conn = null;

        try{
        conn = DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/ast20201_individual?user=root&password=root&useSSL=false&serverTimezone=UTC"
        );
        }catch(SQLException e){
        out.println("connection error: " + e);
        }      
        out.println(user_role);
        if(user_role.equals("student")){
            String student_id = session.getAttribute("user_id").toString();
            String class_id = request.getParameter("id");
            String sql = "UPDATE `attendance` SET `state`=if(NOW() > (select end from class_list where class_id = ?) AND NOW() < (select start from class_list where class_id = ?) , 'LATE' , 'ATTEND'), `attendance_time`=NOW() WHERE student_id = ? AND class_id=?";
            try{
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setString(1,class_id);
            pre.setString(2,class_id);
            pre.setString(3, student_id);
            pre.setString(4,class_id);
            int i = pre.executeUpdate();
            if(i == 1){
                out.println("take attendance");
            }
            }catch(SQLException e){
                out.println(e);
            }
           
        }else{
            out.println("not student");
        }
    }
    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(lesson.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(lesson.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
