package br.com.sistemabancario.padroes.estrategia;

import br.com.sistemabancario.interfaces.ITaxaOperacao;

/**
 * Implementação da estratégia de isenção total de tarifas bancárias.
 * Retorna ausência de cobranças, aplicável a cenários como contas premium ou bônus temporários.
 */
public class IsentoTaxa implements ITaxaOperacao {

    /**
     * Processa a isenção da operação financeira.
     * * @param valor O montante da transação (não afeta o cálculo desta estratégia).
     * @return O valor zero, indicando gratuidade.
     */
    @Override
    public double calcularTaxa(double valor) {
        return 0.0;
    }
}