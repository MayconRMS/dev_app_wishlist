package br.com.ecommerce.wishlist.core.usecases;

import br.com.ecommerce.wishlist.core.entity.factory.WishlistFactory;
import br.com.ecommerce.wishlist.core.request.AdicionarProdutoWishlistClienteRequest;
import br.com.ecommerce.wishlist.core.response.AdicionarProdutoWishlistResponse;
import br.com.ecommerce.wishlist.core.usecases.interfaces.AdicionarProdutoNaWishlistClienteInput;
import br.com.ecommerce.wishlist.core.usecases.interfaces.VerificarClienteExistePorIdInput;
import br.com.ecommerce.wishlist.core.usecases.interfaces.VerificarProdutoExistePorIdInput;
import br.com.ecommerce.wishlist.exception.DadosFaltandoException;
import br.com.ecommerce.wishlist.exception.OperacaoInvalidaException;
import br.com.ecommerce.wishlist.exception.RegistroNaoEncontradoException;
import br.com.ecommerce.wishlist.gateway.database.WishlistDS;
import br.com.ecommerce.wishlist.gateway.database.model.WishlistDataModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdicionarProdutoNaWishlistCliente implements AdicionarProdutoNaWishlistClienteInput {

    private final WishlistDS wishlistDS;
    private final VerificarClienteExistePorIdInput consultarCliente;
    private final VerificarProdutoExistePorIdInput consultarProduto;
    private final WishlistFactory wishlistFactory;

    @Override
    public AdicionarProdutoWishlistResponse adicionar(AdicionarProdutoWishlistClienteRequest wishlistRequest) {
        if (!consultarProduto.verificaExistenciaProdutoPorId(wishlistRequest.getIdProduto()))
            throw new RegistroNaoEncontradoException("Produto não existe.");

        if (wishlistRequest.getIdCliente() == null || wishlistRequest.getIdCliente().isBlank())
            throw new DadosFaltandoException("É obrigatorio informar a id do cliente para adicionar um produto.");
        if (!consultarCliente.consultaExistenciaClientePorId(wishlistRequest.getIdCliente()))
            throw new RegistroNaoEncontradoException("Cliente não encontrado.");

        String id = null;
        var wishlist = wishlistFactory.criar("nome", wishlistRequest.getIdCliente(), List.of());

        Optional<WishlistDataModel> optionalWishlistDataModel = wishlistDS.buscarWishlistPorClienteId(wishlistRequest.getIdCliente());
        if (optionalWishlistDataModel.isPresent()) {
            var wishlistDataModel = optionalWishlistDataModel.get();
            wishlist = wishlistFactory.criar(wishlistDataModel.getNome(), wishlistDataModel.getIdCliente(), wishlistDataModel.getIdProdutos());
            id = wishlistDataModel.getId();
        }

        if (wishlist.isWishlistCheia())
            throw new OperacaoInvalidaException("Wishlist já está cheia.");

        if (wishlist.getIdProdutos().stream().anyMatch(s -> s.equals(wishlistRequest.getIdProduto()))) {
            throw new OperacaoInvalidaException("Produto já está na wishlist");
        }

        wishlist.getIdProdutos().add(wishlistRequest.getIdProduto());

        var wishlistDataModel = wishlistDS.save(id, wishlist);

        return new AdicionarProdutoWishlistResponse(wishlistDataModel.getId(),
                wishlist.getIdCliente(), wishlist.getIdProdutos());
    }

}
