package Teste.Itau.projeto.pix.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.util.UUID;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@JsonFormat
public class AlterarResponse {

    private String id;

    private String tipochave;

    private String valorchave;

    private String tipocliente;

    private String tipoconta;

    private Integer numconta;

    private Integer numagencia;

    private String nome;

    private String sobrenome;

    private String datainclusao;


}
