package br.com.sistemabancario.gerenciamento;

import br.com.sistemabancario.excecoes.SaldoInsuficiente;
import br.com.sistemabancario.modelo.Conta;
import br.com.sistemabancario.modelo.Transacao;
import br.com.sistemabancario.persistencia.LoggerSistema;

/**
 * Classe responsável por mediar operações complexas que envolvem mais de uma entidade.
 * Implementa o Princípio de Responsabilidade Única (SRP), isolando a lógica financeira 
 * de transferências para não sobrecarregar as classes de domínio base (como a superclasse Conta).
 */
public class GerenciadorTransacoes {

    /**
     * Orquestra a transferência de fundos entre duas contas distintas.
     * A operação retira o valor da conta de origem, o injeta na conta de destino e garante
     * o registro de ambos os eventos no histórico financeiro, tratando exceções de saldo insuficiente.
     *
     * @param origem  A conta da qual o valor será debitado.
     * @param destino A conta na qual o valor será creditado.
     * @param valor   O montante da transferência.
     * @throws SaldoInsuficiente Caso a conta de origem não possua saldo ou limite disponível.
     */
    public void executarTransferencia(Conta origem, Conta destino, double valor) {
        if (origem == null || destino == null) {
            throw new IllegalArgumentException("As contas de origem e destino devem ser válidas e informadas.");
        }
        
        if (origem.getNumero().equals(destino.getNumero())) {
            throw new IllegalArgumentException("Não é possível realizar uma transferência para a própria conta de origem.");
        }
        
        if (valor <= 0) {
            throw new IllegalArgumentException("O montante de transferência deve ser obrigatoriamente superior a zero.");
        }

        try {
            // A chamada ao método sacar acionará automaticamente o polimorfismo das contas filhas
            // (validando limites extras) e o Padrão Strategy (aplicando possíveis taxas de operação).
            origem.sacar(valor);
            
            // Injeta o valor na conta de destino.
            destino.depositar(valor);
            
            // Adição manual de descritivos de transferência no histórico de ambas as contas.
            // Estes registros complementam os eventos genéricos criados pelos métodos sacar/depositar.
            origem.getHistorico().add(new Transacao(valor, "TRANSFER OUT - PARA " + destino.getNumero()));
            destino.getHistorico().add(new Transacao(valor, "TRANSFER IN - DE " + origem.getNumero()));
            
            // Regista o sucesso da transação no ficheiro de logs do sistema.
            LoggerSistema.registrarLog(String.format("Transferência de R$ %.2f concluída: Origem %s -> Destino %s", 
                                                     valor, origem.getNumero(), destino.getNumero()));

        } catch (SaldoInsuficiente e) {
            // Intercepta a falha de saldo, regista o erro para propósitos de auditoria interna no sistema
            // e propaga a exceção para que a camada de interface (UI) notifique adequadamente o utilizador.
            LoggerSistema.registrarErro(e);
            throw new SaldoInsuficiente("Falha ao processar a transferência: " + e.getMessage());
        }
    }
}