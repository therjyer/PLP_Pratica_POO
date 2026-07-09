package br.com.sistemabancario.ui;

import br.com.sistemabancario.excecoes.ContaInvalida;
import br.com.sistemabancario.excecoes.SaldoInsuficiente;
import br.com.sistemabancario.gerenciamento.Banco;
import br.com.sistemabancario.gerenciamento.GerenciadorTransacoes;
import br.com.sistemabancario.modelo.Cliente;
import br.com.sistemabancario.modelo.Conta;
import br.com.sistemabancario.padroes.fabrica.ContaFabrica;
import br.com.sistemabancario.persistencia.LoggerSistema;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Classe responsável por gerenciar a Interface Gráfica do Utilizador (GUI).
 * Utiliza JFrame para os menus principais e JOptionPane para interações dinâmicas.
 * Orquestra as operações aplicando blocos try-catch para evitar interrupções abruptas.
 */
public class MenuInterativo {

    private Banco banco;
    private JFrame framePrincipal;
    private ContaFabrica fabrica;
    private GerenciadorTransacoes gerenciadorTransacoes;

    /**
     * Construtor que inicializa as dependências do menu.
     */
    public MenuInterativo() {
        this.banco = Banco.getInstancia();
        this.fabrica = new ContaFabrica();
        this.gerenciadorTransacoes = new GerenciadorTransacoes();
        configurarEstilo();
        configurarFramePrincipal();
    }

