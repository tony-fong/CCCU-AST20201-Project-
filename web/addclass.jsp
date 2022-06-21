<%-- 
    Document   : addclass
    Created on : 2020年5月23日, 下午02:07:35
    Author     : tonyf
--%>
<jsp:useBean id="list" class="function.List" scope="session"/>
<%@ page import = "java.io.*" %>
<%@ page import="java.sql.*"%>
<%@ page import="java.util.*"%>
<%@page session="true"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
            String title = request.getParameter("title");
            String start = request.getParameter("start");
            String end = request.getParameter("end");
            String course_id = session.getAttribute("course_id").toString();
            String teacher_id = session.getAttribute("user_id").toString();
            out.println(start);
            out.println(title);
            out.println(course_id);
            out.println(teacher_id);
            list.setTitle(title);
            list.setStart(start);
            list.setEnd(end);
            list.setCourseId(course_id);
            list.setTeacherId(teacher_id);
            if(list.addclass()){
                out.println("add success");
            }
            list.addrecord();
%>
