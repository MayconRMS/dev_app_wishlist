package br.com.ecommerce.wishlist.core.usecases.interfaces;

public interface RemoverProdutoNaWishlistClienteInput {
    void removeDaWishlistPorId(String idCliente, String idProduto);
}
