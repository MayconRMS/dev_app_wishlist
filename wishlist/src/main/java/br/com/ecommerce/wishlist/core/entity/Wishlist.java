package br.com.ecommerce.wishlist.core.entity;

import java.util.List;

public interface Wishlist {

    String getNome();

    String getIdCliente();

    List<String> getIdProdutos();

    boolean isWishlistCheia();
}
