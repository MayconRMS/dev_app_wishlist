package br.com.ecommerce.wishlist.core.usecases;

import br.com.ecommerce.wishlist.core.entity.WishlistImpl;
import br.com.ecommerce.wishlist.core.entity.factory.WishlistFactory;
import br.com.ecommerce.wishlist.core.request.AdicionarProdutoWishlistClienteRequest;
import br.com.ecommerce.wishlist.core.usecases.interfaces.RemoverProdutoNaWishlistClienteInput;
import br.com.ecommerce.wishlist.core.usecases.interfaces.VerificarClienteExistePorIdInput;
import br.com.ecommerce.wishlist.core.usecases.interfaces.VerificarProdutoExistePorIdInput;
import br.com.ecommerce.wishlist.exception.DadosFaltandoException;
import br.com.ecommerce.wishlist.exception.RegistroNaoEncontradoException;
import br.com.ecommerce.wishlist.gateway.database.WishlistDS;
import br.com.ecommerce.wishlist.gateway.database.model.WishlistDataModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class RemoverProdutoNaWishlistClienteTest {

    RemoverProdutoNaWishlistClienteInput removerProdutoNaWishlistCliente;

    @Mock
    private WishlistDS wishlistDS;
    @Mock
    private VerificarClienteExistePorIdInput consultarCliente;
    @Mock
    private VerificarProdutoExistePorIdInput consultarProduto;
    @Mock
    private WishlistFactory wishlistFactory;

    @Test
    void removeDaWishlistPorId() {
    }

    @BeforeEach
    void init() {
        removerProdutoNaWishlistCliente = new RemoverProdutoNaWishlistCliente(wishlistDS, consultarCliente, consultarProduto, wishlistFactory);
    }

    @Test
    @DisplayName("Dado que cliente e o produto existam,e a wishlis deve remover o produto da wishlist")
    void deveRemoverProdutoAWishlist() {
        AdicionarProdutoWishlistClienteRequest wishlistRequest = new AdicionarProdutoWishlistClienteRequest("1", "3");
        List<String> produtos = new ArrayList<>();
        produtos.add("4");
        produtos.add("5");
        WishlistDataModel wishlistDataModel = new WishlistDataModel(null, "lista", wishlistRequest.getIdCliente(), produtos);

        Mockito.when(consultarProduto.verificaExistenciaProdutoPorId(wishlistRequest.getIdProduto())).thenReturn(true);
        Mockito.when(consultarCliente.consultaExistenciaClientePorId(wishlistRequest.getIdCliente())).thenReturn(true);
        Mockito.when(wishlistDS.buscarWishlistPorClienteId(wishlistRequest.getIdCliente())).thenReturn(Optional.of(wishlistDataModel));

        var wishlist = new WishlistImpl(wishlistDataModel.getNome(), wishlistDataModel.getIdCliente(), wishlistDataModel.getIdProdutos());
        Mockito.when(wishlistFactory.criar(wishlistDataModel.getNome(), wishlistDataModel.getIdCliente(), wishlistDataModel.getIdProdutos())).thenReturn(wishlist);

        Mockito.when(wishlistDS.save(any(), any())).thenReturn(wishlistDataModel);

        removerProdutoNaWishlistCliente.removeDaWishlistPorId(wishlistRequest.getIdCliente(), wishlistRequest.getIdProduto());

        Mockito.verify(wishlistDS).save(any(), any());
    }

    @Test
    @DisplayName("Dado que o produto não existe deve lançar exception.")
    void naoDeveAdicionarProdutoAWishlistQuandoNaoExisteProduto() {
        AdicionarProdutoWishlistClienteRequest wishlistRequest = new AdicionarProdutoWishlistClienteRequest("1", "3");
        Mockito.when(consultarProduto.verificaExistenciaProdutoPorId(wishlistRequest.getIdProduto())).thenReturn(false);

        org.assertj.core.api.Assertions.assertThatThrownBy(
                        () -> removerProdutoNaWishlistCliente.removeDaWishlistPorId(wishlistRequest.getIdCliente(), wishlistRequest.getIdProduto()))
                .isInstanceOf(RegistroNaoEncontradoException.class)
                .hasMessage("Produto não existe.");

    }

    @Test
    @DisplayName("Dado que o id Cliente não existe, deve lançar exception.")
    void naoDeveAdicionarProdutoAWishlistQuandoNaoExisteCliente() {
        AdicionarProdutoWishlistClienteRequest wishlistRequest = new AdicionarProdutoWishlistClienteRequest("1", "3");
        wishlistRequest.setIdCliente(null);
        Mockito.when(consultarProduto.verificaExistenciaProdutoPorId(wishlistRequest.getIdProduto())).thenReturn(true);

        org.assertj.core.api.Assertions.assertThatThrownBy(
                        () -> removerProdutoNaWishlistCliente.removeDaWishlistPorId(wishlistRequest.getIdCliente(), wishlistRequest.getIdProduto()))
                .isInstanceOf(DadosFaltandoException.class)
                .hasMessage("É obrigatorio informar a id do cliente para remover um produto.");
    }

    @Test
    @DisplayName("Dado que o Cliente não existe, deve lançar exception.")
    void naoDeveAdicionarProdutoAWishlistQuandoNaoExisteIdCliente() {
        AdicionarProdutoWishlistClienteRequest wishlistRequest = new AdicionarProdutoWishlistClienteRequest("1", "3");
        Mockito.when(consultarProduto.verificaExistenciaProdutoPorId(wishlistRequest.getIdProduto())).thenReturn(true);
        Mockito.when(consultarCliente.consultaExistenciaClientePorId(wishlistRequest.getIdCliente())).thenReturn(false);

        org.assertj.core.api.Assertions.assertThatThrownBy(
                        () -> removerProdutoNaWishlistCliente.removeDaWishlistPorId(wishlistRequest.getIdCliente(), wishlistRequest.getIdProduto()))
                .isInstanceOf(RegistroNaoEncontradoException.class)
                .hasMessage("Cliente não encontrado.");
    }

    @Test
    @DisplayName("Dado que a lista não exista")
    void naoDeveAdicionarProdutoAWishlistQuandoListaCheia() {
        AdicionarProdutoWishlistClienteRequest wishlistRequest = new AdicionarProdutoWishlistClienteRequest("1", "3");

        Mockito.when(consultarProduto.verificaExistenciaProdutoPorId(wishlistRequest.getIdProduto())).thenReturn(true);
        Mockito.when(consultarCliente.consultaExistenciaClientePorId(wishlistRequest.getIdCliente())).thenReturn(true);
        Mockito.when(wishlistDS.buscarWishlistPorClienteId(wishlistRequest.getIdCliente())).thenReturn(Optional.empty());

        org.assertj.core.api.Assertions.assertThatThrownBy(
                        () -> removerProdutoNaWishlistCliente.removeDaWishlistPorId(wishlistRequest.getIdCliente(), wishlistRequest.getIdProduto()))
                .isInstanceOf(RegistroNaoEncontradoException.class)
                .hasMessage("Wishlist não existe.");
    }

}