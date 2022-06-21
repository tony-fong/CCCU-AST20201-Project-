package function;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author tonyf
 */
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
public class Role {
    public String getRole(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if(session.getAttribute("role") != null){
            String role = session.getAttribute("role").toString();
            if(role == "teacher"){
                return "teacher";
            }else if(role == "student"){
                return "student";
            }
        }else{
            return "unlogin";
        }
        return "unlogin";
    }
    public String unlogin(String user_role){
        if(user_role.equals("unlogin")){
            return "please login";
        }
        return "";
    }
}
