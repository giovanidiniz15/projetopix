package Teste.Itau.projeto.pix.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor @NoArgsConstructor
public class ConsultaQtdDto {

    @Getter @Setter
    private Integer quantidade;

    public ConsultaQtdDto(ConsultaQtdDto projection) {
        quantidade = projection.getQuantidade();
    }
}
