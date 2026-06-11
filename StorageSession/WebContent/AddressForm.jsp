<%@  page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%-- AuthFilter garantisce un utente autenticato per questa pagina. --%>
<!DOCTYPE html>
<html lang = "it">
<head>
	<link rel="stylesheet" type="text/css" href="AddressStyle.css">
	<title>Aggiungi Indirizzo</title>
</head>
<body>
    <jsp:include page="Header.jsp" />
	<main>
		<section class="form-section">
			<h2>Registra il tuo indirizzo di spedizione</h2>
			<form action="AddressControl" method="POST" class="address-form">
				<div class="input-container">
					<input id="citta" class="input" type="text" name="citta" placeholder="Citta" required>
				</div>
				<div class="input-container">
					<input id="provincia" class="input" type="text" name="provincia" placeholder="Provincia" required>
				</div>
				<div class="input-container">
					<input id="cap" class="input" type="text" name="cap" placeholder="Cap" required>
				</div>
				<div class="input-container">
					<input id="via" class="input" type="text" name="via" placeholder="Via" required>
				</div>
				<div class="input-container">
					<input id="numero_civico" class="input" type="text" name="numero_civico" placeholder="Numero Civico"required>
				</div>
				<div class="input-container">
					<input id="stato" class="input" type="text" name="stato" placeholder="Stato"required>
				</div>
				<input type="submit" value="Aggiungi" class="submit-button">
				<!-- <button onclick="aggiungiIndirizzo()" class="btn">AGGIUNGI INDIRIZZO</button> -->
			</form>
		</section>
	</main>
    <jsp:include page="Footer.jsp" />
</body>
</html>