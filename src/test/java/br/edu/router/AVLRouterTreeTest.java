package br.edu.router;

import br.edu.router.benchmark.DataGenerator;
import br.edu.router.model.PacketRule;
import br.edu.router.tree.avl.AVLRouterTree;
import br.edu.router.validator.AVLValidator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AVLRouterTreeTest {

    private static final long SEED = 42L;

    @Test
    void emptyTreeIsValid() {
        AVLRouterTree tree = new AVLRouterTree();
        assertTrue(AVLValidator.isValid(tree));
        assertEquals(0, tree.size());
        assertEquals(0, tree.height());
    }

    @Test
    void singleInsertionPreservesInvariants() {
        AVLRouterTree tree = new AVLRouterTree();
        tree.insert(new PacketRule(1, "192.168.0.1", "10.0.0.1", 5));

        List<String> violations = AVLValidator.validate(tree);
        assertTrue(violations.isEmpty(), "Violações: " + violations);
        assertEquals(1, tree.size());
        assertEquals(1, tree.height());
    }

    @Test
    void orderedInsertionPreservesInvariants() {
        AVLRouterTree tree = new AVLRouterTree();
        DataGenerator.generateOrderedRules(1000, SEED).forEach(tree::insert);

        List<String> violations = AVLValidator.validate(tree);
        assertTrue(violations.isEmpty(), "Violações após inserção ordenada: " + violations);
        assertEquals(1000, tree.size());
    }

    @Test
    void orderedInsertionTriggersRotations() {
        AVLRouterTree tree = new AVLRouterTree();
        DataGenerator.generateOrderedRules(1000, SEED).forEach(tree::insert);

        assertTrue(tree.getRotationCount() > 0,
                "Inserção ordenada deve gerar rotações de rebalanceamento");
    }

    @Test
    void deletionPreservesInvariants() {
        AVLRouterTree tree = new AVLRouterTree();
        List<PacketRule> rules = DataGenerator.generateOrderedRules(500, SEED);
        rules.forEach(tree::insert);

        int toDelete = (int) (rules.size() * 0.20);
        for (int i = 0; i < toDelete; i++) {
            tree.delete(rules.get(i));
        }

        List<String> violations = AVLValidator.validate(tree);
        assertTrue(violations.isEmpty(), "Violações após deleção de 20%: " + violations);
        assertEquals(500 - toDelete, tree.size());
    }

    @Test
    void heightRespectsBound() {
        AVLRouterTree tree = new AVLRouterTree();
        int n = 1000;
        DataGenerator.generateOrderedRules(n, SEED).forEach(tree::insert);

        double maxAllowed = 1.44 * (Math.log(n + 2) / Math.log(2));
        assertTrue(tree.height() <= maxAllowed,
                "Altura " + tree.height() + " excede o limite teórico AVL de " + maxAllowed);
    }

    @Test
    void searchFindsAllInsertedRules() {
        AVLRouterTree tree = new AVLRouterTree();
        List<PacketRule> rules = DataGenerator.generateOrderedRules(200, SEED);
        rules.forEach(tree::insert);

        rules.forEach(r -> assertTrue(tree.search(r), "Regra não encontrada: " + r));
    }

    @Test
    void searchReturnsFalseForAbsentRule() {
        AVLRouterTree tree = new AVLRouterTree();
        DataGenerator.generateOrderedRules(100, SEED).forEach(tree::insert);

        assertFalse(tree.search(new PacketRule(9999, "1.1.1.1", "2.2.2.2", 99)));
    }
}
