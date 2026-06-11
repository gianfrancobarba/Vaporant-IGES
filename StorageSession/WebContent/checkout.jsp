<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.List,it.unisa.model.Cart,it.unisa.model.ProductBean,it.unisa.model.AddressBean" %>

<%
    // AuthFilter garantisce un utente autenticato per questa pagina.
    Cart cart = (Cart) request.getSession().getAttribute("cart");
    request.setAttribute("cart", cart);

    // gli indirizzi sono caricati in sessione al login (e risincronizzati dopo
    // l'aggiunta di un nuovo indirizzo), non ri-interrogati qui.
    List<AddressBean> indirizzi = (List<AddressBean>) session.getAttribute("indirizzi");
%>

<!DOCTYPE html>
<html lang = "it">
<head>
    <meta charset="UTF-8">
    <title>Checkout</title>
    <link href="checkoutStyle.css" rel="stylesheet" type="text/css">
</head>
<body>
    <jsp:include page="Header.jsp" />
    <h1>checkout</h1>
    <table>
        <thead>
            <tr>
                <th>Prodotto</th>
                <th>Quantità</th>
                <th>Prezzo</th>
            </tr>
        </thead>
        <tbody>
            <% if (cart != null) { %>
                <% for (ProductBean beancart : cart.getProducts()) { %>
                    <tr>
                        <td>
                            <h1><%= beancart.getName() %></h1>
                        </td>
                        <td>
                            <form action="cart?action=aggiornaCheck&id=<%= beancart.getCode() %>"
                                method="POST" class="update">
                                <input type="number" id="quantita" name="quantita"
                                    value="<%= beancart.getQuantity() %>" required min="1"
                                    max="<%= beancart.getQuantityStorage() %>">
                                <button class="checkout-btn" type="submit">Aggiorna</button>
                            </form>
                        </td>
                        <td>
                            <%= beancart.getPrice() + "€" %>
                        </td>
                    </tr>
                <% } %>
            <% } %>
        </tbody>
    </table>

    <%-- carrello assente/vuoto (robustezza): evita NPE, il form resta utilizzabile --%>
    <span>Totale: <%= cart != null ? cart.getPrezzoTotale() : "0.00" %>€</span>

    <form action="Ordine" method="POST" class="none">
        <%-- Dropdown per l'indirizzo di spedizione --%>
        <div class="dropdown">
            <label for="addressDropdown" class="placeholder">Indirizzo di spedizione: </label>
            <select id="addressDropdown" name="addressDropdown" required>
                <option value="">Seleziona un indirizzo</option>
                <%-- Aggiungi gli indirizzi solo se l'utente ne ha --%>
                <% if (indirizzi != null) { %>
                    <% for (AddressBean address : indirizzi) { %>
                        <option value="<%= address.getId() %>">
                            <%= address.toStringScript() %>
                        </option>
                    <% } %>
                <% } %>
            </select>
        </div>

        <%-- Dropdown per l'indirizzo di fatturazione --%>
        <div class="dropdown">
            <label for="addressDropdown2" class="placeholder">Indirizzo di fatturazione</label>
            <select id="addressDropdown2" name="addressDropdown2" required>
                <option value="">Seleziona un indirizzo</option>
                <%-- Aggiungi gli indirizzi solo se l'utente ne ha --%>
                <% if (indirizzi != null) { %>
                    <% for (AddressBean address : indirizzi) { %>
                        <option value="<%= address.getId() %>">
                            <%= address.toStringScript() %>
                        </option>
                    <% } %>
                <% } %>
            </select>
        </div>

        <%-- Bottone per aggiungere un indirizzo se l'utente non ne ha --%>
        <%
            boolean hasAddresses = (indirizzi != null && !indirizzi.isEmpty());
        %>
        <% if (!hasAddresses) { %>
            <div class="add">
                <a href="AddressForm.jsp">Aggiungi indirizzo..</a>
            </div>
        <% } %>

        <span>Paga con:</span>
                <div class = "radio">
                    <input type="radio" id="paypal" name="payment" required value="PayPal">
                    <label for="paypal">PayPal</label>
                </div>
                <div class = "radio">
                    <input type="radio" id="Carta di credito/debito" name="payment" required value="Carta di credito/debito">
                    <label for="Carta di credito/debito">Carta di credito/debito</label>
                </div>
             <div class = "checkout">
                <button class="checkout-btn" type="submit">Acquista</button>
             </div>
    </form>

    <jsp:include page="Footer.jsp" />
</body>
</html>