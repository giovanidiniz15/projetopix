package Teste.Itau.projeto.pix.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@JsonFormat
public class DeletarResponse {

    private String codigo;
    private String mensagem;

}
