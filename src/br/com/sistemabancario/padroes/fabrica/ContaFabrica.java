package br.com.sistemabancario.padroes.fabrica;

import br.com.sistemabancario.modelo.Cliente;
import br.com.sistemabancario.modelo.Conta;
import br.com.sistemabancario.modelo.ContaCorrente;
import br.com.sistemabancario.modelo.ContaPoupanca;
import br.com.sistemabancario.modelo.ContaInvestimento;
import java.util.Random;

/**
 * Classe responsável por centralizar a criação de objetos do tipo Conta.
 * Implementa o Padrão de Projeto Factory Method, isolando o processo
 * de instanciação das subclasses específicas.
 */
public class ContaFabrica {

    /**
     * Cria e retorna uma instância de Conta baseada no tipo especificado.
     *
     * @param tipo    O tipo de conta desejada ("corrente", "poupanca" ou "investimento").
     * @param titular O objeto Cliente que será o titular da nova conta.
     * @return Uma instância da subclasse apropriada de Conta.
     * @throws IllegalArgumentException Caso o tipo de conta informado seja inválido.
     */
    public Conta criarConta(String tipo, Cliente titular) {
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new IllegalArgumentException("O tipo de conta deve ser informado.");
        }

        // Gera um número de conta aleatório fictício com 6 dígitos para a nova conta
        String numeroConta = String.format("%06d", new Random().nextInt(1000000));

        // Utiliza estrutura switch/case para determinar a subclasse e aplicar valores iniciais padrão
        switch (tipo.toLowerCase().trim()) {
            case "corrente":
                // Conta Corrente criada com um limite de cheque especial padrão de R$ 500,00
                return new ContaCorrente(numeroConta, titular, 500.0);
                
            case "poupanca":
                // Conta Poupança criada com uma taxa de rendimento padrão de 0.5% (0.005)
                return new ContaPoupanca(numeroConta, titular, 0.005);
                
            case "investimento":
                // Conta de Investimento criada com um índice de risco/taxa padrão
                return new ContaInvestimento(numeroConta, titular, 0.02);
                
            default:
                throw new IllegalArgumentException("Tipo de conta desconhecido ou inválido: " + tipo);
        }
    }
}