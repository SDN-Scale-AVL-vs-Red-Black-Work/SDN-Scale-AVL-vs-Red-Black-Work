package br.edu.router.benchmark;

import br.edu.router.model.PacketRule;
import br.edu.router.tree.RouterTree;
import br.edu.router.util.Timer;
import java.util.List;

public class BenchmarkRunner {

    public static BenchmarkResult  runPerformanceTest(RouterTree tree, List<PacketRule> rules, String treeName) {
        Timer timer = new Timer();
        int totalElements = rules.size();

        System.out.println("=== Iniciando Stress Test: " + treeName + " (" + totalElements + " elementos) ===");

        timer.start();
        for (PacketRule rule : rules) {
            tree.insert(rule);
        }
        timer.stop();
        long tempoInsercao = timer.getElapsedTimeInNanoseconds();

        timer.start();
        for (PacketRule rule : rules) {
            tree.search(rule);
        }
        timer.stop();
        long tempoBusca = timer.getElapsedTimeInNanoseconds();

        int quantidadeDelecao = (int) (totalElements * 0.20);
        timer.start();
        for (int i = 0; i < quantidadeDelecao; i++) {
            tree.delete(rules.get(i));
        }
        timer.stop();
        long tempoDelecao = timer.getElapsedTimeInNanoseconds();

        int rotacoes = tree.getRotationCount();

        System.out.println("[" + treeName + "] Inserção: " + tempoInsercao + " ns | Busca: " + tempoBusca + " ns | Deleção: " + tempoDelecao + " ns | Rotações: " + rotacoes);
        System.out.println("=====================================================\n");

        return new BenchmarkResult(treeName, totalElements, tempoInsercao, tempoBusca, tempoDelecao, rotacoes);
    }
}
