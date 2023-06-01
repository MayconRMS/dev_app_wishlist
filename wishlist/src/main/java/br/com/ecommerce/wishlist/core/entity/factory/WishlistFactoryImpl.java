package br.com.ecommerce.wishlist.core.entity.factory;

import br.com.ecommerce.wishlist.core.entity.Wishlist;
import br.com.ecommerce.wishlist.core.entity.factory.WishlistFactory;
import br.com.ecommerce.wishlist.core.entity.WishlistImpl;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WishlistFactoryImpl implements WishlistFactory {
    @Override
    public Wishlist criar(String nome, String idCliente, List<String> idProdutos) {
        return new WishlistImpl(nome, idCliente, idProdutos);
    }
}
