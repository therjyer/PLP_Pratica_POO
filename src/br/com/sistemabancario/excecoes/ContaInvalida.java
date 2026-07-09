package br.com.sistemabancario.excecoes;

/**
 * Exceção customizada para a validação de existência de contas.
 * Lançada ao tentar realizar operações com números de conta inexistentes no repositório.
 */
public class ContaInvalida extends RuntimeException {

    /**
     * Construtor da exceção que recebe uma mensagem personalizada.
     * * @param mensagem Mensagem detalhando o erro (ex: "Conta não encontrada no sistema.").
     */
    public ContaInvalida(String mensagem) {
        super(mensagem);
    }
}