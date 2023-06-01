package br.com.ecommerce.wishlist.core.usecases;

import br.com.ecommerce.wishlist.core.response.ConsultarWishlistResponseList;
import br.com.ecommerce.wishlist.core.usecases.interfaces.ConsultarTodasWishlistInput;
import br.com.ecommerce.wishlist.gateway.database.WishlistDS;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConsultarTodasWishlist implements ConsultarTodasWishlistInput {

    private final WishlistDS wishlistDS;

    @Override
    public ConsultarWishlistResponseList consulta() {
        var consultarWishlistResponseList = new ConsultarWishlistResponseList();
        wishlistDS.buscarAllWishlist()
                .forEach(wish -> consultarWishlistResponseList.add(wish.getId(), wish.getNome(), wish.getIdCliente(), wish.getIdProdutos()));
        return consultarWishlistResponseList;
    }
}
