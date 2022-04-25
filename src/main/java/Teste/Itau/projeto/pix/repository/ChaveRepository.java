package Teste.Itau.projeto.pix.repository;

import Teste.Itau.projeto.pix.model.ChaveModel;
import Teste.Itau.projeto.pix.projection.ConsultaPadrao;
import Teste.Itau.projeto.pix.projection.ConsultaQtd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

// extends JPA p essa se comunicar com o banco passo a classe e o tipo do ID
public interface ChaveRepository extends JpaRepository<ChaveModel, String> {

    List<ChaveModel> findAllByvalorchave(String valorChave);

    //List<ChaveModel> findAllByNumAgenciaAndNumConta(Integer numagencia, Integer numconta);

    @Query(nativeQuery = true, value = "select id as quantidade from tb_chave_pix" +
            " where num_agencia = :agencia and num_conta = :conta and tipo_cliente =:tipocliente")
    List<ConsultaQtd>findByAgenciaAndConta(String agencia, String conta, String tipocliente);

    @Query(nativeQuery = true, value = "select id, tipo_chave as tipochave, valor_chave as valorchave, tipo_conta as tipoconta, " +
            "num_agencia as numagencia, num_conta as numconta, nome, sobrenome, data_inclusao as datainclusao, data_inativacao as datainativacao" +
            " from tb_chave_pix where tipo_chave = :tipochave")
    List<ConsultaPadrao>findByTipoChave(String tipochave );

    @Query(nativeQuery = true, value = "select id, tipo_chave as tipochave, valor_chave as valorchave, tipo_conta as tipoconta, " +
            "num_agencia as numagencia, num_conta as numconta, nome, sobrenome, data_inclusao as datainclusao, data_inativacao as datainativacao" +
            " from tb_chave_pix where num_agencia =:agencia and num_conta = :conta")
    List<ConsultaPadrao>findByAgenciaConta(String agencia, String conta );

    @Query(nativeQuery = true, value = "select id, tipo_chave as tipochave, valor_chave as valorchave, tipo_conta as tipoconta, " +
            "num_agencia as numagencia, num_conta as numconta, nome, sobrenome, data_inclusao as datainclusao, data_inativacao as datainativacao" +
            " from tb_chave_pix where nome = :nome")
    List<ConsultaPadrao>findByNome(String nome );

}
