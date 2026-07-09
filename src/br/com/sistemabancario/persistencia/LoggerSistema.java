package br.com.sistemabancario.persistencia;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Classe responsável por registrar eventos e erros do sistema em um arquivo de log textual.
 * Faz parte do módulo de persistência (Infraestrutura), garantindo o rastreamento e auditoria 
 * das atividades da aplicação.
 */
public class LoggerSistema {

    // Caminho relativo para o arquivo de log definido na arquitetura do projeto
    private static final String CAMINHO_ARQUIVO = "dados/sistema.log";
    private static final DateTimeFormatter FORMATADOR_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    /**
     * Registra uma mensagem informativa ou um evento comum no log do sistema.
     *
     * @param mensagem A mensagem a ser registrada (ex: "Sistema iniciado", "Conta criada").
     */
    public static void registrarLog(String mensagem) {
        String dataHora = LocalDateTime.now().format(FORMATADOR_DATA);
        String logFormatado = String.format("[%s] INFO: %s", dataHora, mensagem);
        escreverNoArquivo(logFormatado);
    }

    /**
     * Registra um erro ou exceção capturada no log do sistema.
     * Útil para auditoria de falhas sem interromper a execução para o utilizador final.
     *
     * @param e A exceção que foi capturada pelo bloco try-catch.
     */
    public static void registrarErro(Exception e) {
        String dataHora = LocalDateTime.now().format(FORMATADOR_DATA);
        String logFormatado = String.format("[%s] ERRO: %s - %s", dataHora, e.getClass().getSimpleName(), e.getMessage());
        escreverNoArquivo(logFormatado);
    }

    /**
     * Método utilitário privado que encapsula a lógica de escrita no arquivo físico.
     *
     * @param linha O texto estruturado pronto para ser escrito no documento.
     */
    private static void escreverNoArquivo(String linha) {
        garantirDiretorio();
        
        // O parâmetro 'true' no FileWriter habilita o modo 'append', adicionando ao final do arquivo.
        // O uso do try-with-resources garante o fechamento automático do BufferedWriter.
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ARQUIVO, true))) {
            writer.write(linha);
            writer.newLine();
        } catch (IOException e) {
            // Em caso de falha crítica na gravação do próprio log, exibe no terminal
            System.err.println("Falha crítica de I/O ao tentar gravar no arquivo de log: " + e.getMessage());
        }
    }

    /**
     * Verifica e cria o diretório 'dados/' caso ele ainda não exista na raiz do projeto.
     * Evita a exceção FileNotFoundException ao tentar criar o arquivo em uma pasta inexistente.
     */
    private static void garantirDiretorio() {
        File diretorio = new File("dados");
        if (!diretorio.exists()) {
            diretorio.mkdirs();
        }
    }
}