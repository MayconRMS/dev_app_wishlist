package br.com.ecommerce.wishlist.gateway.rest.controller;

import br.com.ecommerce.wishlist.AbstractContainerBaseTest;
import br.com.ecommerce.wishlist.Integration;
import br.com.ecommerce.wishlist.core.request.AdicionarProdutoWishlistClienteRequest;
import br.com.ecommerce.wishlist.core.response.*;
import br.com.ecommerce.wishlist.exception.OperacaoInvalidaException;
import br.com.ecommerce.wishlist.exception.RegistroNaoEncontradoException;
import net.jcip.annotations.NotThreadSafe;
import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@NotThreadSafe
@SpringBootTest
@Category(Integration.class)
class WishlistControllerIntegrationTest extends AbstractContainerBaseTest {

    @Autowired
    private WishlistController wishlistController;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void init(){
        inicializaWishlist(mongoTemplate);
        inicializaProduto(mongoTemplate);
        inicializaCliente(mongoTemplate);
    }

    @AfterEach
    void finish(){
        dropWishlist(mongoTemplate);
        dropProduto(mongoTemplate);
        dropCliente(mongoTemplate);
    }

    @Test
    @DisplayName("Dado que o produto não existe, deve lancar exception.")
    void deveLancarExceptionQuandoProdutoNaoExiste() {
        AdicionarProdutoWishlistClienteRequest request = new AdicionarProdutoWishlistClienteRequest("1", "23");

        org.assertj.core.api.Assertions.assertThatThrownBy(
                        () -> wishlistController.adicionarProdutoNaWishlistCliente(request))
                .isInstanceOf(RegistroNaoEncontradoException.class)
                .hasMessage("Produto não existe.");

    }

    @Test
    @DisplayName("Dado que a wishlist existe e está cheia, deve lancar exception.")
    void deveLancarExceptionQuandoWishlistEstiverCheia() {
        AdicionarProdutoWishlistClienteRequest request = new AdicionarProdutoWishlistClienteRequest("1", "21");

        org.assertj.core.api.Assertions.assertThatThrownBy(
                        () -> wishlistController.adicionarProdutoNaWishlistCliente(request))
                .isInstanceOf(OperacaoInvalidaException.class)
                .hasMessage("Wishlist já está cheia.");

    }

    @Test
    @DisplayName("Dado que a wishlist existe e não está cheia, deve adicionar um produto.")
    void deveAdicionarProdutoNaWishlis() {
        AdicionarProdutoWishlistClienteRequest request = new AdicionarProdutoWishlistClienteRequest( "2", "1");

        AdicionarProdutoWishlistResponse response = wishlistController.adicionarProdutoNaWishlistCliente(request);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getData());
        Assertions.assertEquals(1, response.getData().getIdProdutos().size());
    }

    @Test
    @DisplayName("Dado que a wishlist existe mas tem produtos, retornar a lista de produtos.")
    void deveConsultarProdutosDaWishlist() {
        ConsultarProdutoResponseList response = wishlistController.listaTodosProdutosdaWishListCliente("1");

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getData());
        Assertions.assertEquals(20, response.getData().size());
    }


    @Test
    @DisplayName("Dado que a wishlist existe e o produto faz parte dela, retornar verdadeiro.")
    void deveVerificarSeProdutosExisteNaWishlist() {
        VerificarProdutoExisteNaWishlistResponse response = wishlistController.verificarProdutoExisteNaWishlistCliente("1", "1");
        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.isData());

    }

    @Test
    @DisplayName("Dado que a wishtlist existe e o produto faz parte dela, deve remover o produto da wishlist.")
    void deveRemoverProdutoDaWishList() {
        wishlistController.removerProdutoDaWishlistCliente("1", "1");
        ConsultarProdutoResponseList response = wishlistController.listaTodosProdutosdaWishListCliente("1");

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getData());
        Assertions.assertEquals(19, response.getData().size());
    }
}
