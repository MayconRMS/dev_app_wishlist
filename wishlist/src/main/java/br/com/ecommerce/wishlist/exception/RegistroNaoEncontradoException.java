package br.com.ecommerce.wishlist.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RegistroNaoEncontradoException extends BusinessException {
    public RegistroNaoEncontradoException(String message) {
        super(message);
    }
}
