package it.unisa.service;

import it.unisa.model.Cart;
import it.unisa.model.ProductBean;

/**
 * Logica applicativa relativa al carrello. Risolve l'ID prodotto tramite {@link ProductService}
 * e applica l'operazione al {@link Cart} (oggetto di sessione gestito dal controller).
 */
public class CartService {

	private final ProductService productService;

	public CartService() {
		this(new ProductService());
	}

	public CartService(ProductService productService) {
		this.productService = productService;
	}

	public void addProduct(Cart cart, int productId) {
		ProductBean prod = productService.findByKey(productId);
		if (prod != null) {
			cart.addProduct(prod);
		}
	}

	public void deleteProduct(Cart cart, int productId) {
		ProductBean prod = productService.findByKey(productId);
		if (prod != null) {
			cart.deleteProduct(prod);
		}
	}

	public void aggiorna(Cart cart, int productId, int quantita) {
		ProductBean prod = productService.findByKey(productId);
		if (prod != null) {
			cart.aggiorna(prod, quantita);
		}
	}
}
