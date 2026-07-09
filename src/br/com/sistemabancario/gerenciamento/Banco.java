package br.com.sistemabancario.gerenciamento;

import br.com.sistemabancario.modelo.Cliente;
import br.com.sistemabancario.modelo.Conta;
import br.com.sistemabancario.persistencia.GerenciadorArquivo;
import br.com.sistemabancario.persistencia.LoggerSistema;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por atuar como o banco de dados principal em memória da aplicação.
 * Implementa o Padrão de Projeto Singleton para garantir que exista apenas uma
 * única instância de controle global de clientes e contas durante toda a execução.
 */
public class Banco {

    // Instância estática e única da classe (Padrão Singleton)
    private static Banco instancia;

    // Listas globais atuando como repositório em memória
    private List<Conta> contas;
    private List<Cliente> clientes;

    /**
     * Construtor privado para impedir a instanciação externa utilizando a palavra-chave 'new'.
     * Inicializa as listas internas em memória vazias.
     */
    private Banco() {
        this.contas = new ArrayList<>();
        this.clientes = new ArrayList<>();
    }

    /**
     * Retorna a instância única do Banco.
     * Utiliza o modificador 'synchronized' para evitar inconsistências em caso de acesso concorrente
     * durante a primeira inicialização.
     *
     * @return A instância global do objeto Banco.
     */
    public static synchronized Banco getInstancia() {
        if (instancia == null) {
            instancia = new Banco();
        }
        return instancia;
    }

    /**
     * Aciona a camada de persistência para ler os dados do disco e carregá-los 
     * para as listas em memória (contas e clientes) no momento de arranque da aplicação.
     */
    public void inicializarDados() {
        try {
            // Delega a carga dos objetos ao GerenciadorArquivo
            this.contas = GerenciadorArquivo.carregarDados();
            this.clientes.clear(); // Previne duplicidades em caso de recarregamento
            
            // Popula a lista de clientes reconstruindo-a a partir dos titulares das contas salvas
            for (Conta conta : this.contas) {
                Cliente titular = conta.getTitular();
                if (!existeCliente(titular.getCpf())) {
                    this.clientes.add(titular);
                }
            }
            LoggerSistema.registrarLog("Banco de dados em memória inicializado com sucesso.");
        } catch (Exception e) {
            LoggerSistema.registrarErro(e);
        }
    }

    /**
     * Aciona a camada de persistência para gravar o estado atual das contas em memória 
     * no arquivo físico no momento do encerramento da aplicação.
     */
    public void salvarEstado() {
        try {
            GerenciadorArquivo.salvarDados(this.contas);
            LoggerSistema.registrarLog("Estado do banco persistido fisicamente com sucesso.");
        } catch (Exception e) {
            LoggerSistema.registrarErro(e);
        }
    }

    /**
     * Adiciona uma nova conta recém-instanciada ao repositório global.
     *
     * @param conta A instância da conta a ser armazenada.
     */
    public void adicionarConta(Conta conta) {
        if (conta != null) {
            this.contas.add(conta);
            Cliente titular = conta.getTitular();
            if (!existeCliente(titular.getCpf())) {
                this.clientes.add(titular);
            }
            LoggerSistema.registrarLog("Nova conta registrada em memória: " + conta.getNumero());
        }
    }

    /**
     * Busca uma conta específica baseando-se no seu número identificador.
     *
     * @param numero O número da conta procurada.
     * @return A instância da Conta, ou null caso não seja encontrada.
     */
    public Conta buscarConta(String numero) {
        for (Conta c : this.contas) {
            if (c.getNumero().equals(numero)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Método auxiliar privado para verificar a existência de um cliente pelo CPF.
     *
     * @param cpf O CPF do cliente.
     * @return true se o cliente já estiver registrado, false caso contrário.
     */
    private boolean existeCliente(String cpf) {
        for (Cliente c : this.clientes) {
            if (c.getCpf().equals(cpf)) {
                return true;
            }
        }
        return false;
    }

    // Getters para expor as listas à interface e a outros módulos do sistema

    public List<Conta> getContas() {
        return contas;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }
}