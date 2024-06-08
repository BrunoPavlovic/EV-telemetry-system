<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page
	import="java.util.List, java.util.Date, java.text.SimpleDateFormat,edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.podaci.Vozilo"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>REST MVC - Pregled pracenih voznji</title>
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
	<h1>REST MVC - Pregled pracenih voznji</h1>
	<ul>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/vozila/pocetak">Početna
				stranica pracenih voznji</a></li>
	</ul>
	<br />
	<table>
		<tr>
			<th>R.br.</th>
			<th>Id</th>
			<th>Broj</th>
			<th>Vrijeme</th>
			<th>Brzina</th>
			<th>Snaga</th>
			<th>Struja</th>
			<th>Visina</th>
			<th>GpsBrzina</th>
			<th>PreostaloKm</th>
			<th>UkupnoKm</th>
			<th>GpsSirina</th>
			<th>GpsDuzina</th>
		</tr>
		<%
		int i = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
		List<Vozilo> praceneVoznje = (List<Vozilo>) request.getAttribute("praceneVoznje");
		for (Vozilo v : praceneVoznje) {
		  i++;
		  Date vrijeme = new Date(v.getVrijeme() * 1000);
		%>
		<tr>
			<td class="desno"><%=i%></td>
			<td><%=v.getId()%></td>
			<td><%=v.getBroj()%></td>
			<td><%=sdf.format(vrijeme)%></td>
			<td><%=v.getBrzina()%></td>
			<td><%=v.getSnaga()%></td>
			<td><%=v.getStruja()%></td>
			<td><%=v.getVisina()%></td>
			<td><%=v.getGpsBrzina()%></td>
			<td><%=v.getPreostaloKm()%></td>
			<td><%=v.getUkupnoKm()%></td>
			<td><%=v.getGpsSirina()%></td>
			<td><%=v.getGpsDuzina()%></td>
		</tr>
		<%
		}
		%>
	</table>
</body>
</html>
