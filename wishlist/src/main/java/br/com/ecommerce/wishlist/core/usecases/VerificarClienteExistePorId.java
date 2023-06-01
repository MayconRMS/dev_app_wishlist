package br.com.ecommerce.wishlist.core.usecases;

import br.com.ecommerce.wishlist.gateway.database.ClienteDS;
import br.com.ecommerce.wishlist.core.usecases.interfaces.VerificarClienteExistePorIdInput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificarClienteExistePorId implements VerificarClienteExistePorIdInput {

    private final ClienteDS clienteDS;

    @Override
    public boolean consultaExistenciaClientePorId(String id) {
        if (id == null)
            return false;
        return clienteDS.buscaPorId(id).isPresent();
    }
}
