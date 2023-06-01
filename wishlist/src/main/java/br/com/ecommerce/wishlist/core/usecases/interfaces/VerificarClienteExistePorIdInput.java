package br.com.ecommerce.wishlist.core.usecases.interfaces;

public interface VerificarClienteExistePorIdInput {

    boolean consultaExistenciaClientePorId(String id);
}
