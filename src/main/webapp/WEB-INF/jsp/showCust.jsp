<%@ page language="java" 
	contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>


<div class="starter-template">
	<div class="row">
		<h1>customer Details</h1>
		<p>
			Below are the current list of orders in the system.
		</p>
		<div class="table-responsive">
			<table class="table table-striped">
				<thead>
					<tr>
						<th> ID</th>
						<th>1 name</th>
						<th>2 name</th>
						<th>email</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${cusList}" var="cust">
            			<tr>
      						<td>${cust.id}</td>
      						<td>${cust.firstName}</td>
      						<td>${cust.lastName}</td>
      						<td>${cust.email}</td>
 
       					</tr>
       				</c:forEach>
       			</tbody>
       		</table>
       	</div>
	</div>
</div>

</html>