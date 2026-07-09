package br.com.sistemabancario.modelo;

import br.com.sistemabancario.excecoes.SaldoInsuficiente;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe abstrata que serve como superclasse base para todos os tipos de contas bancárias.
 * Centraliza os atributos e comportamentos comuns (como saldo, depósito e extrato),
 * promovendo o reaproveitamento de código e servindo como fundação para o polimorfismo.
 */
public abstract class Conta {

    protected double saldo;
    protected String numero;
    protected Cliente titular;
    protected List<Transacao> historico;

    /**
     * Construtor base para a criação de uma nova conta.
     * O saldo é inicializado zerado e a lista de transações é instanciada.
     *
     * @param numero  O número identificador da conta.
     * @param titular O objeto Cliente que representa o dono da conta.
     */
    public Conta(String numero, Cliente titular) {
        this.numero = numero;
        this.titular = titular;
        this.saldo = 0.0;
        this.historico = new ArrayList<>();
    }

    /**
     * Adiciona fundos ao saldo da conta e registra a operação no histórico.
     *
     * @param valor O montante a ser depositado (deve ser maior que zero).
     */
    public void depositar(double valor) {
        if (valor > 0) {
            this.saldo += valor;
            this.historico.add(new Transacao(valor, "DEPÓSITO"));
        } else {
            throw new IllegalArgumentException("O valor do depósito deve ser estritamente positivo.");
        }
    }

    /**
     * Subtrai fundos do saldo da conta, caso haja disponibilidade.
     * Este método pode ser sobrescrito (Override) nas classes filhas para
     * implementar regras de negócio específicas, como limites de cheque especial.
     *
     * @param valor O montante a ser sacado.
     * @throws SaldoInsuficiente Caso o valor solicitado seja superior ao saldo disponível.
     */
    public void sacar(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor do saque deve ser estritamente positivo.");
        }
        
        if (this.saldo >= valor) {
            this.saldo -= valor;
            this.historico.add(new Transacao(valor, "SAQUE"));
        } else {
            throw new SaldoInsuficiente("Falha na operação: Saldo insuficiente para realizar o saque de R$ " + valor);
        }
    }

    /**
     * Gera um relatório textual contendo todas as transações realizadas na conta.
     *
     * @return Uma String formatada representando o extrato financeiro.
     */
    public String gerarExtrato() {
        StringBuilder extrato = new StringBuilder();
        extrato.append("=== Extrato da Conta ").append(this.numero).append(" ===\n");
        extrato.append("Titular: ").append(this.titular.getNome()).append("\n");
        extrato.append("--------------------------------------------------\n");
        
        if (this.historico.isEmpty()) {
            extrato.append("Nenhuma transação registrada.\n");
        } else {
            for (Transacao transacao : this.historico) {
                extrato.append(transacao.toString()).append("\n");
            }
        }
        
        extrato.append("--------------------------------------------------\n");
        extrato.append(String.format("SALDO ATUAL: R$ %.2f\n", this.saldo));
        
        return extrato.toString();
    }

    // Getters para encapsulamento da leitura dos atributos protegidos

    public double getSaldo() {
        return saldo;
    }

    public String getNumero() {
        return numero;
    }

    public Cliente getTitular() {
        return titular;
    }

    public List<Transacao> getHistorico() {
        return historico;
    }
}