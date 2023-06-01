package br.com.ecommerce.wishlist.gateway.database.impl;

import br.com.ecommerce.wishlist.gateway.database.model.ClienteDataModel;
import br.com.ecommerce.wishlist.gateway.database.repository.ClienteRepository;
import br.com.ecommerce.wishlist.gateway.database.ClienteDS;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ClienteDSImpl implements ClienteDS {

    private final ClienteRepository clienteRepository;

    @Override
    public Optional<ClienteDataModel> buscaPorId(String id) {
        return clienteRepository.findById(id);
    }

}
