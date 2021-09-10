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
		<h1>Selected Orders</h1>
		<p>
			Below are the Your Selected Orders
		</p>
		<div class="table-responsive">
			<table class="table table-striped">
				<thead>
					<tr>
						<th>Order ID</th>
						<th>Order Number</th>
						<th>Order Name</th>
						<th>Order Type</th>
						<th>Order Prize</th>
						<th>Order Quantity</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${showOrders}" var="showOrder">
            			<tr>
      						<td>${showOrder.id}</td>
      						<td>${showOrder.catalogItem.itemNumber}</td>
      						<td> ${showOrder.catalogItem.itemName}</td>
      						<td>${showOrder.catalogItem.itemType}</td>
      						<td>${showOrder.price}</td>
      						<td>${showOrder.quantity}</td>
       					</tr>
       				</c:forEach>
       			</tbody>
       		</table>
       	</div>
	</div>
</div>
 
 
 
 
 
 
 
 
</body>
</html>