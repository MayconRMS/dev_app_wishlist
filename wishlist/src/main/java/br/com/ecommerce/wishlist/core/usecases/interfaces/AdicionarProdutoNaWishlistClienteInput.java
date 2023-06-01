package br.com.ecommerce.wishlist.core.usecases.interfaces;

import br.com.ecommerce.wishlist.core.request.AdicionarProdutoWishlistClienteRequest;
import br.com.ecommerce.wishlist.core.response.AdicionarProdutoWishlistResponse;

public interface AdicionarProdutoNaWishlistClienteInput {

    AdicionarProdutoWishlistResponse adicionar(AdicionarProdutoWishlistClienteRequest request);

}
