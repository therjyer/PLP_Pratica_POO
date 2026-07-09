package br.com.sistemabancario.modelo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Classe POJO que representa uma transação individual.
 * Os objetos desta classe irão compor o histórico de operações (extrato) das contas.
 */
public class Transacao {
    
    // Atributos privados garantindo o encapsulamento
    private Date data;
    private double valor;
    private String tipo;

    /**
     * Construtor da transação.
     * A data de registro é gerada automaticamente no momento da instânciação.
     * * @param valor O montante da operação financeira.
     * @param tipo A classificação da transação (ex: "DEPÓSITO", "SAQUE", "TRANSFERÊNCIA").
     */
    public Transacao(double valor, String tipo) {
        this.data = new Date();
        this.valor = valor;
        this.tipo = tipo;
    }

    // Getters para acessar as informações protegidas
    
    public Date getData() {
        return data;
    }

    public double getValor() {
        return valor;
    }

    public String getTipo() {
        return tipo;
    }

    /**
     * Sobrescrita do método toString() para facilitar a impressão da linha do extrato.
     * Formata a exibição da data e do valor financeiro de maneira legível para o utilizador final.
     */
    @Override
    public String toString() {
        SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return String.format("%s | %-15s | R$ %.2f", formatador.format(this.data), this.tipo, this.valor);
    }
}