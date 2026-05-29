package br.edu.router;

import br.edu.router.benchmark.BenchmarkResult;
import br.edu.router.benchmark.BenchmarkRunner;
import br.edu.router.benchmark.DataGenerator;
import br.edu.router.model.PacketRule;
import br.edu.router.tree.avl.AVLRouterTree;
import br.edu.router.tree.redblack.RedBlackRouterTree;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BenchmarkTest {

    private static final long SEED = 42L;

    @Test
    void dataGeneratorProducesCorrectQuantity() {
        assertEquals(100,  DataGenerator.generateOrderedRules(100,  SEED).size());
        assertEquals(1000, DataGenerator.generateOrderedRules(1000, SEED).size());
    }

    @Test
    void sameSeedProducesIdenticalDatasets() {
        List<PacketRule> d1 = DataGenerator.generateOrderedRules(200, SEED);
        List<PacketRule> d2 = DataGenerator.generateOrderedRules(200, SEED);

        for (int i = 0; i < d1.size(); i++) {
            assertEquals(d1.get(i).getId(),       d2.get(i).getId());
            assertEquals(d1.get(i).getPriority(), d2.get(i).getPriority());
        }
    }

    @Test
    void generatedDataIsSorted() {
        List<PacketRule> rules = DataGenerator.generateOrderedRules(500, SEED);
        for (int i = 0; i < rules.size() - 1; i++) {
            assertTrue(rules.get(i).compareTo(rules.get(i + 1)) <= 0,
                    "Dados fora de ordem na posição " + i);
        }
    }

    @Test
    void benchmarkResultStoresValues() {
        BenchmarkResult r = new BenchmarkResult("AVL_Tree", 1000, 500L, 200L, 100L, 42);
        assertEquals("AVL_Tree", r.getTreeName());
        assertEquals(1000, r.getVolume());
        assertEquals(500L, r.getInsertionTime());
        assertEquals(200L, r.getSearchTime());
        assertEquals(100L, r.getDeletionTime());
        assertEquals(42,   r.getRotations());
    }

    @Test
    void benchmarkRunnerReturnsCoherentResultsForAVL() {
        List<PacketRule> rules = DataGenerator.generateOrderedRules(100, SEED);
        BenchmarkResult result = BenchmarkRunner.runPerformanceTest(new AVLRouterTree(), rules, "AVL_Tree");

        assertEquals("AVL_Tree", result.getTreeName());
        assertEquals(100, result.getVolume());
        assertTrue(result.getInsertionTime() > 0);
        assertTrue(result.getSearchTime()    > 0);
        assertTrue(result.getDeletionTime()  >= 0);
        assertTrue(result.getRotations()     > 0);
    }

    @Test
    void benchmarkRunnerReturnsCoherentResultsForRBT() {
        List<PacketRule> rules = DataGenerator.generateOrderedRules(100, SEED);
        BenchmarkResult result = BenchmarkRunner.runPerformanceTest(new RedBlackRouterTree(), rules, "RedBlack_Tree");

        assertEquals("RedBlack_Tree", result.getTreeName());
        assertEquals(100, result.getVolume());
        assertTrue(result.getInsertionTime() > 0);
        assertTrue(result.getSearchTime()    > 0);
        assertTrue(result.getRotations()     > 0);
    }
}
