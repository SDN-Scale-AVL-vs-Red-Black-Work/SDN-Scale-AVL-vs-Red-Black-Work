package br.edu.router;

import br.edu.router.benchmark.BenchmarkResult;
import br.edu.router.benchmark.BenchmarkRunner;
import br.edu.router.benchmark.DataGenerator;
import br.edu.router.model.PacketRule;
import br.edu.router.tree.avl.AVLRouterTree;
import br.edu.router.tree.redblack.RedBlackRouterTree;
import br.edu.router.util.CsvExporter;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        long seed = 42L; // Rigor científico: mesma semente para os mesmos dados nas duas árvores
        int[] volumes = {10000, 100000, 1000000};
        List<BenchmarkResult> performanceResults = new ArrayList<>();

        System.out.println("🤖 MONITOR DE DESEMPENHO SDN-SCALE ATIVADO 🤖\n");

        for (int volume : volumes) {
            // Gera bases de dados idênticas para a rodada atual
            List<PacketRule> avlDataset = DataGenerator.generateOrderedRules(volume, seed);
            List<PacketRule> rbtDataset = DataGenerator.generateOrderedRules(volume, seed);

            // Instancia as árvores do projeto (com os nomes exatos do Integrante 1)
            AVLRouterTree avlTree = new AVLRouterTree();
            RedBlackRouterTree rbtTree = new RedBlackRouterTree();

            // Executa os testes pesados na árvore AVL
            BenchmarkResult avlRes = BenchmarkRunner.runPerformanceTest(avlTree, avlDataset, "AVL_Tree");
            performanceResults.add(avlRes);

            // Executa os testes pesados na árvore Red-Black
            BenchmarkResult rbtRes = BenchmarkRunner.runPerformanceTest(rbtTree, rbtDataset, "RedBlack_Tree");
            performanceResults.add(rbtRes);
        }

        // Exporta o relatório final em CSV usando o teu método do CsvExporter
        CsvExporter.exportToCSV(performanceResults, "performance_results.csv");
    }
}