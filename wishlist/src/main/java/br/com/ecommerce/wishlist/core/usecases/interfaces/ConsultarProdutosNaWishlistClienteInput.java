package br.com.ecommerce.wishlist.core.usecases.interfaces;

import br.com.ecommerce.wishlist.core.response.ConsultarProdutoResponseList;
import br.com.ecommerce.wishlist.core.response.VerificarProdutoExisteNaWishlistResponse;

public interface ConsultarProdutosNaWishlistClienteInput {
    VerificarProdutoExisteNaWishlistResponse verificaProdutoExisteNaWishlistCliente(String idCliente, String idProduto);
}
