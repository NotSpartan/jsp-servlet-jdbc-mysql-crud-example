<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>Email Adresar</title>
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
	integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
	crossorigin="anonymous">
</head>
<body>

	<header>
		<nav class="navbar navbar-expand-md navbar-dark"
			style="background-color: tomato">
			<div>
				<a href="https://www.tutorialspoint.com/spring/index.htm" 
					class="navbar-brand"> Email Adresar </a>
			</div>

			<ul class="navbar-nav">
				<li><a href="<%=request.getContextPath()%>/list"
					class="nav-link">Korisnici</a></li>
			</ul>
		</nav>
	</header>
	<br>

	<div class="row">
		<!-- <div class="alert alert-success" *ngIf='message'>{{message}}</div> -->

		<div class="container">
			<h3 class="text-center">Lista Korisnika</h3>
			<hr>
			<div class="container text-left">

				<a href="<%=request.getContextPath()%>/new" class="btn btn-success">Dodaj Korisnika</a>
			</div>
			<br>
			<table class="table table-bordered">
				<thead>
					<tr>
						<th>ID</th>
						<th>Ime</th>
						<th>Email</th>
						<th>Država</th>
						<th>Akcije</th>
					</tr>
				</thead>
				<tbody>
					<!--   for (Todo todo: todos) {  -->
					<c:forEach var="user" items="${listUser}">

						<tr>
							<td><c:out value="${user.id}" /></td>
							<td><c:out value="${user.name}" /></td>
							<td><c:out value="${user.email}" /></td>
							<td><c:out value="${user.country}" /></td>
							<td><a href="edit?id=<c:out value='${user.id}' />">Promjeni</a>
								&nbsp;&nbsp;&nbsp;&nbsp; <a
								href="delete?id=<c:out value='${user.id}' />">Izbriši</a></td>
						</tr>
					</c:forEach>
					<!-- } -->
				</tbody>

			</table>
		</div>
	</div>
</body>
</html>