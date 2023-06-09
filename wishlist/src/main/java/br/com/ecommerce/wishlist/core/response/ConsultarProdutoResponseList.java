package br.com.ecommerce.wishlist.core.response;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ConsultarProdutoResponseList extends DefaultResponse {

    private List<Data> data = new ArrayList<>();

    @Getter
    public class Data {
        private String id;
        private String nome;
    }

    public ConsultarProdutoResponseList(String id, String nome){
        this.add(id, nome);
    }

    public void add(String id, String nome){
        var dados = new Data();
        dados.id = id;
        dados.nome = nome;
        this.data.add(dados);
    }
}
