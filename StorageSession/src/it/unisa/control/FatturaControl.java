package it.unisa.control;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

import it.unisa.model.AddressBean;
import it.unisa.model.AddressDaoImpl;
import it.unisa.model.OrderBean;
import it.unisa.model.ProductBean;
import it.unisa.model.UserBean;

public class FatturaControl extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private static AddressDaoImpl AddressDao = new AddressDaoImpl();
	
    public FatturaControl() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            	
    	// Recupera i dati necessari dalla sessione
        
    	UserBean user = (UserBean) request.getSession().getAttribute("user");
        OrderBean order = (OrderBean) request.getSession().getAttribute("order");
        @SuppressWarnings("unchecked")
		List<ProductBean> listaProdotti = (List<ProductBean>) request.getSession().getAttribute("listaProd");
        AddressBean address = null;
        try {
			 address = AddressDao.findAddressByID(order.getId_indirizzo());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        int iva = 22;
        
        double totaleIva = order.getPrezzoTot() / 100 * iva;

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
 
        
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
        
        // Importa modello fattura
            PDDocument document = null;
            try {
                document = PDDocument.load(new File("C:\\Users\\tulli\\Desktop\\TSW\\struttura_fattura.pdf"));
            } catch (IOException e) {
                // Gestione dell'eccezione: log, reindirizzamento, messaggio di errore, ecc.
                e.printStackTrace();
                // Esempio di reindirizzamento a una pagina di errore
                response.sendRedirect("error-page.jsp");
                return;
            }

            // modulo del documento
            PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();

            // Imposta i valori
            try {
                acroForm.getField("nfattura").setValue(String.valueOf(order.getId_ordine()));
                acroForm.getField("Data").setValue(LocalDate.now().toString());
                acroForm.getField("IVA").setValue(iva + "%");
                acroForm.getField("nomeCognome").setValue(user.getCognome() + " " + user.getNome());
                acroForm.getField("dataNascita").setValue(user.getDataNascita().toString());
                acroForm.getField("indirizzo").setValue(address.toStringScript());
                acroForm.getField("numeroTelefono").setValue(user.getNumTelefono());

                int i = 1;
                String prodottoRow = "ProdottoRow";
                String quantitaRow = "QuantitaRow";
                String prezzoRow = "PrezzoRow";
                String totaleRow = "TotaleRow";

                for (ProductBean prod : listaProdotti) {
                    acroForm.getField(prodottoRow + i).setValue(prod.getName());
                    acroForm.getField(quantitaRow + i).setValue(String.valueOf(prod.getQuantity()));
                    acroForm.getField(prezzoRow + i).setValue("€ " + String.valueOf(prod.getPrice()));
                    acroForm.getField(totaleRow + i).setValue("€ " + String.valueOf(prod.getPrice() * prod.getQuantity()));
                    i++;
                }

                acroForm.getField("Imponibile").setValue("€ " + String.valueOf(decimalFormat.format(order.getPrezzoTot())));
                acroForm.getField("totaleIva").setValue(String.valueOf(decimalFormat.format(totaleIva)));
                acroForm.getField("totaleFattura").setValue(String.valueOf(decimalFormat.format(order.getPrezzoTot() + totaleIva)));
            } catch (IOException e) {
                
            	// Gestione dell'eccezione: log, reindirizzamento, messaggio di errore, ecc.
                e.printStackTrace();
                return;
            }

            List<PDField> fields = acroForm.getFields();

            // Imposta l'attributo "read-only" per ogni campo modulo
            for (PDField field : fields) {
                field.setReadOnly(true);
            }

            // Salva il documento in un array di byte
            try {
                document.save(outputStream);
            } catch (IOException e) {
            	
                // Gestione dell'eccezione: log, reindirizzamento, messaggio di errore, ecc.
                e.printStackTrace();
                return;
            } finally {
                document.close();
            }

            // Imposta il tipo di contenuto della risposta come PDF
            response.setContentType("application/pdf");
            
            // Download della fattura
            String fileName = "fattura.pdf";
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

            // Restituisci l'array di byte del documento PDF
            try (ServletOutputStream outputStream2 = response.getOutputStream()) {
                outputStream.writeTo(outputStream2);
                outputStream2.flush();
            } catch (IOException e) {
            	
                // Gestione dell'eccezione: log, reindirizzamento, messaggio di errore, ecc.
                e.printStackTrace();
                
            }
        } catch (IOException e) {
        	
            // Gestione dell'eccezione: log, reindirizzamento, messaggio di errore, ecc.
            
        	e.printStackTrace();
            
        }
        
        
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}