package br.com.sistemabancario.persistencia;

import br.com.sistemabancario.modelo.Cliente;
import br.com.sistemabancario.modelo.Conta;
import br.com.sistemabancario.modelo.ContaCorrente;
import br.com.sistemabancario.modelo.ContaInvestimento;
import br.com.sistemabancario.modelo.ContaPoupanca;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por gerir a leitura e gravação dos dados das contas.
 * Implementa a persistência em ficheiros CSV, garantindo que o estado da aplicação
 * seja mantido entre as diferentes sessões do sistema.
 */
public class GerenciadorArquivo {

    private static final String CAMINHO_ARQUIVO = "dados/contas_salvas.csv";
    private static final String DELIMITADOR = ";";

    /**
     * Guarda a lista atual de contas no ficheiro CSV.
     * Sobrescreve o ficheiro anterior para refletir o estado mais recente.
     *
     * @param contas A lista de contas em memória que será persistida.
     */
    public static void salvarDados(List<Conta> contas) {
        garantirDiretorio();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ARQUIVO))) {
            // Escreve o cabeçalho do ficheiro CSV
            writer.write("TipoConta;NumeroConta;Saldo;NomeCliente;CPFCliente;SenhaCliente;AtributoEspecial");
            writer.newLine();

            for (Conta conta : contas) {
                String tipo = "";
                double atributoEspecial = 0.0;

                // Identifica o tipo concreto da conta para salvar os seus dados específicos
                if (conta instanceof ContaCorrente) {
                    tipo = "CORRENTE";
                    atributoEspecial = ((ContaCorrente) conta).getLimite();
                } else if (conta instanceof ContaPoupanca) {
                    tipo = "POUPANCA";
                    atributoEspecial = ((ContaPoupanca) conta).getTaxaRendimento();
                } else if (conta instanceof ContaInvestimento) {
                    tipo = "INVESTIMENTO";
                    atributoEspecial = ((ContaInvestimento) conta).getRisco();
                }

                Cliente titular = conta.getTitular();
                
                // Formata a linha utilizando ponto como separador decimal padronizado
                String linha = String.format(java.util.Locale.US, "%s%s%s%s%.2f%s%s%s%s%s%s%s%.4f",
                        tipo, DELIMITADOR,
                        conta.getNumero(), DELIMITADOR,
                        conta.getSaldo(), DELIMITADOR,
                        titular.getNome(), DELIMITADOR,
                        titular.getCpf(), DELIMITADOR,
                        titular.getSenha(), DELIMITADOR,
                        atributoEspecial);

                writer.write(linha);
                writer.newLine();
            }
            
            LoggerSistema.registrarLog("Dados das contas salvos com sucesso. Total: " + contas.size());

        } catch (IOException e) {
            LoggerSistema.registrarErro(e);
            System.err.println("Erro ao tentar guardar os dados das contas: " + e.getMessage());
        }
    }

    /**
     * Lê o ficheiro CSV, reconstrói os objetos e retorna uma lista de Contas.
     *
     * @return Uma List<Conta> instanciada com os dados recuperados do disco.
     */
    public static List<Conta> carregarDados() {
        List<Conta> contasCarregadas = new ArrayList<>();
        File arquivo = new File(CAMINHO_ARQUIVO);

        // Retorna a lista vazia caso seja a primeira execução e o ficheiro não exista
        if (!arquivo.exists()) {
            return contasCarregadas;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha = reader.readLine(); // Pula o cabeçalho

            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;

                String[] dados = linha.split(DELIMITADOR);

                String tipo = dados[0];
                String numero = dados[1];
                double saldo = Double.parseDouble(dados[2]);
                String nome = dados[3];
                String cpf = dados[4];
                String senha = dados[5];
                double atributoEspecial = Double.parseDouble(dados[6]);

                // Reconstrói a entidade Titular
                Cliente titular = new Cliente(nome, cpf, senha);
                Conta contaReconstruida = null;

                // Reconstrói a subclasse correta com base no Tipo mapeado
                switch (tipo) {
                    case "CORRENTE":
                        contaReconstruida = new ContaCorrente(numero, titular, atributoEspecial);
                        break;
                    case "POUPANCA":
                        contaReconstruida = new ContaPoupanca(numero, titular, atributoEspecial);
                        break;
                    case "INVESTIMENTO":
                        contaReconstruida = new ContaInvestimento(numero, titular, atributoEspecial);
                        break;
                    default:
                        LoggerSistema.registrarLog("Tipo de conta desconhecido ao carregar: " + tipo);
                        break;
                }

                if (contaReconstruida != null) {
                    // Restaura o saldo utilizando o método oficial de depósito para respeitar o encapsulamento
                    if (saldo > 0) {
                        contaReconstruida.depositar(saldo);
                        // Limpa o histórico para não gerar um falso evento de "DEPÓSITO" no extrato ao inicializar
                        contaReconstruida.getHistorico().clear();
                    }
                    contasCarregadas.add(contaReconstruida);
                }
            }

            LoggerSistema.registrarLog("Dados carregados com sucesso. Total de contas: " + contasCarregadas.size());

        } catch (IOException | NumberFormatException e) {
            LoggerSistema.registrarErro(e);
            System.err.println("Erro ao tentar carregar os dados do ficheiro: " + e.getMessage());
        }

        return contasCarregadas;
    }

    /**
     * Garante que o diretório base 'dados/' existe antes de efetuar operações de gravação.
     */
    private static void garantirDiretorio() {
        File diretorio = new File("dados");
        if (!diretorio.exists()) {
            diretorio.mkdirs();
        }
    }
}