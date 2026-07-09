package br.com.sistemabancario.modelo;

/**
 * Especialização da classe Conta para representar uma Conta Poupança.
 * Possui um atributo de rendimento mensal e, por padrão, é isenta de taxas de saque.
 */
public class ContaPoupanca extends Conta {

    private double taxaRendimento;

    /**
     * Construtor da Conta Poupança.
     *
     * @param numero         O número identificador da conta.
     * @param titular        O objeto Cliente que representa o dono da conta.
     * @param taxaRendimento O percentual de rendimento mensal (ex: 0.005 para 0.5%).
     */
    public ContaPoupanca(String numero, Cliente titular, double taxaRendimento) {
        super(numero, titular);
        this.taxaRendimento = taxaRendimento;
    }

    /**
     * Aplica o rendimento mensal ao saldo atual da conta.
     * Registra a operação no histórico.
     */
    public void aplicarRendimento() {
        if (this.saldo > 0) {
            double rendimento = this.saldo * this.taxaRendimento;
            this.saldo += rendimento;
            this.historico.add(new Transacao(rendimento, "RENDIMENTO MENSAL"));
        }
    }

    // O método sacar() não precisa ser sobrescrito aqui, pois a regra padrão da superclasse 
    // (validar apenas o saldo, sem limite extra e sem taxa embutida) atende à Poupança neste modelo simples.

    public double getTaxaRendimento() {
        return taxaRendimento;
    }

    public void setTaxaRendimento(double taxaRendimento) {
        this.taxaRendimento = taxaRendimento;
    }
}