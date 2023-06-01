package br.com.ecommerce.wishlist.gateway.database;

import br.com.ecommerce.wishlist.gateway.database.model.ProdutoDataModel;

import java.util.List;
import java.util.Optional;

public interface ProdutoDS {
    Optional<ProdutoDataModel> buscarPorId(String idProduto);
    List<ProdutoDataModel> buscarPorIdIn(List<String> ids);
}
