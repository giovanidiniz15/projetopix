package Teste.Itau.projeto.pix.service;

import Teste.Itau.projeto.pix.model.ChaveModel;
import Teste.Itau.projeto.pix.repository.ChaveRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ChavaServiceUnitTest {

    @InjectMocks
    private ChaveService chaveService;

    @Mock
    ChaveRepository chaveRepository;

    @Test
    public void deveRetornarChavePorId(){
        ChaveModel chaveModel = new ChaveModel();
        when(chaveRepository.findById(any())).thenReturn(Optional.of(chaveModel));
        chaveService.listarPorId(any());
    }

    @Test
    public void deveListarPorTipoDeChave(){

        List lista = new ArrayList();
        lista.add(1);
        when(chaveRepository.findByAgenciaConta(anyString(),anyString())).thenReturn(lista);
        chaveService.listarPorAgenciaConta(anyString(),anyString());
    }

    @Test
    public void deveListarPorNome(){
        List lista = new ArrayList();
        lista.add(1);
        when(chaveRepository.findByNome(anyString())).thenReturn(lista);
        chaveService.listarPorNome(anyString());
    }

    @Test
    public void deveListarPorTipoChave(){
        List lista = new ArrayList();
        lista.add(1);
        when(chaveRepository.findByTipoChave(anyString())).thenReturn(lista);
        chaveService.listarPorTipoChave(anyString());
    }

    @Test
    public void quandoAlterarChave(){
        ChaveModel chaveModel = new ChaveModel();
        chaveModel.setNome("Giovani");
        chaveModel.setSobrenome("Diniz");
        chaveModel.setTipochave("email");
        chaveModel.setValorchave("giovani.diniz@live.com");
        chaveModel.setTipoconta("corrente");
        chaveModel.setTipocliente("fisica");
        chaveModel.setDatainclusao("23/04/2022");
        chaveModel.setNumagencia(1001);
        chaveModel.setNumconta(77127);

        when(chaveRepository.findById(anyString())).thenReturn(Optional.of(chaveModel));
        chaveService.alterarChave(anyString(),chaveModel);
    }

    @Test
    public void quandoDeletarChave(){
        ChaveModel chaveModel = new ChaveModel();
        when(chaveRepository.findById(anyString())).thenReturn(Optional.of(chaveModel));
        chaveService.deletarChave(anyString());
    }

    @Test
    public void quandoCadastrarChaveNovaCpf(){
        ChaveModel chaveModel = new ChaveModel();
        chaveModel.setNome("Giovani");
        chaveModel.setSobrenome("Diniz");
        chaveModel.setTipochave("cpf");
        chaveModel.setValorchave("41747012882");
        chaveModel.setTipoconta("corrente");
        chaveModel.setTipocliente("fisica");
        chaveModel.setDatainclusao("23/04/2022");
        chaveModel.setNumagencia(1001);
        chaveModel.setNumconta(77127);

        List lista = new ArrayList();
        lista.add(1);
        chaveService.cadastrarChave(chaveModel);
    }

    @Test
    public void quandoCadastrarChaveNovaCnpj(){
        ChaveModel chaveModel = new ChaveModel();
        chaveModel.setNome("Giovani");
        chaveModel.setSobrenome("Diniz");
        chaveModel.setTipochave("cnpj");
        chaveModel.setValorchave("79107423000165");
        chaveModel.setTipoconta("corrente");
        chaveModel.setTipocliente("juridica");
        chaveModel.setDatainclusao("23/04/2022");
        chaveModel.setNumagencia(1001);
        chaveModel.setNumconta(77127);

        List lista = new ArrayList();
        lista.add(1);
        chaveService.cadastrarChave(chaveModel);
    }

    @Test
    public void quandoCadastrarChaveNovaAleatoria(){

        ChaveModel chaveModel = new ChaveModel();
        chaveModel.setNome("Giovani");
        chaveModel.setSobrenome("Diniz");
        chaveModel.setTipochave("aleatoria");
        chaveModel.setValorchave("");
        chaveModel.setTipoconta("corrente");
        chaveModel.setTipocliente("fisica");
        chaveModel.setDatainclusao("23/04/2022");
        chaveModel.setNumagencia(1001);
        chaveModel.setNumconta(77127);

        List lista = new ArrayList();
        lista.add(1);
        chaveService.cadastrarChave(chaveModel);
    }

    @Test
    public void quandoCadastrarChaveNovaEmail(){
        ChaveModel chaveModel = new ChaveModel();
        chaveModel.setNome("Giovani");
        chaveModel.setSobrenome("Diniz");
        chaveModel.setTipochave("email");
        chaveModel.setValorchave("giovani.diniz@live.com");
        chaveModel.setTipoconta("corrente");
        chaveModel.setTipocliente("fisica");
        chaveModel.setDatainclusao("23/04/2022");
        chaveModel.setNumagencia(1001);
        chaveModel.setNumconta(77127);

        List lista = new ArrayList();
        lista.add(1);
        chaveService.cadastrarChave(chaveModel);
    }

}
