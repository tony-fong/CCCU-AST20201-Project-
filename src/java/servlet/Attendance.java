/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import function.Role;
import java.sql.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author tonyf
 */
public class Attendance extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
            response.setContentType("text/html;charset=UTF-8");
                Role role = new Role();
                String user_role = role.getRole(request);
                if(user_role.equals("unlogin")){
                    try (PrintWriter out = response.getWriter()) {
                        out.println("<a href='login.html'>please login</a>");
                    }
                    return;
                }
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
                try (PrintWriter out = response.getWriter()) {
                    String class_id = request.getParameter("class_id");
                    if(user_role == "teacher"){
                        String sql = "SELECT class_id, class_title, attendance.student_id, student_name, attendance.state FROM attendance NATURAL JOIN class_list NATURAL JOIN student WHERE class_id = ?";
                        PreparedStatement pre = conn.prepareStatement(sql);
                        pre.setString(1,class_id);
                        //out.println(pre.toString());
                        try{
                            rs = pre.executeQuery(); 
                        }catch(SQLException E){}
                        out.println("<table class='table' id='attendance'>");
                        out.println("<tr>");
                        out.println("<td>");
                        out.println("class_id");
                        out.println("</td>");
                        out.println("<td>");
                        out.println("class title");
                        out.println("</td>"); 
                        out.println("<td>");
                        out.println("student name");
                        out.println("</td>");
                        out.println("<td>");
                        out.println("student id");
                        out.println("</td>");
                        out.println("<td>");
                        out.println("state");
                        out.println("</td>");
                        out.println("<td>");
                        out.println("<input type='button' class='all_absence' id='" + class_id + "' value='student absence'>");
                        out.println("</td>");
                        out.println("</tr>");
                        while(rs.next()){
                            out.println("<tr class='row' id='" + rs.getInt("student_id") + "'>");
                            out.println("<td class ='class_id' id='" + rs.getString("class_id") + "'>");
                            out.println(rs.getString("class_id"));
                            out.println("</td>");
                            out.println("<td>");
                            out.println(rs.getString("class_title"));
                            out.println("</td>"); 
                            out.println("<td>");
                            out.println(rs.getString("student_name"));
                            out.println("</td>");
                            out.println("<td class='student_id' id='" + rs.getInt("student_id") + "'>");
                            out.println(rs.getInt("student_id"));
                            out.println("</td>");
                            out.println("<td>");
                            if(rs.getString("state") == null){
                             out.println(rs.getString(""));
                            }else{
                            out.println(rs.getString("state"));}
                            out.println("</td>");
                            if(!rs.getString("state").equals("ATTEND") && !rs.getString("state").equals("absence")){
                                out.println("<td>");
                                out.println("<input type='button' class='absence' value='absence' />");
                                out.println("</td>");
                            }
                            out.println("</tr>");
                            
                        }
                        out.println("</table>");
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
            Logger.getLogger(Attendance.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Attendance.class.getName()).log(Level.SEVERE, null, ex);
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

