package br.com.ecommerce.wishlist.core.usecases;

import br.com.ecommerce.wishlist.core.entity.factory.WishlistFactory;
import br.com.ecommerce.wishlist.core.usecases.interfaces.RemoverProdutoNaWishlistClienteInput;
import br.com.ecommerce.wishlist.core.usecases.interfaces.VerificarClienteExistePorIdInput;
import br.com.ecommerce.wishlist.core.usecases.interfaces.VerificarProdutoExistePorIdInput;
import br.com.ecommerce.wishlist.exception.DadosFaltandoException;
import br.com.ecommerce.wishlist.exception.OperacaoInvalidaException;
import br.com.ecommerce.wishlist.exception.RegistroNaoEncontradoException;
import br.com.ecommerce.wishlist.gateway.database.WishlistDS;
import br.com.ecommerce.wishlist.gateway.database.model.WishlistDataModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RemoverProdutoNaWishlistCliente implements RemoverProdutoNaWishlistClienteInput {

    private final WishlistDS wishlistDS;
    private final VerificarClienteExistePorIdInput consultarCliente;
    private final VerificarProdutoExistePorIdInput consultarProduto;
    private final WishlistFactory wishlistFactory;

    @Override
    public void removeDaWishlistPorId(String idCliente, String idProduto) {
        if (!consultarProduto.verificaExistenciaProdutoPorId(idProduto))
            throw new RegistroNaoEncontradoException("Produto não existe.");

        if (idCliente == null || idCliente.isBlank())
            throw new DadosFaltandoException("É obrigatorio informar a id do cliente para remover um produto.");
        if (!consultarCliente.consultaExistenciaClientePorId(idCliente))
            throw new RegistroNaoEncontradoException("Cliente não encontrado.");

        Optional<WishlistDataModel> optionalWishlistDataModel = wishlistDS.buscarWishlistPorClienteId(idCliente);

        if (optionalWishlistDataModel.isEmpty())
            throw new RegistroNaoEncontradoException("Wishlist não existe.");

        var wishlistDataModel = optionalWishlistDataModel.get();
        var wishlist = wishlistFactory.criar(wishlistDataModel.getNome(), wishlistDataModel.getIdCliente(), wishlistDataModel.getIdProdutos());

        wishlist.getIdProdutos().remove(idProduto);
        wishlistDS.save(wishlistDataModel.getId(), wishlist);
    }
}
