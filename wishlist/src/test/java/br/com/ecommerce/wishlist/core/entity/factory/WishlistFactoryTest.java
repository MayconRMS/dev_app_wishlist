package br.com.ecommerce.wishlist.core.entity.factory;

import br.com.ecommerce.wishlist.core.entity.Wishlist;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class WishlistFactoryTest {

    WishlistFactory wishlistFactory = new WishlistFactoryImpl();

    @Test
    void deveCriarEntity(){
        Wishlist wishlist = wishlistFactory.criar("List", "1", List.of("2", "3"));

        Assertions.assertNotNull(wishlist);
        Assertions.assertEquals("List", wishlist.getNome());
        Assertions.assertEquals("1", wishlist.getIdCliente());
        Assertions.assertNotNull(wishlist.getIdProdutos());
        Assertions.assertEquals(2, wishlist.getIdProdutos().size());
        org.assertj.core.api.Assertions.assertThat(wishlist.getIdProdutos()).contains("2", "3");
    }
}
