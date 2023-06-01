package br.com.ecommerce.wishlist.gateway.database.repository;


import br.com.ecommerce.wishlist.gateway.database.model.WishlistDataModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends MongoRepository<WishlistDataModel, String> {

    Optional<WishlistDataModel> findByIdCliente(String clienteId);

    List<WishlistDataModel> findByNomeLike(String nome);

}
