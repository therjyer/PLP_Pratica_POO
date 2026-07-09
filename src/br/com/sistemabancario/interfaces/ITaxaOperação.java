package br.com.sistemabancario.interfaces;

/**
 * Contrato que define a base para o padrão de projeto Strategy.
 * Retira das contas a responsabilidade de calcular as tarifas financeiras,
 * permitindo que diferentes estratégias de cobrança (fixa, percentual ou isenção) 
 * sejam implementadas e alteradas dinamicamente.
 */
public interface ITaxaOperacao {

    /**
     * Calcula o valor da taxa com base no montante da operação financeira.
     *
     * @param valor O montante transacionado que servirá de base para o cálculo.
     * @return O valor final da tarifa a ser cobrada pela transação.
     */
    double calcularTaxa(double valor);
    
}