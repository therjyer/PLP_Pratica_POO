package br.com.sistemabancario.interfaces;

/**
 * Contrato de segurança para entidades que necessitam de autenticação no sistema.
 * Pode ser implementado por Clientes, Gerentes, ou qualquer outro tipo de utilizador futuro,
 * garantindo o princípio de baixo acoplamento e flexibilidade.
 */
public interface Autenticavel {

    /**
     * Verifica se a credencial fornecida permite o acesso ao sistema.
     *
     * @param senha A palavra-passe a ser validada.
     * @return true se a autenticação for bem-sucedida, false caso contrário.
     */
    boolean autenticar(String senha);
    
}