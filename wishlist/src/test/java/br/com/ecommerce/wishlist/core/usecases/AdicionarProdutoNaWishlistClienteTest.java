package br.com.ecommerce.wishlist.core.usecases;

import br.com.ecommerce.wishlist.core.entity.factory.WishlistFactory;
import br.com.ecommerce.wishlist.core.entity.factory.WishlistFactoryImpl;
import br.com.ecommerce.wishlist.core.request.AdicionarProdutoWishlistClienteRequest;
import br.com.ecommerce.wishlist.core.response.AdicionarProdutoWishlistResponse;
import br.com.ecommerce.wishlist.core.usecases.interfaces.AdicionarProdutoNaWishlistClienteInput;
import br.com.ecommerce.wishlist.core.usecases.interfaces.VerificarClienteExistePorIdInput;
import br.com.ecommerce.wishlist.exception.DadosFaltandoException;
import br.com.ecommerce.wishlist.exception.OperacaoInvalidaException;
import br.com.ecommerce.wishlist.exception.RegistroNaoEncontradoException;
import br.com.ecommerce.wishlist.gateway.database.WishlistDS;
import br.com.ecommerce.wishlist.gateway.database.model.WishlistDataModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class AdicionarProdutoNaWishlistClienteTest {

    AdicionarProdutoNaWishlistClienteInput adicionarProdutoNaWishlistCliente;

    @Mock
    private WishlistDS wishlistDS;
    @Mock
    private VerificarProdutoExistePorId consultarProduto;
    private WishlistFactory wishlistFactory = new WishlistFactoryImpl();
    @Mock
    private VerificarClienteExistePorIdInput consultarCliente;

    @BeforeEach
    void init() {
        adicionarProdutoNaWishlistCliente = new AdicionarProdutoNaWishlistCliente(wishlistDS, consultarCliente, consultarProduto, wishlistFactory);
    }

    @Test
    @DisplayName("Dado que cliente e o produto existam,e a wishlis não esteja cheia, deve adicionar o produto a whishlist")
    void deveAdicionarProdutoAWishlist() {
        AdicionarProdutoWishlistClienteRequest wishlistRequest = new AdicionarProdutoWishlistClienteRequest("1", "3");
        List<String> produtos = new ArrayList<>();
        produtos.add("4");
        produtos.add("5");
        WishlistDataModel wishlistDataModel = new WishlistDataModel(null, "lista",wishlistRequest.getIdCliente(), produtos);

        Mockito.when(consultarProduto.verificaExistenciaProdutoPorId("3")).thenReturn(true);
        Mockito.when(consultarCliente.consultaExistenciaClientePorId(wishlistRequest.getIdCliente())).thenReturn(true);
        Mockito.when(wishlistDS.buscarWishlistPorClienteId(wishlistRequest.getIdCliente())).thenReturn(Optional.of(wishlistDataModel));
        Mockito.when(wishlistDS.save(any(), any())).thenReturn(wishlistDataModel);

        AdicionarProdutoWishlistResponse wishlistResponse = adicionarProdutoNaWishlistCliente.adicionar(wishlistRequest);

        Mockito.verify(wishlistDS).save(any(), any());
        Assertions.assertNotNull(wishlistResponse);
        Assertions.assertNotNull(wishlistResponse.getData().getIdProdutos());
        Assertions.assertEquals(3, wishlistResponse.getData().getIdProdutos().size());
    }

    @Test
    @DisplayName("Dado que o produto não existe deve lançar exception.")
    void naoDeveAdicionarProdutoAWishlistQuandoNaoExisteProduto() {
        AdicionarProdutoWishlistClienteRequest wishlistRequest = new AdicionarProdutoWishlistClienteRequest("1", "3");
        Mockito.when(consultarProduto.verificaExistenciaProdutoPorId("3")).thenReturn(false);

        org.assertj.core.api.Assertions.assertThatThrownBy(
                        () -> adicionarProdutoNaWishlistCliente.adicionar(wishlistRequest))
                .isInstanceOf(RegistroNaoEncontradoException.class)
                .hasMessage("Produto não existe.");

    }

    @Test
    @DisplayName("Dado que o id Cliente não existe, deve lançar exception.")
    void naoDeveAdicionarProdutoAWishlistQuandoNaoExisteCliente() {
        AdicionarProdutoWishlistClienteRequest wishlistRequest = new AdicionarProdutoWishlistClienteRequest("1", "3");
        wishlistRequest.setIdCliente(null);
        Mockito.when(consultarProduto.verificaExistenciaProdutoPorId("3")).thenReturn(true);

        org.assertj.core.api.Assertions.assertThatThrownBy(
                        () -> adicionarProdutoNaWishlistCliente.adicionar(wishlistRequest))
                .isInstanceOf(DadosFaltandoException.class)
                .hasMessage("É obrigatorio informar a id do cliente para adicionar um produto.");
    }

    @Test
    @DisplayName("Dado que o Cliente não existe, deve lançar exception.")
    void naoDeveAdicionarProdutoAWishlistQuandoNaoExisteIdCliente() {
        AdicionarProdutoWishlistClienteRequest wishlistRequest = new AdicionarProdutoWishlistClienteRequest("1", "3");
        Mockito.when(consultarProduto.verificaExistenciaProdutoPorId("3")).thenReturn(true);
        Mockito.when(consultarCliente.consultaExistenciaClientePorId(wishlistRequest.getIdCliente())).thenReturn(false);

        org.assertj.core.api.Assertions.assertThatThrownBy(
                        () -> adicionarProdutoNaWishlistCliente.adicionar(wishlistRequest))
                .isInstanceOf(RegistroNaoEncontradoException.class)
                .hasMessage("Cliente não encontrado.");
    }

    @Test
    @DisplayName("Dado que a lista esteja cheia deve lançar exception.")
    void naoDeveAdicionarProdutoAWishlistQuandoListaCheia() {
        AdicionarProdutoWishlistClienteRequest wishlistRequest = new AdicionarProdutoWishlistClienteRequest("1", "3");
        List<String> produtos = IntStream.range(0, 20)
                .mapToObj(String::valueOf).collect(Collectors.toList());
        WishlistDataModel wishlistDataModel = new WishlistDataModel(null, "lista",wishlistRequest.getIdCliente(), produtos);

        Mockito.when(consultarProduto.verificaExistenciaProdutoPorId("3")).thenReturn(true);
        Mockito.when(consultarCliente.consultaExistenciaClientePorId(wishlistRequest.getIdCliente())).thenReturn(true);
        Mockito.when(wishlistDS.buscarWishlistPorClienteId(wishlistRequest.getIdCliente())).thenReturn(Optional.of(wishlistDataModel));

        org.assertj.core.api.Assertions.assertThatThrownBy(
                        () -> adicionarProdutoNaWishlistCliente.adicionar(wishlistRequest))
                .isInstanceOf(OperacaoInvalidaException.class)
                .hasMessage("Wishlist já está cheia.");
    }

    @Test
    @DisplayName("Dado que produto já esta na wishliste, deve lançar exception.")
    void naoDeveAdicionarProdutoAWishlistQuandoProdutoJáNaWishList() {
        AdicionarProdutoWishlistClienteRequest wishlistRequest = new AdicionarProdutoWishlistClienteRequest("1", "4");
        List<String> produtos = new ArrayList<>();
        produtos.add("4");
        produtos.add("5");

        WishlistDataModel wishlistDataModel = new WishlistDataModel(null, "lista",wishlistRequest.getIdCliente(), produtos);
        Mockito.when(consultarProduto.verificaExistenciaProdutoPorId("4")).thenReturn(true);
        Mockito.when(consultarCliente.consultaExistenciaClientePorId(wishlistRequest.getIdCliente())).thenReturn(true);
        Mockito.when(wishlistDS.buscarWishlistPorClienteId(wishlistRequest.getIdCliente())).thenReturn(Optional.of(wishlistDataModel));
        org.assertj.core.api.Assertions.assertThatThrownBy(
                        () -> adicionarProdutoNaWishlistCliente.adicionar(wishlistRequest))
                .isInstanceOf(OperacaoInvalidaException.class)
                .hasMessage("Produto já está na wishlist");
    }

}