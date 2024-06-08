<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>REST MVC - Stranica vozila</title>
<link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath}/resources/css/nwtis.css">
<nav>
	<a href="${pageContext.servletContext.contextPath}">Izbornik</a>
	<a href="${pageContext.servletContext.contextPath}/mvc/vozila/pocetak">Početna stranica praćenih vožnji</a>
</nav>
</head>
<body>
	<h1>REST MVC - Stranica vozila</h1>
	<ul>
		<li>
			<h2>Pretraživanje praćenih vožnji u intervalu</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/vozila/pretrazivanjePracenihVoznji">
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
						<td><input type="submit" value=" Dohvati praćene vožnje "></td>
					</tr>
				</table>
			</form>
		<li>
			<h2>Pretaživanje praćenih vožnji u intervalu za e-vozilo</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/vozila/pretrazivanjePracenihVoznjiZaVozilo">
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
						<td><input name="doVremena" />
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit" value=" Dohvati praćene vožnje "></td>
					</tr>
				</table>
			</form>
		</li>
		<li>
			<h2>Ispis praćenih vožnji za e-vozilo</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/vozila/praceneVoznjeZaVozilo">
				<table>
					<tr>
						<td>Id e-vozila:</td>
						<td><input name="id" required>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit"
							value=" Ispiši praćene vožnje za e-vozilo"></td>
					</tr>
				</table>
			</form>
		</li>
		<li>
			<h2>Pokretanje praćene vožnje za e-vozilo - START</h2>
			<form method="post" action="${pageContext.request.contextPath}/mvc/vozila/start">
				<label>Id:</label>
				<input name="id" required>
				<button type="submit">START</button>
			</form>
			<div id="start">${start}</div>
		</li>
		<li>
			<h2>Zaustavljanje praćene vožnje za e-vozilo - STOP</h2>
			<form method="post" action="${pageContext.request.contextPath}/mvc/vozila/stop">
				<label>Id:</label>
				<input name="id" required>
				<button type="submit">STOP</button>
			</form>
			<div id="stop">${stop}</div>
		</li>
		<li>
			<h2>Nova praćena vožnja</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/vozila/dodajPracenuVoznju">
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
							value="Dodaj praćenu vožnju za e-vozilo"></td>
					</tr>
				</table>
			</form>
			<div id="poruka">${poruka}</div>
		</li>
		</li>
	</ul>
</body>
</html>
