# SDN-Scale-AVL-vs-Red-Black-Work

## Visão geral
Este projeto compara duas estruturas de árvore (AVL e Red-Black) aplicadas a roteadores/filtragem de pacotes, medindo desempenho e escalabilidade. Inclui geradores de dados, validadores, rotinas de benchmark e exportação de resultados para CSV.

## Objetivos
- Implementar e comparar `AVLRouterTree` e `RedBlackRouterTree` em operações relevantes para tabelas de regras de roteador.
- Medir tempo de construção, busca, inserção e remoção em diferentes cenários de carga.
- Exportar resultados para análise (CSV + gráficos).

## Estrutura do repositório
- `pom.xml` — configuração Maven
- `src/main/java/br/edu/router/` — código-fonte principal
  - `benchmark/` — `BenchmarkRunner`, `DataGenerator`, `BenchmarkResult`
  - `model/` — `PacketRule`
  - `tree/` — implementação das árvores e subpacotes `avl/` e `redblack/`
  - `util/` — utilitários como `CsvExporter` e `Timer`
  - `validator/` — validadores `AVLValidator` e `RedBlackValidator`
- `results/` — saída dos benchmarks
  - `benchmark_results.csv` e `performance_results.csv`
  - `charts/` — imagens/plots gerados

## Requisitos
- Java 26 ou superior (conforme `<maven.compiler.release>26</maven.compiler.release>` no `pom.xml`)
- Maven 3.6+

## Como compilar
1. Compilar o projeto com Maven:

```bash
mvn -DskipTests package
```

2. Executar testes unitários:

```bash
mvn test
```

## Como executar os benchmarks
Você pode executar a classe principal do benchmark diretamente via Maven Exec. Exemplo:

```bash
mvn -Dexec.mainClass="br.edu.router.benchmark.BenchmarkRunner" exec:java
```

Isso executará os cenários de benchmark definidos em `BenchmarkRunner` e gravará os resultados em `results/`.

Alternativamente, se houver um `Main` que orquestre execuções, rode:

```bash
mvn -Dexec.mainClass="br.edu.router.Main" exec:java
```

## Saída e análise
- Resultados CSV: `results/benchmark_results.csv` e `results/performance_results.csv`.
- Se gráficos forem gerados por ferramentas externas, salve-os em `results/charts/`.

Para reproduzir análises ou gerar novos gráficos, abra os CSVs em sua ferramenta preferida (Python/pandas, R, Excel) ou verifique scripts locais (se houver).

## Organização do Benchmark
- `DataGenerator` controla a geração de regras/pacotes e configura parâmetros de carga.
- `BenchmarkRunner` executa operações (inserção, busca, remoção) em ambas as árvores e mede tempos usando `Timer`.
- `CsvExporter` grava resultados em formato reutilizável para comparação automática.

## Testes e Validação
- Testes unitários para ambas implementações estão em `src/test/java/br/edu/router/`:
  - `AVLRouterTreeTest.java`
  - `RedBlackRouterTreeTest.java`
  - `BenchmarkTest.java`

Use `mvn test` para rodar todos os testes.

## Como estender ou reproduzir experimentos
- Ajuste parâmetros de entrada no `DataGenerator` (tamanhos, distribuição, número de iterações).
- Adicione novos cenários no `BenchmarkRunner` e garanta que `CsvExporter` inclua as colunas necessárias.

## Observações importantes
- Certifique-se que a JVM tem memória suficiente para grandes entradas (use `-Xmx` se necessário).
- Para medidas estáveis, execute benchmarks múltiplas vezes e descarte outliers.
---
