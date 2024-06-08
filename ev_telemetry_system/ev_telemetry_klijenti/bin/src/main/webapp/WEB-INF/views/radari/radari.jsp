<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page
	import="java.util.List, java.util.Date, java.text.SimpleDateFormat,edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.podaci.Radar"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>REST MVC - Pregled radara</title>
<link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath}/resources/css/nwtis.css">
<style type="text/css">
table, th, td {
	border: 1px solid;
}

th {
	text-align: center;
	font-weight: bold;
}

.desno {
	text-align: right;
}
</style>
</head>
<body>
	<h1>REST MVC - Pregled radara</h1>
	<ul>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/radari/pocetak">Početna
				stranica radara</a></li>
	</ul>
	<br />
	<table>
		<tr>
			<th>R.br.
			<th>Adresa</th>
			<th>Mrezna vrata</th>
			<th>Gps sirina</th>
			<th>Gps brzina</th>
			<th>Maksimalna udaljenost</th>
		</tr>
		<%
		int i = 0;
		List<Radar> radari = (List<Radar>) request.getAttribute("radari");
		for (Radar r : radari) {
		  i++;
		%>
		<tr>
			<td class="desno"><%=i%></td>
			<td><%= r.getAdresaRadara() %></td>
			<td><%= r.getMreznaVrataRadara() %></td>
			<td><%= r.getGpsSirina() %></td>
			<td><%= r.getGpsDuzina() %></td>
			<td><%= r.getMaksUdaljenost() %></td>
		</tr>
		<%
		}
		%>
	</table>
</body>
</html>
