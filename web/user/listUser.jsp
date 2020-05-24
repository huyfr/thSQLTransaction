<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 16/05/2020
  Time: 17:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <title>User managerment application</title>
</head>
<body>
<center>
<h1>User Managerment Application</h1>
<h2><a href="/user?action=create">Add new User</a></h2>
<h2><a href="/user?action=search">Search an user</a></h2>
</center>
<div align="center">
    <table border="1" cellpadding="5">
        <caption><h2>List of User</h2></caption>
        <tr>
            <td>ID</td>
            <td>Name</td>
            <td>Email</td>
            <td>Country</td>
            <td>Action</td>
        </tr>
        <c:forEach items="${listUser}" var="user">
            <tr>
                <td>${user.getId()}</td>
                <td>${user.getName()}</td>
                <td>${user.getEmail()}</td>
                <td>${user.getCountry()}</td>
                <td>
                    <a href="/user?action=edit&id=${user.getId()}">Edit</a>
                    <a href="/user?action=delete&id=${user.getId()}">Delete</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>
