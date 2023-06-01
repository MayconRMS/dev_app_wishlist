package br.com.ecommerce.wishlist.gateway.rest.controller;

import br.com.ecommerce.wishlist.core.request.AdicionarProdutoWishlistClienteRequest;
import br.com.ecommerce.wishlist.core.response.*;
import br.com.ecommerce.wishlist.core.usecases.interfaces.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Operações da Wishlist Cliente")
@RequiredArgsConstructor
@RestController("/wishlists")
public class WishlistController {

    private final AdicionarProdutoNaWishlistClienteInput adicionarProdutoNaWishlistClienteInput;
    private final RemoverProdutoNaWishlistClienteInput removerProdutoNaWishlistClienteInput;
    private final TodosProdutosNaWishlistClienteInput todosProdutosNaWishlistClienteInput;
    private final ConsultarProdutosNaWishlistClienteInput consultarProdutosNaWishlistClienteInput;
    private final ConsultarTodasWishlistInput consultarTodasWishlistInput;

    @Operation(summary = "Adiciona um produto a wishlist do cliente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto adicionado com sucesso.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AdicionarProdutoWishlistResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Produto ou wishlist não encontrados.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DefaultResponse.class))}),
            @ApiResponse(responseCode = "409", description = "Wishlist cheia.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DefaultResponse.class))})
    })
    @PostMapping("/produto")
    public AdicionarProdutoWishlistResponse adicionarProdutoNaWishlistCliente(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados para um produto a uma wishlist.", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AdicionarProdutoWishlistClienteRequest.class)))
            @RequestBody AdicionarProdutoWishlistClienteRequest request) {
        return adicionarProdutoNaWishlistClienteInput.adicionar(request);
    }

    @Operation(summary = "Remove produto da wishlist do cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Remoção do produto realizada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Se a id do cliente ou do produto não forem informadas.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DefaultResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Se o produto não for encontrado na wishlist, ou a wishlist não existe , ou a cliente não existe.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DefaultResponse.class))})
    })
    @DeleteMapping("/produto/{idProduto}/cliente/{idCliente}")
    public void removerProdutoDaWishlistCliente(@Parameter(description = "Id do cliente.") @PathVariable String idCliente,
                                                @Parameter(description = "Id do produto.") @PathVariable String idProduto) {
        removerProdutoNaWishlistClienteInput.removeDaWishlistPorId(idCliente, idProduto);
    }

    @Operation(summary = "Listar todos produtos da wishlist do cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ConsultarWishlistResponseList.class))}),
            @ApiResponse(responseCode = "400", description = "Se cliente não existe.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DefaultResponse.class))})
    })
    @GetMapping("/produto/cliente/{idCliente}")
    public ConsultarProdutoResponseList listaTodosProdutosdaWishListCliente(@Parameter(description = "Id do cliente.") @PathVariable String idCliente) {
        return todosProdutosNaWishlistClienteInput.listarTodosProdutosExisteNaWishlistCliente(idCliente);
    }

    @Operation(summary = "Verifica se um produto existe na wishlist do cliente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = VerificarProdutoExisteNaWishlistResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Se id do produto ou do cliente não forem informados.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DefaultResponse.class))})
    })
    @GetMapping("/produto/{idProduto}/cliente/{idCliente}")
    public VerificarProdutoExisteNaWishlistResponse verificarProdutoExisteNaWishlistCliente(@Parameter(description = "Id do cliente.") @PathVariable String idCliente,
                                                                                            @Parameter(description = "Id do produto.") @PathVariable String idProduto) {
        return consultarProdutosNaWishlistClienteInput.verificaProdutoExisteNaWishlistCliente(idCliente, idProduto);
    }

    @Operation(summary = "Buscar todas as wishlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ConsultarWishlistResponseList.class))}),
            @ApiResponse(responseCode = "400", description = "Se wishlist for null.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DefaultResponse.class))})
    })
    @GetMapping("/wishlists")
    public ConsultarWishlistResponseList consultaTodasWishlist() {
        return consultarTodasWishlistInput.consulta();
    }

}