    /**
     * Aplica o estilo visual nativo do sistema operacional à interface Swing.
     */
    private void configurarEstilo() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            LoggerSistema.registrarErro(e);
        }
    }

    /**
     * Inicia o ciclo de vida da interface e carrega os dados persistidos no disco.
     */
    public void iniciarLoop() {
        banco.inicializarDados();
        SwingUtilities.invokeLater(() -> framePrincipal.setVisible(true));
    }

    /**
     * Configura a janela principal do sistema bancário.
     */
    private void configurarFramePrincipal() {
        framePrincipal = new JFrame("OOP Bank - Sistema Bancário");
        framePrincipal.setSize(400, 300);
        framePrincipal.setLocationRelativeTo(null);
        framePrincipal.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        framePrincipal.setLayout(new GridLayout(4, 1, 10, 10));

        // Intercepta o evento de fechar a janela para garantir a persistência dos dados
        framePrincipal.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                encerrarSistema();
            }
        });

        JLabel lblTitulo = new JLabel("Bem-vindo ao OOP Bank", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));

        JButton btnCriarConta = new JButton("Abrir Nova Conta");
        JButton btnAcessarConta = new JButton("Acessar Conta");
        JButton btnSair = new JButton("Sair do Sistema");

        btnCriarConta.addActionListener(e -> fluxoCriarConta());
        btnAcessarConta.addActionListener(e -> fluxoAcessarConta());
        btnSair.addActionListener(e -> encerrarSistema());

        framePrincipal.add(lblTitulo);
        framePrincipal.add(btnCriarConta);
        framePrincipal.add(btnAcessarConta);
        framePrincipal.add(btnSair);
    }

    /**
     * Gerencia o fluxo de criação de uma nova conta bancária utilizando o Padrão Factory.
     */
    private void fluxoCriarConta() {
        try {
            String nome = JOptionPane.showInputDialog(framePrincipal, "Digite o nome completo do titular:");
            if (nome == null || nome.trim().isEmpty()) return;

            String cpf = JOptionPane.showInputDialog(framePrincipal, "Digite o CPF do titular:");
            if (cpf == null || cpf.trim().isEmpty()) return;

            String senha = JOptionPane.showInputDialog(framePrincipal, "Crie uma senha de acesso:");
            if (senha == null || senha.trim().isEmpty()) return;

            String[] opcoes = {"Corrente", "Poupanca", "Investimento"};
            String tipoSelecionado = (String) JOptionPane.showInputDialog(
                    framePrincipal, "Selecione o tipo de conta:", "Tipo de Conta",
                    JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);

            if (tipoSelecionado == null) return;

            Cliente novoCliente = new Cliente(nome, cpf, senha);
            Conta novaConta = fabrica.criarConta(tipoSelecionado, novoCliente);
            banco.adicionarConta(novaConta);

            JOptionPane.showMessageDialog(framePrincipal, 
                    "Conta criada com sucesso!\nNúmero da sua conta: " + novaConta.getNumero(), 
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(framePrincipal, "Erro inesperado: " + e.getMessage(), 
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Gerencia o fluxo de login verificando a existência da conta e a validade da senha.
     */
    private void fluxoAcessarConta() {
        try {
            String numeroConta = JOptionPane.showInputDialog(framePrincipal, "Informe o número da conta:");
            if (numeroConta == null || numeroConta.trim().isEmpty()) return;

            Conta conta = banco.buscarConta(numeroConta);
            if (conta == null) {
                // Lança exceção customizada caso a conta não exista
                throw new ContaInvalida("A conta de número " + numeroConta + " não foi encontrada no sistema.");
            }

            JPasswordField pf = new JPasswordField();
            int okCxl = JOptionPane.showConfirmDialog(framePrincipal, pf, "Digite sua senha:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (okCxl == JOptionPane.OK_OPTION) {
                String senha = new String(pf.getPassword());
                if (conta.getTitular().autenticar(senha)) {
                    abrirPainelConta(conta);
                } else {
                    JOptionPane.showMessageDialog(framePrincipal, "Senha incorreta. Acesso negado.", 
                            "Falha de Autenticação", JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (ContaInvalida e) {
            // Captura de erro de negócio: Conta Inexistente
            JOptionPane.showMessageDialog(framePrincipal, e.getMessage(), "Validação", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(framePrincipal, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Exibe o painel de operações financeiras para uma conta autenticada.
     * @param conta A conta atualmente logada.
     */
    private void abrirPainelConta(Conta conta) {
        JDialog painel = new JDialog(framePrincipal, "Painel da Conta - " + conta.getNumero(), true);
        painel.setSize(400, 350);
        painel.setLocationRelativeTo(framePrincipal);
        painel.setLayout(new GridLayout(6, 1, 10, 10));

        JLabel lblBoasVindas = new JLabel("Olá, " + conta.getTitular().getNome(), SwingConstants.CENTER);
        lblBoasVindas.setFont(new Font("Arial", Font.BOLD, 16));

        JButton btnDepositar = new JButton("Realizar Depósito");
        JButton btnSacar = new JButton("Realizar Saque");
        JButton btnTransferir = new JButton("Transferir Fundos");
        JButton btnExtrato = new JButton("Ver Extrato e Saldo");
        JButton btnSairConta = new JButton("Encerrar Sessão");

        btnDepositar.addActionListener(e -> realizarDeposito(conta, painel));
        btnSacar.addActionListener(e -> realizarSaque(conta, painel));
        btnTransferir.addActionListener(e -> realizarTransferencia(conta, painel));
        btnExtrato.addActionListener(e -> exibirExtrato(conta, painel));
        btnSairConta.addActionListener(e -> painel.dispose());

        painel.add(lblBoasVindas);
        painel.add(btnDepositar);
        painel.add(btnSacar);
        painel.add(btnTransferir);
        painel.add(btnExtrato);
        painel.add(btnSairConta);

        painel.setVisible(true);
    }

    private void realizarDeposito(Conta conta, JDialog parente) {
        try {
            String input = JOptionPane.showInputDialog(parente, "Informe o valor do depósito (R$):");
            if (input == null) return;
            
            double valor = Double.parseDouble(input.replace(",", "."));
            conta.depositar(valor);
            JOptionPane.showMessageDialog(parente, "Depósito de R$ " + String.format("%.2f", valor) + " realizado com sucesso.");
            
        } catch (NumberFormatException e) {
            // Em aplicações Swing, a NumberFormatException age de forma análoga à InputMismatchException do console
            JOptionPane.showMessageDialog(parente, "Valor inválido. Por favor, insira apenas números.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(parente, e.getMessage(), "Operação Inválida", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void realizarSaque(Conta conta, JDialog parente) {
        try {
            String input = JOptionPane.showInputDialog(parente, "Informe o valor para saque (R$):");
            if (input == null) return;
            
            double valor = Double.parseDouble(input.replace(",", "."));
            conta.sacar(valor);
            JOptionPane.showMessageDialog(parente, "Saque de R$ " + String.format("%.2f", valor) + " realizado com sucesso.");
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(parente, "Valor numérico inválido.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
        } catch (SaldoInsuficiente e) {
            // Captura de erro financeiro crítico
            JOptionPane.showMessageDialog(parente, e.getMessage(), "Saldo Insuficiente", JOptionPane.WARNING_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(parente, e.getMessage(), "Operação Inválida", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void realizarTransferencia(Conta origem, JDialog parente) {
        try {
            String numDestino = JOptionPane.showInputDialog(parente, "Informe o número da conta de destino:");
            if (numDestino == null || numDestino.trim().isEmpty()) return;

            Conta destino = banco.buscarConta(numDestino);
            if (destino == null) {
                throw new ContaInvalida("A conta destino (" + numDestino + ") não foi encontrada.");
            }

            String input = JOptionPane.showInputDialog(parente, "Informe o valor a transferir (R$):");
            if (input == null) return;
            
            double valor = Double.parseDouble(input.replace(",", "."));
            
            // O GerenciadorTransacoes orquestra a lógica e lança as devidas exceções caso falhe
            gerenciadorTransacoes.executarTransferencia(origem, destino, valor);
            JOptionPane.showMessageDialog(parente, "Transferência concluída com sucesso!");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(parente, "Valor numérico inválido.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
        } catch (ContaInvalida e) {
            JOptionPane.showMessageDialog(parente, e.getMessage(), "Conta de Destino Inválida", JOptionPane.WARNING_MESSAGE);
        } catch (SaldoInsuficiente e) {
            JOptionPane.showMessageDialog(parente, e.getMessage(), "Saldo Insuficiente", JOptionPane.WARNING_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(parente, e.getMessage(), "Operação Inválida", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void exibirExtrato(Conta conta, JDialog parente) {
        JTextArea textArea = new JTextArea(15, 40);
        textArea.setText(conta.gerarExtrato());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        JOptionPane.showMessageDialog(parente, scrollPane, "Extrato Bancário", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Garante que os dados sejam gravados no disco antes da finalização segura do processo.
     */
    private void encerrarSistema() {
        banco.salvarEstado();
        LoggerSistema.registrarLog("Sistema encerrado de forma segura pelo utilizador.");
        System.exit(0);
    }
}