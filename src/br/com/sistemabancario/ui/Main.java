package br.com.sistemabancario.ui;

import br.com.sistemabancario.persistencia.LoggerSistema;

/**
 * Ponto de entrada (Entry Point) principal da aplicação.
 * Mantém-se extremamente enxuto, limitando-se a inicializar a interface do utilizador.
 */
public class Main {

    public static void main(String[] args) {
        LoggerSistema.registrarLog("Inicializando o Sistema Bancário OOP...");
        
        // Instancia e inicia o loop da interface gráfica do utilizador
        MenuInterativo interfaceUsuario = new MenuInterativo();
        interfaceUsuario.iniciarLoop();
    }
}