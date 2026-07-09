package br.com.sistemabancario.modelo;

import br.com.sistemabancario.excecoes.SaldoInsuficiente;
import br.com.sistemabancario.interfaces.ITaxaOperacao;
import br.com.sistemabancario.padroes.estrategia.TaxaPercentual;

/**
 * Especialização da classe Conta para representar uma Conta de Investimento.
 * Incorpora um fator de risco e aplica uma taxa percentual nas operações de saque.
 */
public class ContaInvestimento extends Conta {

    private double risco;
    // Utilização do Padrão Strategy para definir a regra de cobrança de taxa
    private ITaxaOperacao regraTaxa;

    /**
     * Construtor da Conta de Investimento.
     *
     * @param numero  O número identificador da conta.
     * @param titular O objeto Cliente que representa o dono da conta.
     * @param risco   O índice de risco associado ao investimento.
     */
    public ContaInvestimento(String numero, Cliente titular, double risco) {
        super(numero, titular);
        this.risco = risco;
        // Define uma taxa percentual padrão para saques na Conta de Investimento
        this.regraTaxa = new TaxaPercentual();
    }

    /**
     * Sobrescrita (Polimorfismo) do método sacar.
     * Valida o saldo e aplica uma taxa percentual sobre o valor do saque.
     *
     * @param valor O montante a ser sacado.
     * @throws SaldoInsuficiente Caso o valor solicitado + taxa ultrapasse o saldo disponível.
     */
    @Override
    public void sacar(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor do saque deve ser estritamente positivo.");
        }

        double taxa = regraTaxa.calcularTaxa(valor);
        double valorTotalSaque = valor + taxa;

        if (this.saldo >= valorTotalSaque) {
            this.saldo -= valorTotalSaque;
            this.historico.add(new Transacao(valor, "SAQUE"));
            this.historico.add(new Transacao(taxa, "TARIFA DE SAQUE (PERCENTUAL)"));
        } else {
            throw new SaldoInsuficiente(
                String.format("Falha na operação: Saldo insuficiente. Valor do saque (R$ %.2f) + Taxa (R$ %.2f) excede o saldo disponível (R$ %.2f).", 
                              valor, taxa, this.saldo)
            );
        }
    }

    public double getRisco() {
        return risco;
    }

    public void setRisco(double risco) {
        this.risco = risco;
    }
}