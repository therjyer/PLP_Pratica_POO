package br.com.sistemabancario.padroes.estrategia;

import br.com.sistemabancario.interfaces.ITaxaOperacao;

/**
 * Implementação da estratégia de cobrança baseada em percentual.
 * Calcula a tarifa como uma fração do valor total movimentado na operação financeira.
 */
public class TaxaPercentual implements ITaxaOperacao {

    // Percentual constante da taxa (ex: 1% do valor da transação)
    private static final double PERCENTUAL = 0.01;

    /**
     * Calcula a tarifa financeira sobre um montante variável.
     * * @param valor O montante transacionado que servirá de base.
     * @return O valor correspondente à porcentagem da transação.
     */
    @Override
    public double calcularTaxa(double valor) {
        return valor * PERCENTUAL;
    }
}