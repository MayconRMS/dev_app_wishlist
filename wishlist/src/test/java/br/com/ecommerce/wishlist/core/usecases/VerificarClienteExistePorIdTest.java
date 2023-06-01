package br.com.ecommerce.wishlist.core.usecases;

import br.com.ecommerce.wishlist.core.usecases.VerificarClienteExistePorId;
import br.com.ecommerce.wishlist.gateway.database.ClienteDS;
import br.com.ecommerce.wishlist.gateway.database.model.ClienteDataModel;
import br.com.ecommerce.wishlist.core.usecases.interfaces.VerificarClienteExistePorIdInput;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class VerificarClienteExistePorIdTest {

    VerificarClienteExistePorIdInput consultarClientePorNomeLikeInput;

    @Mock
    private ClienteDS clienteDS;

    @BeforeEach
    void init(){
        consultarClientePorNomeLikeInput = new VerificarClienteExistePorId(clienteDS);
    }

    @Test
    @DisplayName("Dado que a id não é nula ou vazia e que o cliente com a id exista, deve retornar true.")
    void deveConsultar(){
        Mockito.when(clienteDS.buscaPorId("1")).thenReturn(Optional.of(new ClienteDataModel("1", "nome 1")));

        boolean retorno = consultarClientePorNomeLikeInput.consultaExistenciaClientePorId("1");

        Assertions.assertTrue(retorno);
    }
    @Test
    @DisplayName("Dado que a id não é nula ou vazia e que o cliente com a id não exista, deve retornar true.")
    void deveConsultarERetornaFalse(){
        Mockito.when(clienteDS.buscaPorId("1")).thenReturn(Optional.empty());
        boolean retorno = consultarClientePorNomeLikeInput.consultaExistenciaClientePorId("1");

        Assertions.assertFalse(retorno);
    }
    @Test
    @DisplayName("Dado que a id é nula , deve retornar false.")
    void deveConsultarERetornaFalseQuandoIdNula(){
        boolean retorno = consultarClientePorNomeLikeInput.consultaExistenciaClientePorId(null);

        Assertions.assertFalse(retorno);
    }
}
