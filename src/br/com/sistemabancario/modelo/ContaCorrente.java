package br.com.sistemabancario.modelo;

import br.com.sistemabancario.excecoes.SaldoInsuficiente;
import br.com.sistemabancario.interfaces.ITaxaOperacao;
import br.com.sistemabancario.padroes.estrategia.TaxaFixa;

/**
 * Especialização da classe Conta para representar uma Conta Corrente.
 * Possui um limite de cheque especial e aplica uma taxa fixa nas operações de saque.
 */
public class ContaCorrente extends Conta {

    private double limite;
    // Utilização do Padrão Strategy para definir a regra de cobrança de taxa
    private ITaxaOperacao regraTaxa;

    /**
     * Construtor da Conta Corrente.
     *
     * @param numero  O número identificador da conta.
     * @param titular O objeto Cliente que representa o dono da conta.
     * @param limite  O valor do limite do cheque especial.
     */
    public ContaCorrente(String numero, Cliente titular, double limite) {
        super(numero, titular);
        this.limite = limite;
        // Define uma taxa fixa padrão para saques na Conta Corrente
        this.regraTaxa = new TaxaFixa(); 
    }

    /**
     * Sobrescrita (Polimorfismo) do método sacar.
     * Considera o limite do cheque especial e aplica a taxa de operação.
     *
     * @param valor O montante a ser sacado.
     * @throws SaldoInsuficiente Caso o valor solicitado + taxa ultrapasse o saldo + limite.
     */
    @Override
    public void sacar(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor do saque deve ser estritamente positivo.");
        }

        double taxa = regraTaxa.calcularTaxa(valor);
        double valorTotalSaque = valor + taxa;

        if (this.saldo + this.limite >= valorTotalSaque) {
            this.saldo -= valorTotalSaque;
            this.historico.add(new Transacao(valor, "SAQUE"));
            this.historico.add(new Transacao(taxa, "TARIFA DE SAQUE"));
        } else {
            throw new SaldoInsuficiente(
                String.format("Falha na operação: Saldo insuficiente. Valor do saque (R$ %.2f) + Taxa (R$ %.2f) excede o saldo disponível + limite (R$ %.2f).", 
                              valor, taxa, (this.saldo + this.limite))
            );
        }
    }

    public double getLimite() {
        return limite;
    }

    public void setLimite(double limite) {
        this.limite = limite;
    }
}