package br.com.ecommerce.wishlist.core.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class AdicionarProdutoWishlistClienteRequest {

    @NotBlank
    private String idCliente;
    @NotBlank
    private String idProduto;

}
