package it.unisa.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Cart {

	private ArrayList<ProductBean> products;
	private BigDecimal prezzoTotale = BigDecimal.ZERO;

	public Cart() {
		products = new ArrayList<ProductBean>();
	}

	public BigDecimal getPrezzoTotale() {
		return prezzoTotale.setScale(2, RoundingMode.HALF_UP);
	}

	public void setPrezzoTotale(BigDecimal prezzoTotale) {
		this.prezzoTotale = prezzoTotale;
	}

	public List<ProductBean> getProducts() {
		return  products;
	}

	public void addProduct(ProductBean product) {

		ProductBean prod = containsProduct(product);

		if (!products.isEmpty() && prod != null)
		{
			if(prod.getQuantity() < prod.getQuantityStorage())
				aggiorna(product,prod.getQuantity() + 1);
		} else {
			products.add(product);
			setPrezzoTotale(prezzoTotale.add(BigDecimal.valueOf(product.getPrice())));
		}

	}

	public void deleteProduct(ProductBean product) {
		for(ProductBean prod : products) {
			if(prod.getCode() == product.getCode()) {
					BigDecimal totaleRiga = BigDecimal.valueOf(prod.getPrice()).multiply(BigDecimal.valueOf(prod.getQuantity()));
					setPrezzoTotale(prezzoTotale.subtract(totaleRiga));
					products.remove(prod);

				break;
			}
		}
 	}

	public ProductBean containsProduct(ProductBean product) {
		for (ProductBean pb : products) {
			if (pb.getCode() == product.getCode()) {
				return pb;
			}
		}
		return null;
	}

	public void aggiorna(ProductBean product, int quantita) {

		int index;
		for (index = 0; index < products.size(); index++) {
			if (products.get(index).getCode() == product.getCode()) {

				BigDecimal prezzoUnitario = BigDecimal.valueOf(products.get(index).getPrice());
				setPrezzoTotale(prezzoTotale.subtract(prezzoUnitario.multiply(BigDecimal.valueOf(products.get(index).getQuantity()))));

				products.get(index).setQuantity(quantita);
				setPrezzoTotale(prezzoTotale.add(prezzoUnitario.multiply(BigDecimal.valueOf(quantita))));

				break;
			}
		}
	}
}
