package br.com.ecommerce.wishlist.core.entity.factory;

import br.com.ecommerce.wishlist.core.entity.Wishlist;

import java.util.List;

public interface WishlistFactory {

    Wishlist criar(String nome, String idCliente, List<String> idProdutos);
}
