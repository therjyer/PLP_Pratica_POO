package br.com.sistemabancario.modelo;

import br.com.sistemabancario.interfaces.Autenticavel;

/**
 * Entidade de domínio que representa o titular de uma ou mais contas bancárias.
 */
public class Cliente implements Autenticavel {

    private String nome;
    private String cpf;
    private String senha;

    /**
     * Construtor padrão para inicialização da entidade Cliente.
     *
     * @param nome  O nome completo do titular.
     * @param cpf   O documento de identificação (CPF).
     * @param senha A credencial de segurança para acessos.
     */
    public Cliente(String nome, String cpf, String senha) {
        this.nome = nome;
        this.cpf = cpf;
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    /**
     * Verifica se a credencial fornecida corresponde à senha registrada pelo cliente.
     *
     * @param senha A palavra-passe a ser validada.
     * @return true se a senha coincidir, false caso contrário.
     */
    @Override
    public boolean autenticar(String senha) {
        if (this.senha == null || senha == null) {
            return false;
        }
        return this.senha.equals(senha);
    }
}