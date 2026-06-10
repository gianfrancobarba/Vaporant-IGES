package it.unisa.control;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import it.unisa.model.AddressBean;
import it.unisa.model.AddressDaoImpl;
import it.unisa.model.OrderBean;
import it.unisa.model.ProductBean;
import it.unisa.model.UserBean;

public class FatturaControl extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static AddressDaoImpl addressDao = new AddressDaoImpl();

	private static final int IVA = 22;

	public FatturaControl() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		UserBean user = (UserBean) request.getSession().getAttribute("user");
		OrderBean order = (OrderBean) request.getSession().getAttribute("order");
		@SuppressWarnings("unchecked")
		List<ProductBean> listaProdotti = (List<ProductBean>) request.getSession().getAttribute("listaProd");

		// Dati indispensabili mancanti: nessuna fattura da generare
		if (user == null || order == null || listaProdotti == null) {
			response.sendRedirect("error-page.jsp");
			return;
		}

		AddressBean address = null;
		try {
			address = addressDao.findAddressByID(order.getId_indirizzo());
		} catch (SQLException e) {
			e.printStackTrace();
			response.sendRedirect("error-page.jsp");
			return;
		}

		BigDecimal imponibile = order.getPrezzoTot();
		BigDecimal totaleIva = imponibile.multiply(BigDecimal.valueOf(IVA))
				.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
		BigDecimal totaleFattura = imponibile.add(totaleIva);
		DecimalFormat df = new DecimalFormat("#0.00");

		// Generazione del PDF a runtime (nessun template esterno, nessun percorso assoluto)
		try (PDDocument document = new PDDocument();
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

			PDPage page = new PDPage(PDRectangle.A4);
			document.addPage(page);

			float margin = 50;
			float leading = 18f;
			float y = page.getMediaBox().getHeight() - margin;

			try (PDPageContentStream cs = new PDPageContentStream(document, page)) {

				// Intestazione
				cs.setFont(PDType1Font.HELVETICA_BOLD, 20);
				y = line(cs, margin, y, leading, "Fattura n. " + order.getId_ordine());
				y -= leading;

				// Dati documento e cliente
				cs.setFont(PDType1Font.HELVETICA, 12);
				y = line(cs, margin, y, leading, "Data: " + LocalDate.now());
				y = line(cs, margin, y, leading, "Cliente: " + user.getCognome() + " " + user.getNome());
				y = line(cs, margin, y, leading, "Data di nascita: " + user.getDataNascita());
				y = line(cs, margin, y, leading, "Telefono: " + user.getNumTelefono());
				if (address != null) {
					y = line(cs, margin, y, leading, "Indirizzo: " + address.toStringScript());
				}
				y -= leading;

				// Tabella prodotti (font monospaziato per l'allineamento delle colonne)
				cs.setFont(PDType1Font.COURIER_BOLD, 11);
				y = line(cs, margin, y, leading,
						rpad("Prodotto", 26) + rpad("Quantita", 12) + rpad("Prezzo", 16) + "Totale");

				cs.setFont(PDType1Font.COURIER, 11);
				for (ProductBean prod : listaProdotti) {
					double totaleRiga = prod.getPrice() * prod.getQuantity();
					String riga = rpad(prod.getName(), 26)
							+ rpad(String.valueOf(prod.getQuantity()), 12)
							+ rpad("EUR " + df.format(prod.getPrice()), 16)
							+ "EUR " + df.format(totaleRiga);
					y = line(cs, margin, y, leading, riga);
				}
				y -= leading;

				// Totali
				cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
				y = line(cs, margin, y, leading, "Imponibile: EUR " + df.format(imponibile));
				y = line(cs, margin, y, leading, "IVA (" + IVA + "%): EUR " + df.format(totaleIva));
				y = line(cs, margin, y, leading, "Totale fattura: EUR " + df.format(totaleFattura));
			}

			document.save(outputStream);

			// Download del PDF
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename=\"fattura.pdf\"");
			try (ServletOutputStream out = response.getOutputStream()) {
				outputStream.writeTo(out);
				out.flush();
			}
		}
	}

	/** Scrive una riga di testo (sanificata) alla posizione data e ritorna la nuova ordinata. */
	private static float line(PDPageContentStream cs, float x, float y, float leading, String text)
			throws IOException {
		cs.beginText();
		cs.newLineAtOffset(x, y);
		cs.showText(sanitize(text));
		cs.endText();
		return y - leading;
	}

	/**
	 * Rimuove i diacritici (a&grave; -> a, e&grave; -> e, ...) e i caratteri non ASCII, per compatibilita'
	 * con i font standard del PDF (che non includono tutti i glifi Unicode, ad es. il simbolo dell'euro).
	 */
	private static String sanitize(String s) {
		if (s == null) {
			return "";
		}
		String n = Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
		return n.replaceAll("[^\\x20-\\x7E]", "");
	}

	/** Tronca o riempie con spazi la stringa alla lunghezza data, lasciando almeno uno spazio di separazione. */
	private static String rpad(String s, int len) {
		if (s == null) {
			s = "";
		}
		if (s.length() >= len) {
			return s.substring(0, Math.max(0, len - 1)) + " ";
		}
		StringBuilder sb = new StringBuilder(s);
		while (sb.length() < len) {
			sb.append(' ');
		}
		return sb.toString();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
