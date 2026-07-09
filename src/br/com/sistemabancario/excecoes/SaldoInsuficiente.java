package br.com.sistemabancario.excecoes;

/**
 * Exceção customizada para o tratamento de violações em regras de negócio financeiras.
 * Lançada durante tentativas de saque ou transferência com valores superiores
 * aos fundos disponíveis na conta de origem.
 */
public class SaldoInsuficiente extends RuntimeException {

    /**
     * Construtor da exceção que recebe uma mensagem personalizada.
     * * @param mensagem Mensagem detalhando o erro (ex: "Saldo insuficiente para esta operação").
     */
    public SaldoInsuficiente(String mensagem) {
        super(mensagem);
    }
}