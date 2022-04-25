package Teste.Itau.projeto.pix.controller;

import Teste.Itau.projeto.pix.model.AlterarResponse;
import Teste.Itau.projeto.pix.model.CadastrarResponse;
import Teste.Itau.projeto.pix.model.ChaveModel;
import Teste.Itau.projeto.pix.model.DeletarResponse;
import Teste.Itau.projeto.pix.model.error.ErrorResponse;
import Teste.Itau.projeto.pix.projection.ConsultaPadrao;
import Teste.Itau.projeto.pix.service.ChaveService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@Slf4j //Pra fazer anotacao
@RestController //Pra avisar que essa classe é um controller
@AllArgsConstructor //Criar os contrutores automaticamente
public class ChaveController {

    @Autowired
    ChaveService chaveService;

    // CADASTRAR CHAVE PIX
    @PostMapping(path = "/api/cadastrochave")
    public ResponseEntity cadastroChave(@RequestBody @Valid ChaveModel chaveModel) {
        try {
            final ChaveModel chaveModelResponse = chaveService.cadastrarChave(chaveModel);
            CadastrarResponse cadastrarResponse = new CadastrarResponse();
            cadastrarResponse.setId(chaveModelResponse.getId());

            return new ResponseEntity(cadastrarResponse, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            final ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setCode(e.getStatus().toString());
            errorResponse.setMessage(e.getReason());

            return new ResponseEntity(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    //ALTERAR CHAVE PIX
    @PutMapping(path = "/api/alterarchave/{id}")
    public ResponseEntity alterarChave(@PathVariable String id, @RequestBody @Valid ChaveModel chaveModel) {
        try {
            return new ResponseEntity(chaveService.alterarChave(id, chaveModel), HttpStatus.OK);
        } catch (ResponseStatusException e) {
            final ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setCode(e.getStatus().toString());
            errorResponse.setMessage(e.getReason());

            return new ResponseEntity(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    //DELETAR CHAVE PIX
    @PutMapping(path = "/api/deletarchave/{id}")
    public ResponseEntity deletarChave(@PathVariable String id) {
        DeletarResponse deletarResponse = new DeletarResponse();

        if (!chaveService.deletarChave(id)) {
            deletarResponse.setCodigo(HttpStatus.UNPROCESSABLE_ENTITY.toString());
            deletarResponse.setMensagem("Chave inativada ou nao encontrada!");

            return new ResponseEntity(deletarResponse, HttpStatus.UNPROCESSABLE_ENTITY);
        } else {
            deletarResponse.setCodigo(HttpStatus.OK.toString());
            deletarResponse.setMensagem("Chave inativada com sucesso id: " + id);

            return new ResponseEntity(deletarResponse, HttpStatus.OK);
        }
    }

    //LISTAR POR ID
    @GetMapping(path = "/api/listarporid/{id}")
    public ChaveModel listarPorId(@PathVariable String id) {
        return chaveService.listarPorId(id);
    }

    //LISTAR POR TIPO DE CHAVE
    @GetMapping(path = "/api/listarportipochave/{tipo}")
    public List<ConsultaPadrao> listarPorTipo(@PathVariable String tipo) {
        return chaveService.listarPorTipoChave(tipo);
    }

    //LISTAR CHAVE POR AGENCIA E CONTA
    @GetMapping(path = "/api/listarAgenciaConta/{agencia}/{conta}")
    public List<ConsultaPadrao> listarPorAgenciaConta(@PathVariable String agencia, @PathVariable String conta) {
        return chaveService.listarPorAgenciaConta(agencia, conta);
    }

    //LISTAR CHAVE POR NOME
    @GetMapping(path = "/api/listarpornome/{nome}")
    public List<ConsultaPadrao> listarPorNome(@PathVariable String nome) {
        return chaveService.listarPorNome(nome);
    }


}
