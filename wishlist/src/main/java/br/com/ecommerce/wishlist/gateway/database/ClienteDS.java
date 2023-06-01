package br.com.ecommerce.wishlist.gateway.database;

import br.com.ecommerce.wishlist.gateway.database.model.ClienteDataModel;

import java.util.List;
import java.util.Optional;

public interface ClienteDS {
    Optional<ClienteDataModel> buscaPorId(String id);
}
