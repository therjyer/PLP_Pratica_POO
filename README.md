# Sistema Bancário (PLP_PRATICA_POO)

Este é um projeto prático focado na aplicação avançada de Programação Orientada a Objetos (POO), princípios SOLID e Padrões de Projeto em Java. O sistema simula o funcionamento de uma instituição financeira, englobando a gestão de contas, execução de transações dinâmicas, persistência de dados em arquivos e tratamento seguro de exceções.

## Arquitetura do Código-Fonte (`src/br/com/sistemabancario/`)

A organização dos pacotes foi rigorosamente dividida para garantir baixo acoplamento e alta coesão, facilitando a escalabilidade e a manutenção do código.

| Pacote | Responsabilidade | Componentes Principais |
| --- | --- | --- |
| **`modelo`** | Entidades de Domínio (POJOs) e hierarquia de herança principal. | `Cliente`, `Conta`, `ContaCorrente`, `ContaPoupanca`, `ContaInvestimento`, `Transacao` |
| **`interfaces`** | Definição de contratos e abstrações essenciais. | `Autenticavel`, `ITaxaOperacao` |
| **`padroes`** | Implementação isolada de Design Patterns. | `ContaFabrica`, `TaxaFixa`, `TaxaPercentual`, `IsentoTaxa` |
| **`excecoes`** | Tratamento de erros customizados de negócio. | `SaldoInsuficiente`, `ContaInvalida` |
| **`gerenciamento`** | Lógica de negócio, regras de controle e separação de responsabilidades. | `Banco`, `GerenciadorTransacoes` |
| **`persistencia`** | Manipulação de I/O (Leitura e gravação de arquivos). | `GerenciadorArquivo`, `LoggerSistema` |
| **`ui`** | Interface com o usuário (loop de interação) e inicialização da aplicação. | `MenuInterativo`, `Main` |

## Padrões de Projeto e Arquitetura

O desenvolvimento fez uso estratégico de padrões para resolver problemas de design de software e flexibilizar o sistema:

* **Factory Method:** Utilizado na classe `ContaFabrica` para abstrair e centralizar a lógica complexa de instanciação dos diferentes tipos de contas.
* **Strategy:** Implementado através da interface `ITaxaOperacao`, permitindo a variação dinâmica e intercambiável das regras de cobrança (fixa, percentual ou isenção) sem modificar as classes de domínio.
* **Singleton:** Aplicado na classe `Banco` para garantir uma instância única de controle global de clientes e contas durante toda a execução.
* **Single Responsibility Principle (SRP):** Refletido na classe `GerenciadorTransacoes`, que retira a responsabilidade de transferência de dentro das próprias contas, isolando a lógica financeira.

## Armazenamento e Persistência (`dados/`)

O estado da aplicação é mantido de forma não volátil fora do código-fonte, garantindo que contas e extratos não sejam perdidos ao encerrar o sistema.

| Arquivo | Descrição |
| --- | --- |
| **`contas_salvas.csv`** | Arquivo delimitado para gravação e leitura de todas as contas registradas. |
| **`historico_transacoes.csv`** | Persistência do histórico financeiro, atuando como o extrato de todas as transações realizadas. |
| **`sistema.log`** | Arquivo de texto gerado automaticamente pelo `LoggerSistema` para rastrear exceções, avisos e eventos críticos. |