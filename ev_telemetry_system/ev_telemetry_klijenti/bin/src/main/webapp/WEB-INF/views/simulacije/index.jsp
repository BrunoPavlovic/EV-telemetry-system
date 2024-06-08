<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>REST MVC - Stranica simulacija</title>
<link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath}/resources/css/nwtis.css">
<nav>
	<a href="${pageContext.servletContext.contextPath}">Izbornik</a>
	<a href="${pageContext.servletContext.contextPath}/mvc/simulacije/pocetak">Početna stranica vožnji</a>
</nav>
</head>
<body>
	<h1>REST MVC - Stranica simulacija</h1>
	<ul>
		<li>
			<h2>Pretraživanje vožnji u intervalu</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/simulacije/pretrazivanjeVoznji">
				<table>
					<tr>
						<td>Od vremena:</td>
						<td><input name="odVremena" required/> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>Do vremena:</td>
						<td><input name="doVremena" required/>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit" value=" Dohvati vožnje "></td>
					</tr>
				</table>
			</form>
		<li>
			<h2>Pretaživanje vožnji u intervalu za e-vozilo</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/simulacije/pretrazivanjeVoznjiZaVozilo">
				<table>
					<tr>
						<td>Id e-vozila:</td>
						<td><input name="id" required>
					</tr>
					<tr>
						<td>Od vremena:</td>
						<td><input name="odVremena" required/> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>Do vremena:</td>
						<td><input name="doVremena" required/>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit" value=" Dohvati vožnje "></td>
					</tr>
				</table>
			</form>
		</li>
		<li>
			<h2>Ispis vožnji za e-vozilo</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/simulacije/voznjeZaVozilo">
				<table>
					<tr>
						<td>Id e-vozila:</td>
						<td><input name="id" required>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit"
							value=" Ispiši vožnje za e-vozilo"></td>
					</tr>
				</table>
			</form>
		</li>
		<li>
			<h2>Nova vožnja</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/simulacije/dodajVoznju">
				<table>
					<tr>
						<td>Id e-vozila:</td>
						<td><input name="id" type="number" required></td>
					</tr>
					<tr>
						<td>Broj:</td>
						<td><input name="broj" type="number" required></td>
					</tr>
					<tr>
						<td>Vrijeme:</td>
						<td><input name="vrijeme" type="text" required></td>
					</tr>
					<tr>
						<td>Brzina:</td>
						<td><input name="brzina" type="number" step="any" required></td>
					</tr>
					<tr>
						<td>Snaga:</td>
						<td><input name="snaga" type="number" step="any" required></td>
					</tr>
					<tr>
						<td>Struja:</td>
						<td><input name="struja" type="number" step="any" required></td>
					</tr>
					<tr>
						<td>Visina:</td>
						<td><input name="visina" type="number" step="any" required></td>
					</tr>
					<tr>
						<td>Gps Brzina:</td>
						<td><input name="gpsBrzina" type="number" step="any" required></td>
					</tr>
					<tr>
						<td>Temperatura vozila:</td>
						<td><input name="tempVozila" type="number" required></td>
					</tr>
					<tr>
						<td>Postotak baterije:</td>
						<td><input name="postotakBaterija" type="number" required></td>
					</tr>
					<tr>
						<td>Napon baterije:</td>
						<td><input name="naponBaterija" type="number" step="any" required></td>
					</tr>
					<tr>
						<td>Kapacitet baterije:</td>
						<td><input name="kapacitetBaterija" type="number" required></td>
					</tr>
					<tr>
						<td>Temperatura baterije:</td>
						<td><input name="tempBaterija" type="number" required></td>
					</tr>
					<tr>
						<td>Preostalo kilometara:</td>
						<td><input name="preostaloKm" type="number" step="any" required></td>
					</tr>
					<tr>
						<td>Ukupno kilometara:</td>
						<td><input name="ukupnoKm" type="number" step="any" required></td>
					</tr>
					<tr>
						<td>Gps širina:</td>
						<td><input name="gpsSirina" type="number" step="any" required></td>
					</tr>
					<tr>
						<td>Gps dužina:</td>
						<td><input name="gpsDuzina" type="number" step="any" required></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit"
							value="Dodaj vožnju za e-vozilo"></td>
					</tr>
				</table>
			</form>
			<div id="poruka">${poruka}</div>
		</li>
		</li>
	</ul>
</body>
</html>
