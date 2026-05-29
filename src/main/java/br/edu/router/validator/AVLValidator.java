package br.edu.router.validator;

import br.edu.router.model.PacketRule;
import br.edu.router.tree.avl.AVLNode;
import br.edu.router.tree.avl.AVLRouterTree;

import java.util.ArrayList;
import java.util.List;

public class AVLValidator {

    public static boolean isValid(AVLRouterTree tree) {
        return validate(tree).isEmpty();
    }

    public static List<String> validate(AVLRouterTree tree) {
        List<String> violations = new ArrayList<>();
        checkNode(tree.getRoot(), null, null, violations);
        return violations;
    }

    public static void printReport(AVLRouterTree tree) {
        List<String> violations = validate(tree);
        System.out.println("=== Relatório de Validação AVL ===");
        System.out.println("Altura da árvore : " + tree.height());
        System.out.println("Total de nós     : " + tree.size());
        System.out.println("Total de rotações: " + tree.getRotationCount());
        if (violations.isEmpty()) {
            System.out.println("Status: VÁLIDA — todas as invariantes respeitadas.");
        } else {
            System.out.println("Status: INVÁLIDA — " + violations.size() + " violação(ões):");
            violations.forEach(v -> System.out.println("  [!] " + v));
        }
        System.out.println("==================================\n");
    }

    /**
    * Valida a subárvore e retorna sua altura real.
    * Verifica BST, balanceamento e altura dos nós.
    */
    private static int checkNode(AVLNode node, PacketRule minBound, PacketRule maxBound,
                                 List<String> violations) {
        if (node == null) return 0;

        PacketRule rule = node.getRule();

        // Invariante 1: Propriedade BST (bounds globais propagados pela recursão)
        if (minBound != null && rule.compareTo(minBound) <= 0) {
            violations.add("BST violada: nó " + describe(rule)
                    + " não é maior que o limite mínimo " + describe(minBound));
        }
        if (maxBound != null && rule.compareTo(maxBound) >= 0) {
            violations.add("BST violada: nó " + describe(rule)
                    + " não é menor que o limite máximo " + describe(maxBound));
        }

        int leftHeight  = checkNode(node.getLeft(),  minBound, rule,     violations);
        int rightHeight = checkNode(node.getRight(), rule,     maxBound, violations);

        // Invariante 2: Fator de Equilíbrio
        int fb = leftHeight - rightHeight;
        if (Math.abs(fb) > 1) {
            violations.add("FB violado: |FB|=" + Math.abs(fb) + " no nó " + describe(rule));
        }

        // Invariante 3: Altura armazenada
        int expectedHeight = 1 + Math.max(leftHeight, rightHeight);
        if (node.getHeight() != expectedHeight) {
            violations.add("Altura incorreta: esperado=" + expectedHeight
                    + ", armazenado=" + node.getHeight() + " no nó " + describe(rule));
        }

        return expectedHeight;
    }

    private static String describe(PacketRule r) {
        return "[ID=" + r.getId() + " P=" + r.getPriority() + "]";
    }
}
