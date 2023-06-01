package br.com.ecommerce.wishlist.core.usecases;

import br.com.ecommerce.wishlist.core.request.AdicionarProdutoWishlistClienteRequest;
import br.com.ecommerce.wishlist.core.response.ConsultarProdutoResponseList;
import br.com.ecommerce.wishlist.core.usecases.interfaces.TodosProdutosNaWishlistClienteInput;
import br.com.ecommerce.wishlist.core.usecases.interfaces.VerificarClienteExistePorIdInput;
import br.com.ecommerce.wishlist.exception.DadosFaltandoException;
import br.com.ecommerce.wishlist.exception.RegistroNaoEncontradoException;
import br.com.ecommerce.wishlist.gateway.database.ProdutoDS;
import br.com.ecommerce.wishlist.gateway.database.WishlistDS;
import br.com.ecommerce.wishlist.gateway.database.model.ProdutoDataModel;
import br.com.ecommerce.wishlist.gateway.database.model.WishlistDataModel;
import org.junit.jupiter.api.Assertions;
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

import static org.mockito.ArgumentMatchers.anyList;

@ExtendWith(MockitoExtension.class)
class TodosProdutosNaWishlistClienteTest {

    TodosProdutosNaWishlistClienteInput todosProdutosNaWishlistCliente;

    @Mock
    private ProdutoDS produtoDS;
    @Mock
    private WishlistDS wishlistDS;
    @Mock
    private VerificarClienteExistePorIdInput consultarCliente;

    @BeforeEach
    void init() {
        todosProdutosNaWishlistCliente = new TodosProdutosNaWishlistCliente(produtoDS, wishlistDS, consultarCliente);
    }

    @Test
    @DisplayName("Deve listar os produtos do cliente da sua wishlist")
    void deveListarTodosProdutosWishlistCliente() {
        AdicionarProdutoWishlistClienteRequest wishlistRequest = new AdicionarProdutoWishlistClienteRequest("1", "3");
        List<String> produtos = new ArrayList<>();
        produtos.add("1");
        produtos.add("4");
        produtos.add("5");
        WishlistDataModel wishlistDataModel = new WishlistDataModel(null, "lista", wishlistRequest.getIdCliente(), produtos);

        Mockito.when(consultarCliente.consultaExistenciaClientePorId(wishlistRequest.getIdCliente())).thenReturn(true);
        Mockito.when(wishlistDS.buscarWishlistPorClienteId(wishlistRequest.getIdCliente())).thenReturn(Optional.of(wishlistDataModel));

        List<ProdutoDataModel> produtosModel = new ArrayList<>();
        produtosModel.add(new ProdutoDataModel("1","Prod1"));
        produtosModel.add(new ProdutoDataModel("4","Prod4"));
        produtosModel.add(new ProdutoDataModel("5","Prod5"));
        Mockito.when(produtoDS.buscarPorIdIn(anyList())).thenReturn(produtosModel);

        ConsultarProdutoResponseList wishlistResponse = todosProdutosNaWishlistCliente.listarTodosProdutosExisteNaWishlistCliente(wishlistRequest.getIdCliente());

        Assertions.assertNotNull(wishlistResponse);
        Assertions.assertNotNull(wishlistResponse.getData());
        Assertions.assertEquals(3, wishlistResponse.getData().size());
    }

    @Test
    @DisplayName("Dado que o id Cliente não existe, deve lançar exception.")
    void naoDeveAdicionarProdutoAWishlistQuandoNaoExisteCliente() {
        AdicionarProdutoWishlistClienteRequest wishlistRequest = new AdicionarProdutoWishlistClienteRequest("1", "3");
        wishlistRequest.setIdCliente(null);

        org.assertj.core.api.Assertions.assertThatThrownBy(
                        () -> todosProdutosNaWishlistCliente.listarTodosProdutosExisteNaWishlistCliente(wishlistRequest.getIdCliente()))
                .isInstanceOf(DadosFaltandoException.class)
                .hasMessage("É obrigatorio informar a id do cliente para listar os produtos.");
    }

    @Test
    @DisplayName("Dado que o Cliente não existe, deve lançar exception.")
    void naoDeveAdicionarProdutoAWishlistQuandoNaoExisteIdCliente() {
        AdicionarProdutoWishlistClienteRequest wishlistRequest = new AdicionarProdutoWishlistClienteRequest("1", "3");
        Mockito.when(consultarCliente.consultaExistenciaClientePorId(wishlistRequest.getIdCliente())).thenReturn(false);

        org.assertj.core.api.Assertions.assertThatThrownBy(
                        () -> todosProdutosNaWishlistCliente.listarTodosProdutosExisteNaWishlistCliente(wishlistRequest.getIdCliente()))
                .isInstanceOf(RegistroNaoEncontradoException.class)
                .hasMessage("Cliente não encontrado.");
    }

    @Test
    @DisplayName("Dado que a lista não exista")
    void naoDeveAdicionarProdutoAWishlistQuandoListaCheia() {
        AdicionarProdutoWishlistClienteRequest wishlistRequest = new AdicionarProdutoWishlistClienteRequest("1", "3");

        Mockito.when(consultarCliente.consultaExistenciaClientePorId(wishlistRequest.getIdCliente())).thenReturn(true);
        Mockito.when(wishlistDS.buscarWishlistPorClienteId(wishlistRequest.getIdCliente())).thenReturn(Optional.empty());

        org.assertj.core.api.Assertions.assertThatThrownBy(
                        () -> todosProdutosNaWishlistCliente.listarTodosProdutosExisteNaWishlistCliente(wishlistRequest.getIdCliente()))
                .isInstanceOf(RegistroNaoEncontradoException.class)
                .hasMessage("Wishlist não existe.");
    }
}