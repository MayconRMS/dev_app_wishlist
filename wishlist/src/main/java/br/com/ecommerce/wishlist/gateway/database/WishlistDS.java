package br.com.ecommerce.wishlist.gateway.database;


import br.com.ecommerce.wishlist.core.entity.Wishlist;
import br.com.ecommerce.wishlist.gateway.database.model.WishlistDataModel;

import java.util.List;
import java.util.Optional;

public interface WishlistDS {

    Optional<WishlistDataModel> buscarWishlistPorClienteId(String clienteId);

    List<WishlistDataModel> buscarAllWishlist();

    List<WishlistDataModel> buscarWishlistPorNomeLike(String nome);
    WishlistDataModel save(String idWishlist, Wishlist wishlist);


}
