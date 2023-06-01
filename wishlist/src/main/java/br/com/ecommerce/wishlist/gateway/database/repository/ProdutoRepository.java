package br.com.ecommerce.wishlist.gateway.database.repository;


import br.com.ecommerce.wishlist.gateway.database.model.ProdutoDataModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends MongoRepository<ProdutoDataModel, String> {
}
