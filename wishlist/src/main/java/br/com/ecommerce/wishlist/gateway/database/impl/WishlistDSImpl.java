package br.com.ecommerce.wishlist.gateway.database.impl;

import br.com.ecommerce.wishlist.core.entity.Wishlist;
import br.com.ecommerce.wishlist.gateway.database.WishlistDS;
import br.com.ecommerce.wishlist.gateway.database.model.WishlistDataModel;
import br.com.ecommerce.wishlist.gateway.database.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class WishlistDSImpl implements WishlistDS {

    private final WishlistRepository wishlistRepository;

    @Override
    public Optional<WishlistDataModel> buscarWishlistPorClienteId(String clienteId) {
        return wishlistRepository.findByIdCliente(clienteId);
    }

    @Override
    public List<WishlistDataModel> buscarAllWishlist() {
        return wishlistRepository.findAll();
    }

    @Override
    public List<WishlistDataModel> buscarWishlistPorNomeLike(String nome) {
        return wishlistRepository.findByNomeLike(nome);
    }

    @Override
    public WishlistDataModel save(String idWishlist, Wishlist wishlist) {
        var wishlistDataModel = new WishlistDataModel(idWishlist, wishlist.getNome(), wishlist.getIdCliente(), wishlist.getIdProdutos());
        return wishlistRepository.save(wishlistDataModel);
    }
}
