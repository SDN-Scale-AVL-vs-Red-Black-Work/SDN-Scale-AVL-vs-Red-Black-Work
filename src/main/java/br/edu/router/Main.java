package br.edu.router;

import br.edu.router.benchmark.BenchmarkResult;
import br.edu.router.benchmark.BenchmarkRunner;
import br.edu.router.benchmark.DataGenerator;
import br.edu.router.model.PacketRule;
import br.edu.router.tree.avl.AVLRouterTree;
import br.edu.router.tree.redblack.RedBlackRouterTree;
import br.edu.router.util.CsvExporter;
import br.edu.router.validator.AVLValidator;
import br.edu.router.validator.RedBlackValidator;

import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final long SEED = 42L;
    private static final int DELETION_PERCENTAGE = 20;
    private static final String OUTPUT_FILE = "performance_results.csv";

    public static void main(String[] args) {
        int[] volumes = {10000, 100000, 1000000};
        List<BenchmarkResult> performanceResults = new ArrayList<>();

        printHeader(volumes);

        for (int volume : volumes) {
            printRoundHeader(volume);

            // Same seed and generation strategy keep both trees under the same workload.
            List<PacketRule> avlDataset = DataGenerator.generateOrderedRules(volume, SEED);
            List<PacketRule> rbtDataset = DataGenerator.generateOrderedRules(volume, SEED);
            AVLRouterTree avlTree = new AVLRouterTree();
            RedBlackRouterTree rbtTree = new RedBlackRouterTree();

            BenchmarkResult avlRes = BenchmarkRunner.runPerformanceTest(avlTree, avlDataset, "AVL_Tree");
            performanceResults.add(avlRes);
            printAvlReport(avlTree);

            BenchmarkResult rbtRes = BenchmarkRunner.runPerformanceTest(rbtTree, rbtDataset, "RedBlack_Tree");
            performanceResults.add(rbtRes);
            printRedBlackReport(rbtTree);

            printComparison(avlRes, rbtRes);
        }

        CsvExporter.exportToCSV(performanceResults, OUTPUT_FILE);
        System.out.println("\nExecucao finalizada. O relatorio CSV foi salvo em results/" + OUTPUT_FILE + ".");
    }

    private static void printHeader(int[] volumes) {
        System.out.println("============================================================");
        System.out.println("      SDN-Scale - Analise AVL vs Red-Black");
        System.out.println("============================================================");
        System.out.println("Seed fixa: " + SEED);
        System.out.println("Volumes testados: " + formatVolumes(volumes));
        System.out.println("Operacoes por rodada: insercao, busca e delecao de "
                + DELETION_PERCENTAGE + "% dos elementos");
        System.out.println("Validacao: invariantes estruturais verificadas apos a delecao\n");
    }

    private static void printRoundHeader(int volume) {
        System.out.println("------------------------------------------------------------");
        System.out.println("Rodada com " + formatNumber(volume) + " regras de pacote");
        System.out.println("------------------------------------------------------------");
    }

    private static void printAvlReport(AVLRouterTree tree) {
        List<String> violations = AVLValidator.validate(tree);
        printTreeReport("AVL", tree.size(), tree.height(), tree.getRotationCount(), violations);
    }

    private static void printRedBlackReport(RedBlackRouterTree tree) {
        List<String> violations = RedBlackValidator.validate(tree);
        printTreeReport("Red-Black", tree.size(), tree.height(), tree.getRotationCount(), violations);
    }

    private static void printTreeReport(String treeName, int size, int height, int rotations,
                                        List<String> violations) {
        System.out.println("Resumo estrutural - " + treeName);
        System.out.println("Nos restantes : " + formatNumber(size));
        System.out.println("Altura final  : " + height);
        System.out.println("Rotacoes      : " + formatNumber(rotations));

        if (violations.isEmpty()) {
            System.out.println("Validacao     : OK - invariantes preservadas");
        } else {
            System.out.println("Validacao     : FALHA - " + violations.size() + " violacao(oes)");
            for (String violation : violations) {
                System.out.println("  [!] " + violation);
            }
        }

        System.out.println();
    }

    private static void printComparison(BenchmarkResult avl, BenchmarkResult rbt) {
        System.out.println("Comparativo da rodada");
        printMetricComparison("Insercao", avl.getInsertionTime(), rbt.getInsertionTime());
        printMetricComparison("Busca", avl.getSearchTime(), rbt.getSearchTime());
        printMetricComparison("Delecao", avl.getDeletionTime(), rbt.getDeletionTime());
        printRotationComparison(avl.getRotations(), rbt.getRotations());
        System.out.println();
    }

    private static void printMetricComparison(String label, long avlTime, long rbtTime) {
        String winner = avlTime < rbtTime ? "AVL" : rbtTime < avlTime ? "Red-Black" : "Empate";
        System.out.printf("%-10s AVL=%s ns | Red-Black=%s ns | Melhor: %s%n",
                label, formatNumber(avlTime), formatNumber(rbtTime), winner);
    }

    private static void printRotationComparison(int avlRotations, int rbtRotations) {
        String fewerRotations = avlRotations < rbtRotations
                ? "AVL"
                : rbtRotations < avlRotations ? "Red-Black" : "Empate";

        System.out.printf("%-10s AVL=%s | Red-Black=%s | Menos rotacoes: %s%n",
                "Rotacoes", formatNumber(avlRotations), formatNumber(rbtRotations), fewerRotations);
    }

    private static String formatNumber(int number) {
        return String.format("%,d", number);
    }

    private static String formatNumber(long number) {
        return String.format("%,d", number);
    }

    private static String formatVolumes(int[] volumes) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < volumes.length; i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(formatNumber(volumes[i]));
        }

        return builder.toString();
    }
}
