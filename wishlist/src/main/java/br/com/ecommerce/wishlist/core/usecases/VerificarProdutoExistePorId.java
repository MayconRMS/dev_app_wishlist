package br.com.ecommerce.wishlist.core.usecases;

import br.com.ecommerce.wishlist.core.usecases.interfaces.VerificarProdutoExistePorIdInput;
import br.com.ecommerce.wishlist.gateway.database.ProdutoDS;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificarProdutoExistePorId implements VerificarProdutoExistePorIdInput {

    private final ProdutoDS produtoDS;

    @Override
    public boolean verificaExistenciaProdutoPorId(String id) {
        if (id == null)
            return false;
        return produtoDS.buscarPorId(id).isPresent();
    }
}
