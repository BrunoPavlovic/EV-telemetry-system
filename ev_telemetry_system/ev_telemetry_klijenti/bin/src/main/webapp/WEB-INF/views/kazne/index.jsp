<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>REST MVC - Početna stranica</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath}/resources/css/nwtis.css">
        <nav>
        	<a href="${pageContext.servletContext.contextPath}">Izbornik</a>
        	<a href="${pageContext.servletContext.contextPath}/mvc/kazne/pocetak">Početna stranica kazni</a>
        	<a href="${pageContext.servletContext.contextPath}/mvc/kazne/ispisKazni">Ispis svih kazni</a>
        </nav>
    </head>
    <body>
        <h1>REST MVC - Početna stranica</h1>
        <ul>
            <li>
            <h2>Pretraživanje kazni u intervalu</h2>
                <form method="post" action="${pageContext.servletContext.contextPath}/mvc/kazne/pretrazivanjeKazni">
                    <table>
                        <tr>
                            <td>Od vremena: </td>
                            <td><input name="odVremena" required/>
                                <input type="hidden" name="${mvc.csrf.name}" value="${mvc.csrf.token}"/>
                            </td>
                        </tr>
                        <tr>
                            <td>Do vremena: </td>
                            <td><input name="doVremena" required/>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td><input type="submit" value=" Dohvati kazne "></td>
                        </tr>                        
                    </table>
                </form>
            </li>
            <li>
            	<h2>Kazna prema rb</h2>
				<form method="post" action="${pageContext.servletContext.contextPath}/mvc/kazne/ispisKazniRedniBroj">
                    <table>
                    	<tr>
                    		<td>Redni broj e-vozila:</td>
                    		<td><input name="rb" required>
                    	</tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td><input type="submit" value=" Ispiši kaznu za e-vozilo prema rednom broju"></td>
                        </tr>                        
                    </table>
                </form>
            </li>
            <li>
            	<h2>Pretaživanje kazni u intervalu za e-vozilo</h2>
            	                <form method="post" action="${pageContext.servletContext.contextPath}/mvc/kazne/pretrazivanjeKazniZaVozilo">
                    <table>
                       	<tr>
                    		<td>Id e-vozila:</td>
                    		<td><input name="id" required>
                    	</tr>
                        <tr>
                            <td>Od vremena: </td>
                            <td><input name="odVremena" required/>
                                <input type="hidden" name="${mvc.csrf.name}" value="${mvc.csrf.token}"/>
                            </td>
                        </tr>
                        <tr>
                            <td>Do vremena: </td>
                            <td><input name="doVremena" required/>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td><input type="submit" value=" Dohvati kazne "></td>
                        </tr>                        
                    </table>
                </form>
            </li>
			<li>
            	<h2>Ispis kazni za e-vozilo</h2>
            	                <form method="post" action="${pageContext.servletContext.contextPath}/mvc/kazne/ispisKazniZaVozilo">
                    <table>
                    	<tr>
                    		<td>Id e-vozila:</td>
                    		<td><input name="id" required>
                    	</tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td><input type="submit" value=" Ispiši kazne za e-vozilo"></td>
                        </tr>                        
                    </table>
                </form>
            </li>                  
        </ul>          
    </body>
</html>
