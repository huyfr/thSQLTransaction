<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 19/05/2020
  Time: 09:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Search User</title>
</head>
<body>
<form method="post">
    <label>Tim Kiem Theo Country: </label>
    <input type="text" name="search"/>
    <input type="submit"/>
</form>

<table>
    <tr>
        <td>ID</td>
        <td>Name</td>
        <td>Email</td>
        <td>Country</td>
    </tr>
    <c:forEach items="${userList}" var="user">
        <tr>
            <td>${user.getId()}</td>
            <td>${user.getName()}</td>
            <td>${user.getEmail()}</td>
            <td>${user.getCountry()}</td>
            <td><a href="/user?action=edit&id=${user.getId()}">Edit</a></td>
            <td><a href="/user?action=delete&id=${user.getId()}">Delete</a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
