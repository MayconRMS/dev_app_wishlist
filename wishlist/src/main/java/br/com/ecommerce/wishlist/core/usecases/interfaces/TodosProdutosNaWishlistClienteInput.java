package br.com.ecommerce.wishlist.core.usecases.interfaces;

import br.com.ecommerce.wishlist.core.response.ConsultarProdutoResponseList;

public interface TodosProdutosNaWishlistClienteInput {
    ConsultarProdutoResponseList listarTodosProdutosExisteNaWishlistCliente(String idCliente);
}
