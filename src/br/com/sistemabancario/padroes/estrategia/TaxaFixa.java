package br.com.sistemabancario.padroes.estrategia;

import br.com.sistemabancario.interfaces.ITaxaOperacao;

/**
 * Implementação da estratégia de cobrança com valor fixo.
 * Aplica uma tarifa estática e constante, independentemente do montante financeiro da transação.
 */
public class TaxaFixa implements ITaxaOperacao {

    // Valor constante da taxa cobrada (ex: R$ 5,00)
    private static final double VALOR_FIXO = 5.0;

    /**
     * Calcula a tarifa financeira da operação.
     * * @param valor O montante da operação.
     * @return O valor fixo predeterminado da taxa.
     */
    @Override
    public double calcularTaxa(double valor) {
        return VALOR_FIXO;
    }
}