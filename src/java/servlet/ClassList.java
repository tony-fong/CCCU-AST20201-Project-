/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import function.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.*;
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
public class ClassList extends HttpServlet {

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
                ResultSet rs = null;

                try{
                    conn = DriverManager.getConnection(
                            "jdbc:mysql://localhost:3306/ast20201_individual?user=root&password=root&useSSL=false&serverTimezone=UTC"
                    );
                }catch(SQLException e){
                    System.out.println("connection error: " + e);
                }
                HttpSession session = request.getSession();
                try (PrintWriter out = response.getWriter()) {                   
                    if(user_role == "teacher"){
                        String teacher_id = session.getAttribute("user_id").toString();
                        String sql = "select * From  class_list where teacher_id = ? ORDER BY `class_list`.`start` DESC";
                        try{
                            PreparedStatement pre = conn.prepareStatement(sql);
                            pre.setString(1,teacher_id);
                            rs = pre.executeQuery();
                        }catch(SQLException E){}
                            out.println("<div>");
                            
                            out.println("<div class='pop_up' id='addclass'>");
                            out.println("<form>");
                            out.println("Class title<input type='text' id='title'/>");
                            out.println("Start date and time<input type='datetime-local' id='start'/>");
                            out.println("End date and tim<input type='datetime-local' id='end'/>");
                            out.println("<input type='button' id='submit' value='addclass' />");
                            out.println("</form>");
                            out.println("</div>");
                            out.println("</div>");     
                            out.println("<table class='table' id='classlist'>");
                                out.println("<tr>");
                                out.println("<td>");
                                out.println("course_id");
                                out.println("</td>");
                                out.println("<td>");
                                out.println("class id");
                                out.println("</td>");
                                out.println("<td>");
                                out.println("class title");
                                out.println("</td>");
                                out.println("<td>");
                                out.println("start");
                                out.println("</td>");
                                out.println("<td>");
                                out.println("link");
                                out.println("</td>"); 
                                out.println("</tr>");
                            while(rs.next()){
                                out.println("<tr class='classId' id=" + rs.getString("class_id") + ">");
                                out.println("<td>");
                                out.println(rs.getString("course_id"));
                                out.println("</td>");
                                out.println("<td>");
                                out.println(rs.getString("class_id"));
                                out.println("</td>");
                                out.println("<td>");
                                out.println(rs.getString("class_title"));
                                out.println("</td>");
                                out.println("<td>");
                                out.println(rs.getTimestamp("start"));
                                out.println("</td>");
                                out.println("<td>");
                                String link = "localhost:8080/individual/lession?id=" + rs.getString("link") + "";  
                                out.println(" <a id='link' href=''>" + link + "</a>");
                                out.println("</td>"); 
                                out.println("<td>");
                                out.println("<input type='button' class='checkAttendance' value='attendance'>");
                                out.println("</td>");
                                out.println("</tr>");
                            }
                            out.println("</table>");                          
                    }else if(user_role == "student"){
                       
                        String course_id = request.getParameter("course_id");
                        String student_id = session.getAttribute("user_id").toString();
                        String sql = "select class_id,course_id, teacher_name, class_title, start, end, link From  class_list NATURAL JOIN teacher ORDER BY `class_list`.`start` DESC";
                        try{
                            PreparedStatement pre = conn.prepareStatement(sql);
                            rs = pre.executeQuery();
                        }catch(SQLException E){out.println(E);}

                            out.println("student");
                            out.println("<table>");
                                out.println("<tr>");
                                out.println("<td>");
                                out.println("course_id");
                                out.println("</td>");
                                out.println("<td>");
                                out.println("teacher_name");
                                out.println("</td>");
                                out.println("<td>");
                                out.println("class_title");
                                out.println("</td>");
                                out.println("<td>");
                                out.println("start");
                                out.println("</td>");
                                out.println("<td>");
                                out.println("end");
                                out.println("</td>");
                                out.println("<td>");
                                out.println("link");
                                out.println("</td>");
                                out.println("<td>");
                                out.println("state");
                                out.println("</td>");
                                out.println("</tr>");
                            while(rs.next()){
                                out.println("<tr>");
                                out.println("<td>");
                                out.println(rs.getString("course_id"));
                                out.println("</td>");
                                out.println("<td>");
                                out.println(rs.getString("teacher_name"));
                                out.println("</td>");
                                out.println("<td>");
                                out.println(rs.getString("class_title"));
                                out.println("</td>");
                                out.println("<td>");
                                out.println(rs.getTimestamp("start"));
                                out.println("</td>");
                                out.println("<td>");
                                out.println(rs.getTimestamp("end"));
                                out.println("</td>");
                                out.println("<td>");
                                String link = "http://localhost:8080/individual/lesson?id=" + rs.getString("link") + "";  
                                out.println(" <a id='link' href='" + link + "'>" + link + "</a>");
                                out.println("</td>");
                                out.println("<td>");
                               String sql1 = "select state from attendance where class_id =? and student_id = ?";
                            ResultSet rs1 = null;
                            PreparedStatement pre1 = conn.prepareStatement(sql1);
                            pre1.setString(1, rs.getString("class_id"));
                            pre1.setString(2, student_id);
                             
                                rs1 = pre1.executeQuery();
                                if(rs1.next()){
                                out.println(rs1.getString("state"));
                                }
                                out.println("</td>");
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
            Logger.getLogger(ClassList.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ClassList.class.getName()).log(Level.SEVERE, null, ex);
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
