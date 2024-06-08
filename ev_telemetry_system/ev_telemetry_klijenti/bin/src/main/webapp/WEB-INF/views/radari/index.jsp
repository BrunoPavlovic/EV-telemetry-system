<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath}/resources/css/nwtis.css">
<nav>
	<a href="${pageContext.servletContext.contextPath}">Izbornik</a>
	<a href="${pageContext.servletContext.contextPath}/mvc/radari/pocetak">Početna stranica radara</a>
	<a href="${pageContext.servletContext.contextPath}/mvc/radari/ispisRadara">Ispis svih radara</a>
</nav>
<title>REST MVC - Stranica radara</title>
</head>
<body>
	<h1>REST MVC - Stranica radara</h1>
	<ul>
		<li>
			<h2>Ispis podataka za radar</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/radari/podaciZaRadar">
				<table>
					<tr>
						<td>Id:</td>
						<td><input name="id" required>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit" value=" Ispiši podatke radara"></td>
					</tr>
				</table>
			</form>
		</li>
		<li>
			<h2>Resetiranje radara</h2>
			<form method="post" action="${pageContext.request.contextPath}/mvc/radari/reset">
				<button type="submit">Resetiraj radare</button>
			</form>
			<div id="porukaReset">${porukaReset}</div>
		</li>
		<li>
			<h2>Provjera radara s id:</h2>
			<form method="post" action="${pageContext.request.contextPath}/mvc/radari/provjeriRadar">
				<label>Id:</label>
				<input name="id" required>
				<button type="submit">Provjeri radar</button>
			</form>
			<div id="porukaProvjera">${porukaProvjera}</div>
		</li>
		<li>
			<h2>Brisanje svih radara</h2>
			<form method="post" action="${pageContext.request.contextPath}/mvc/radari/obrisiSve">
				<button type="submit">Obriši sve radare</button>
			</form>
			<div id="porukaBrisanjeSvih">${porukaBrisanjeSvih}</div>
		</li>
		<li>
			<h2>Brisanje radara s id</h2>
			<form method="post" action="${pageContext.request.contextPath}/mvc/radari/obrisiRadar">
				<label>Id:</label>
				<input name="id" required>
				<button type="submit">Obriši radar</button>
			</form>
			<div id="porukaObrisiRadar">${porukaObrisiRadar}</div>
		</li>
	</ul>
</body>
</html>
