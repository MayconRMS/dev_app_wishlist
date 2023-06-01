package br.com.ecommerce.wishlist.core.usecases;

import br.com.ecommerce.wishlist.core.response.VerificarProdutoExisteNaWishlistResponse;
import br.com.ecommerce.wishlist.core.usecases.interfaces.ConsultarProdutosNaWishlistClienteInput;
import br.com.ecommerce.wishlist.core.usecases.interfaces.VerificarClienteExistePorIdInput;
import br.com.ecommerce.wishlist.exception.DadosFaltandoException;
import br.com.ecommerce.wishlist.exception.RegistroNaoEncontradoException;
import br.com.ecommerce.wishlist.gateway.database.WishlistDS;
import br.com.ecommerce.wishlist.gateway.database.model.WishlistDataModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConsultarProdutosNaWishlistCliente implements ConsultarProdutosNaWishlistClienteInput {

    private final WishlistDS wishlistDS;
    private final VerificarClienteExistePorIdInput consultarCliente;

    @Override
    public VerificarProdutoExisteNaWishlistResponse verificaProdutoExisteNaWishlistCliente(String idCliente, String idProduto) {
        if (idCliente == null || idCliente.isBlank())
            throw new DadosFaltandoException("É obrigatorio informar a id do cliente.");
        if (!consultarCliente.consultaExistenciaClientePorId(idCliente))
            throw new RegistroNaoEncontradoException("Cliente não encontrado.");

        Optional<WishlistDataModel> optionalWishlistDataModel = wishlistDS.buscarWishlistPorClienteId(idCliente);

        if (optionalWishlistDataModel.isEmpty())
            throw new RegistroNaoEncontradoException("Wishlist não existe.");

        var wishlistDataModel = optionalWishlistDataModel.get();

        return new VerificarProdutoExisteNaWishlistResponse(wishlistDataModel.getIdProdutos()
                .stream()
                .anyMatch(id -> id.equals(idProduto)));

    }
}
