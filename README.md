## ATIVIDADES BACKEND

_**PRIORIDADE**_
* **PADRÕES DE PROJETO**
    * Aplicar no minimo 5 padrões de projetos e documentar no código qual foi utilizado.

* **CARMEM PT.1**
    * As validações terão uma tabela propria, onde tera o dado que esta sendo validado e o regex utilizado.
    * Escolher 3 expressões e desenvolver um autômato finito correspondente.
    * Criar um documento com as seguintes informações:
        - Tabela das entradas e suas expressões comentada;
        - Diagrama dos 3 autômatos escolhidos autômato;
        - Trecho de código com expressões regulares e sua explicação.

* **CARMEM PT.2** 
    * Desenvolver uma consulta avançada para processar comandos ou filtros personalizados no sistema para impressão de relatório.
        - Simulando um analisador léxico e sintático, deverá validar um comando SQL e retornar um iterator contendo todos os dados correspondente.
    * Criar um documento com as seguintes informações:
        - Gramática utilizada para implementação da consulta avançada.
        - Técnica escolhida para implementação do sintático e a tabela correspondente.

* **OUTROS**
    * Implementar Exception Handler.
    * Integração do swagger.
        - Testes e validações com swagger.
    * Corrigir inconsistencias que perceberem.
    * Implementar minhas requisições. _**Caso houver**_

***

## PÓS-ATIVIDADES

* **SISTEMAS DISTRIBUÍDOS E COMPUTAÇÃO EM NUVEM**
    * Elaborar um documento teórico descrevendo a estimativa de custo do sistema utilizando a AWS Pricing Calculator com as seguintes informações:  

        **Serviços da AWS a considerar**
        - EC2: instâncias para hospedar o backend e cálculos de rotas.
        - RDS (PostgreSQL ou MySQL): banco de dados relacional com os dados de caminhões, pontos de coleta, itinerários e conexões entre bairros.
        - S3: armazenamento de logs, backups ou dados estáticos.
        - CloudWatch: monitoramento de métricas e logs do sistema.
        - Outros serviços opcionais que o grupo considerar necessários (ex.: Load Balancer, Lambda, serviços de segurança).  

        **Requisitos mínimos a serem considerados para cálculo**
        - Número de instâncias EC2 necessárias e tipo de instância.
        - Tamanho do banco de dados e armazenamento RDS.
        - Volume estimado de armazenamento em S3 e número de requisições mensais.
        - Métricas e logs no CloudWatch.
        - Tráfego de rede estimado (entrada e saída).

* **GERÊNCIA E CONFIGURAÇÃO DE SOFTWARE**
    * Criar documentação técnica do projeto com as seguintes informações:
        - Identificação dos Itens de Configuração (ICs): backend, frontend, banco de dados e documentos do projeto.
        - Baselines definidas: BL0 (inicial) e BL1 (funcional), cada uma com sua composição e critérios de estabilidade.
        - Política de versionamento: aplicação de Semantic Versioning (MAJOR.MINOR.PATCH).
        - Matriz de rastreabilidade: ligação entre requisitos implementados e os arquivos/componentes correspondentes.
        - RFC registrada: uma solicitação formal de mudança contendo descrição, justificativa, impacto versão aplicada.

***

## OBSERVAÇÕES SOBRE AS ATIVIDADES
A Baseline será o projeto do PI passado, criaremos branchs para trabalhar as atividades sem mexer na base. Baseline so sera alterado caso eu faça uma solicitação de mudança no backend em favor do frontend.  
Como terá mais de uma pessoa mexendo no backend, haverá zonas do codigo que não podem ser alteradas, caso precise me informe para ver se há a necessidade. Por exemplo, todos irão utilizar o model/entity e repository, por conta disso, se uma pessoa mexer, os outros terão que modificar o codigo deles.  
Não façam merge, pois utilizarei a baseline para testar o frontend, então so mexam na branch de vocês.
