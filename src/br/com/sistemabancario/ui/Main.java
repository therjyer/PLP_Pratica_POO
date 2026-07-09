package br.com.sistemabancario.ui;

import br.com.sistemabancario.gerenciamento.Banco;
import br.com.sistemabancario.persistencia.LoggerSistema;

/**
 * Ponto de entrada (Entry Point) principal da aplicação.
 * Mantém-se extremamente enxuto, limitando-se a inicializar os dados
 * de persistência e a interface gráfica do sistema.
 */
public class Main {

    public static void main(String[] args) {
        // Registra o início da execução da aplicação no arquivo de log
        LoggerSistema.registrarLog("Inicializando o Sistema Bancário OOP...");
        
        // Inicializa os dados (recuperando as contas e clientes do arquivo CSV)
        Banco.getInstancia().inicializarDados();
        
        // Instancia e inicia o loop da interface gráfica do utilizador (UI)
        MenuInterativo interfaceUsuario = new MenuInterativo();
        interfaceUsuario.iniciarLoop();
    }
}