package br.com.clash_utilities.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContaInfoDTO {
    private String nome;
    private String tag;
    private String telefone;
    private boolean principal;
    private String contaPrincipal;
}
