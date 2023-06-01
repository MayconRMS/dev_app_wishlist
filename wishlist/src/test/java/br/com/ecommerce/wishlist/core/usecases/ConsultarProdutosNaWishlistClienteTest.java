package br.com.ecommerce.wishlist.core.usecases;

import br.com.ecommerce.wishlist.core.response.VerificarProdutoExisteNaWishlistResponse;
import br.com.ecommerce.wishlist.core.usecases.interfaces.ConsultarProdutosNaWishlistClienteInput;
import br.com.ecommerce.wishlist.core.usecases.interfaces.VerificarClienteExistePorIdInput;
import br.com.ecommerce.wishlist.exception.DadosFaltandoException;
import br.com.ecommerce.wishlist.exception.RegistroNaoEncontradoException;
import br.com.ecommerce.wishlist.gateway.database.WishlistDS;
import br.com.ecommerce.wishlist.gateway.database.model.WishlistDataModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ConsultarProdutosNaWishlistClienteTest {

    ConsultarProdutosNaWishlistClienteInput consultarProdutosNaWishlistCliente;

    @Mock
    private WishlistDS wishlistDS;
    @Mock
    private VerificarClienteExistePorIdInput consultarCliente;

    @BeforeEach
    void init() {
        consultarProdutosNaWishlistCliente = new ConsultarProdutosNaWishlistCliente(wishlistDS, consultarCliente);
    }

    @Test
    @DisplayName("Consultar se existe produto na wishlist do cliente")
    void deveAdicionarProdutoAWishlist() {
        var idCliente = "1";
        var idProduto = "1";
        WishlistDataModel wishlistDataModel = new WishlistDataModel(null, "lista", idCliente, List.of());

        Mockito.when(consultarCliente.consultaExistenciaClientePorId(idCliente)).thenReturn(true);
        Mockito.when(wishlistDS.buscarWishlistPorClienteId(idCliente)).thenReturn(Optional.of(wishlistDataModel));

        VerificarProdutoExisteNaWishlistResponse reponse = consultarProdutosNaWishlistCliente.verificaProdutoExisteNaWishlistCliente(idCliente, idProduto);
        Assertions.assertNotNull(reponse);
    }

    @Test
    @DisplayName("Dado que o id Cliente não existe, deve lançar exception.")
    void naoDeveAdicionarProdutoAWishlistQuandoNaoExisteIdCliente() {
        org.assertj.core.api.Assertions.assertThatThrownBy(
                        () -> consultarProdutosNaWishlistCliente.verificaProdutoExisteNaWishlistCliente(null, "1"))
                .isInstanceOf(DadosFaltandoException.class)
                .hasMessage("É obrigatorio informar a id do cliente.");
    }

    @Test
    @DisplayName("Dado que o Cliente não existe, deve lançar exception.")
    void naoDeveAdicionarProdutoAWishlistQuandoNaoExisteCliente() {
        var idCliente = "1";
        var idProduto = "1";

        Mockito.when(consultarCliente.consultaExistenciaClientePorId(idCliente)).thenReturn(false);

        org.assertj.core.api.Assertions.assertThatThrownBy(
                        () -> consultarProdutosNaWishlistCliente.verificaProdutoExisteNaWishlistCliente(idCliente, idProduto))
                .isInstanceOf(RegistroNaoEncontradoException.class)
                .hasMessage("Cliente não encontrado.");
    }

}