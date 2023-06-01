package br.com.ecommerce.wishlist.core.usecases;

import br.com.ecommerce.wishlist.core.response.ConsultarProdutoResponseList;
import br.com.ecommerce.wishlist.core.usecases.interfaces.TodosProdutosNaWishlistClienteInput;
import br.com.ecommerce.wishlist.core.usecases.interfaces.VerificarClienteExistePorIdInput;
import br.com.ecommerce.wishlist.exception.DadosFaltandoException;
import br.com.ecommerce.wishlist.exception.RegistroNaoEncontradoException;
import br.com.ecommerce.wishlist.gateway.database.ProdutoDS;
import br.com.ecommerce.wishlist.gateway.database.WishlistDS;
import br.com.ecommerce.wishlist.gateway.database.model.WishlistDataModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TodosProdutosNaWishlistCliente implements TodosProdutosNaWishlistClienteInput {

    private final ProdutoDS produtoDS;
    private final WishlistDS wishlistDS;
    private final VerificarClienteExistePorIdInput consultarCliente;

    @Override
    public ConsultarProdutoResponseList listarTodosProdutosExisteNaWishlistCliente(String idCliente) {
        if (idCliente == null || idCliente.isBlank())
            throw new DadosFaltandoException("É obrigatorio informar a id do cliente para listar os produtos.");
        if (!consultarCliente.consultaExistenciaClientePorId(idCliente))
            throw new RegistroNaoEncontradoException("Cliente não encontrado.");

        Optional<WishlistDataModel> optionalWishlistDataModel = wishlistDS.buscarWishlistPorClienteId(idCliente);

        if (optionalWishlistDataModel.isEmpty())
            throw new RegistroNaoEncontradoException("Wishlist não existe.");

        var wishlistDataModel = optionalWishlistDataModel.get();

        var consultarProdutoResponseList = new ConsultarProdutoResponseList();
        produtoDS.buscarPorIdIn(wishlistDataModel.getIdProdutos())
                .forEach(produto -> consultarProdutoResponseList.add(produto.getId(), produto.getNome()));

        return consultarProdutoResponseList;

    }

}
