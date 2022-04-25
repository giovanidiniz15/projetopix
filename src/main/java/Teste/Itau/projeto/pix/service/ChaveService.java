package Teste.Itau.projeto.pix.service;

import Teste.Itau.projeto.pix.model.AlterarResponse;
import Teste.Itau.projeto.pix.model.ChaveModel;
import Teste.Itau.projeto.pix.projection.ConsultaPadrao;
import Teste.Itau.projeto.pix.projection.ConsultaQtd;
import Teste.Itau.projeto.pix.repository.ChaveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class ChaveService {

    @Autowired
    ChaveRepository chaveRepository;

    //OK
    public ChaveModel cadastrarChave(ChaveModel chaveModel){

        chaveModel.setDatainclusao(LocalDateTime.now().toString());

        //Checar se a chave é aleatorio e se for gerar numero aleatorio
        if (chaveModel.getTipochave().equals("aleatoria")){
            chaveModel.setValorchave(UUID.randomUUID().toString());
        }

        if (validarCadastroChave(chaveModel)){
            return chaveRepository.save(chaveModel);
        }
        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Dados invalidos por favor confira! ");
    }

    //OK
    private boolean validarCadastroChave(ChaveModel chaveModel){
        String tipo_chave = chaveModel.getTipochave();
        String tipo_cliente = chaveModel.getTipocliente();

        if (tipo_chave.equals("cpf") && tipo_cliente.equals("juridico")){
            log.info("tipo cliente e tipo chave nao consiste");
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Cpf nao pode ser associado a cliente tp juridico ");
        }

        if (tipo_chave.equals("cnpj") && tipo_cliente.equals("fisica")){
            log.info("tipo cliente e tipo chave nao consiste");
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Cnpj nao pode ser associado a cliente tp fisica. ");
        }

        if (chaveModel.getValorchave().isEmpty()){ //isEmpety checa se é vazio ou preenchido com espaço
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Digite um valor valido para chave! ");
        }

        if (!validarChaveExistente(chaveModel.getValorchave())){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Chave já existe! ");
        }

        if (!validaTipoEValor(chaveModel)){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Tipo e/ou valor de chave não confere. ");
        }

        if (!validaConta(chaveModel)){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Conta invalida verifique! ");
        }

        if (!validaQtdChaves(chaveModel)){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Usuario já excedeu qts maximas permitidas!");
        }

        return true;

    }

    //OK
    private boolean validaConta(ChaveModel chaveModel){
        String nome = chaveModel.getNome();
        String tipo_conta = chaveModel.getTipoconta();
        String tipo_cliente = chaveModel.getTipocliente();
        Integer numagencia = chaveModel.getNumagencia();
        Integer numconta = chaveModel.getNumconta();

        //Para verificar se ta em branco ou so com espaços
        if (nome.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Nome invalido!");
        }

        if (tipo_conta.toLowerCase().equals("corrente") || tipo_conta.toLowerCase().equals("poupanca")) {
            log.info("É conta corrente ou poupança");
            if (tipo_cliente.toLowerCase().equals("fisica") || tipo_cliente.toLowerCase().equals("juridica")) {
                log.info("É cliente pessoa fisica ou juridica");
                if (numagencia > 0 && numagencia.toString().length() <= 4) {// falta verificar se codigo ag e conta é letra
                    log.info("Agencia > 0 e com <= 4 digitos");
                    if (numconta > 0 && numconta.toString().length() <= 8) {
                        log.info("Numero conta >0 e <= 8 digitos");
                        return true;
                    }
                }
            }
        }
        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Dados da conta inválido! ");

    }

    //OK
    private boolean validarChaveExistente(String valorChave){
        final List<ChaveModel> chavemodel = chaveRepository.findAllByvalorchave(valorChave);
        if (chavemodel.size() <= 0){
            return true;
        }
        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Chave já existe ");
    }

    //OK
    private boolean validaTipoEValor(ChaveModel chaveModel){
        String tipo_chave = chaveModel.getTipochave();
        String valor_chave = chaveModel.getValorchave();

        switch (tipo_chave.toLowerCase()){
            case "cpf":
                if (validaCpf(valor_chave)){
                    return true;
                }
                break;
            case "cnpj":
                if (validaCnpj(valor_chave)){
                    return true;
                }
                break;
            case "email":
                if (validaEmail(valor_chave)){
                    return true;
                }
                break;
            case "aleatoria":
                return true;
            default:
                return false;
        }
        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Tipo de chave nao confere com valor!");
    }

    //OK
    private boolean validaCpf(String CPF){

        // considera-se erro CPF's formados por uma sequencia de numeros iguais
        if (CPF.equals("00000000000") ||
                CPF.equals("11111111111") ||
                CPF.equals("22222222222") || CPF.equals("33333333333") ||
                CPF.equals("44444444444") || CPF.equals("55555555555") ||
                CPF.equals("66666666666") || CPF.equals("77777777777") ||
                CPF.equals("88888888888") || CPF.equals("99999999999") ||
                (CPF.length() != 11)){
            System.out.println("CPF Invalido!");
            return false;
        }



        char dig10, dig11;
        int sm, i, r, num, peso;

        // "try" - protege o codigo para eventuais erros de conversao de tipo (int)
        try {
            // Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 10;
            for (i=0; i<9; i++) {
                // converte o i-esimo caractere do CPF em um numero:
                // por exemplo, transforma o caractere '0' no inteiro 0
                // (48 eh a posicao de '0' na tabela ASCII)
                num = (int)(CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig10 = '0';
            else dig10 = (char)(r + 48); // converte no respectivo caractere numerico

            // Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 11;
            for(i=0; i<10; i++) {
                num = (int)(CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig11 = '0';
            else dig11 = (char)(r + 48);

            // Verifica se os digitos calculados conferem com os digitos informados.
            if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10)))
                return(true);
            else return(false);
        } catch (InputMismatchException erro) {
            return(false);
        }

    }

    //OK
    private boolean validaCnpj(String CNPJ){

        // considera-se erro CNPJ's formados por uma sequencia de numeros iguais
        if (CNPJ.equals("00000000000000") || CNPJ.equals("11111111111111") ||
                CNPJ.equals("22222222222222") || CNPJ.equals("33333333333333") ||
                CNPJ.equals("44444444444444") || CNPJ.equals("55555555555555") ||
                CNPJ.equals("66666666666666") || CNPJ.equals("77777777777777") ||
                CNPJ.equals("88888888888888") || CNPJ.equals("99999999999999") ||
                (CNPJ.length() != 14))
            return(false);

        char dig13, dig14;
        int sm, i, r, num, peso;

        // "try" - protege o código para eventuais erros de conversao de tipo (int)
        try {
        // Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 2;
            for (i=11; i>=0; i--) {
        // converte o i-ésimo caractere do CNPJ em um número:
        // por exemplo, transforma o caractere '0' no inteiro 0
        // (48 eh a posição de '0' na tabela ASCII)
                num = (int)(CNPJ.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10)
                    peso = 2;
            }

            r = sm % 11;
            if ((r == 0) || (r == 1))
                dig13 = '0';
            else dig13 = (char)((11-r) + 48);

        // Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 2;
            for (i=12; i>=0; i--) {
                num = (int)(CNPJ.charAt(i)- 48);
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10)
                    peso = 2;
            }

            r = sm % 11;
            if ((r == 0) || (r == 1))
                dig14 = '0';
            else dig14 = (char)((11-r) + 48);

        // Verifica se os dígitos calculados conferem com os dígitos informados.
            if ((dig13 == CNPJ.charAt(12)) && (dig14 == CNPJ.charAt(13)))
                return(true);
            else return(false);
        } catch (InputMismatchException erro) {
            return(false);
        }
    }

    //OK
    private boolean validaEmail(String email){

        boolean isEmailIdValid = false;

        if (email != null && email.length() > 0 && email.length() <= 77) {
            String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(email)
                    ;
            if (matcher.matches()) {
                isEmailIdValid = true;
            } else {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Digite um email que seja valido!");
            }
        }
        else {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Digite um email que seja valido!");
        }

        return isEmailIdValid;
    }

    //ok
    private boolean validaQtdChaves(ChaveModel chaveModel){
        final List<ConsultaQtd> chaveModels = chaveRepository.findByAgenciaAndConta(chaveModel.getNumagencia().toString(), chaveModel.getNumconta().toString(), chaveModel.getTipocliente());

        if(chaveModels.size()>=5 && chaveModel.getTipocliente().toLowerCase().equals("fisica")){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Cliente já atingiu numero maximo de chaves");
        }

        if(chaveModels.size()>=20 && chaveModel.getTipocliente().toLowerCase().equals("juridica")){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Cliente já atingiu numero maximo de chaves");
        }
        return true;
    }

    //OK
    public AlterarResponse alterarChave(String id, ChaveModel chaveModel) {
        ChaveModel chaveAlterada = processaAlteracao(id, chaveModel);
        chaveRepository.save(chaveAlterada);

        AlterarResponse alterarResponse = new AlterarResponse();
        BeanUtils.copyProperties(chaveAlterada, alterarResponse, "datainativacao");
        return alterarResponse;
    }

    //OK
    private ChaveModel processaAlteracao(String id, ChaveModel chaveModel){
        ChaveModel chaveAtual = new ChaveModel();

        try
        { chaveAtual = chaveRepository.findById(id).get();
        }
        catch (Exception e)
        { throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Id não encontrado!");
        }

        if (!(chaveAtual.getDatainativacao() == null)){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Chave já desativada!");
        }

        try {
            BeanUtils.copyProperties(chaveModel, chaveAtual, "id", "tipochave", "valorchave", "tipocliente");
            if (validaConta(chaveAtual)){
                chaveAtual.setDatainclusao(LocalDateTime.now().toString());
                return chaveAtual;
            }
        }
        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Verifique os dados inseridos!");
        }
        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Verifique os dados inseridos!");
    }

    public boolean deletarChave(String id) {
        ChaveModel chaveAtual = new ChaveModel();

        try{
            chaveAtual = chaveRepository.findById(id).get();
            if (chaveAtual.getDatainativacao()==null){
                chaveAtual.setDatainativacao(LocalDateTime.now().toString());
                chaveRepository.save(chaveAtual);
                return true;
            } else {
                return false;
            }
        } catch (Exception e){
            return false;
        }
    }


    public ChaveModel listarPorId(String id) {

        try {
            ChaveModel chaveModel = chaveRepository.findById(id).get();
            return chaveModel;
        }
        catch (Exception e){
            return null;
        }

    }

    public List<ConsultaPadrao> listarPorTipoChave(String tipo) {

        try {
            List<ConsultaPadrao> listarChaves = chaveRepository.findByTipoChave(tipo);
            if (listarChaves.size()<=0){
                return null;
            }
            return listarChaves;
        }
        catch (Exception e){
            return null;
        }

    }

    public List<ConsultaPadrao> listarPorAgenciaConta(String agencia, String conta) {
        try {
            List<ConsultaPadrao> listarChave = chaveRepository.findByAgenciaConta(agencia, conta);
            if (listarChave.size() <= 0) {
                return null;
            }
            return listarChave;
        } catch (Exception e) {
            return null;
        }
    }

    public List<ConsultaPadrao> listarPorNome(String nome) {
        try {
            List<ConsultaPadrao> listarPorNome = chaveRepository.findByNome(nome);
            if (listarPorNome.size()<=0){
                return null;
            }
            return listarPorNome;
        } catch (Exception e){
            return null;
        }
    }
}
