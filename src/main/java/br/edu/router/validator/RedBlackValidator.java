package br.edu.router.validator;

import br.edu.router.model.PacketRule;
import br.edu.router.tree.redblack.Color;
import br.edu.router.tree.redblack.RedBlackNode;
import br.edu.router.tree.redblack.RedBlackRouterTree;

import java.util.ArrayList;
import java.util.List;

public class RedBlackValidator {

    public static boolean isValid(RedBlackRouterTree tree) {
        return validate(tree).isEmpty();
    }

    public static List<String> validate(RedBlackRouterTree tree) {
        List<String> violations = new ArrayList<>();
        RedBlackNode root = tree.getRoot();

        if (root == null) return violations;

        // Propriedade 2: a raiz deve ser PRETA
        if (root.getColor() != Color.BLACK) {
            violations.add("Prop. 2: a raiz é VERMELHA (deve ser PRETA).");
        }

        checkNode(root, null, null, violations);
        return violations;
    }

    public static void printReport(RedBlackRouterTree tree) {
        List<String> violations = validate(tree);
        System.out.println("=== Relatório de Validação RBT ===");
        System.out.println("Altura da árvore : " + tree.height());
        System.out.println("Total de nós     : " + tree.size());
        System.out.println("Total de rotações: " + tree.getRotationCount());
        if (violations.isEmpty()) {
            System.out.println("Status: VÁLIDA — todas as 5 propriedades RBT respeitadas.");
        } else {
            System.out.println("Status: INVÁLIDA — " + violations.size() + " violação(ões):");
            violations.forEach(v -> System.out.println("  [!] " + v));
        }
        System.out.println("===================================\n");
    }

    /**
     * Valida a subárvore e retorna sua altura-negra.
     * Verifica BST, propriedades de cor e ponteiros pai.
     */
    private static int checkNode(RedBlackNode node, PacketRule minBound, PacketRule maxBound,
                                 List<String> violations) {
        if (node == null) return 1;

        PacketRule rule = node.getRule();

        // Invariante BST (bounds globais propagados pela recursão)
        if (minBound != null && rule.compareTo(minBound) <= 0) {
            violations.add("BST violada: nó " + describe(node)
                    + " não é maior que o limite mínimo " + describeRule(minBound));
        }
        if (maxBound != null && rule.compareTo(maxBound) >= 0) {
            violations.add("BST violada: nó " + describe(node)
                    + " não é menor que o limite máximo " + describeRule(maxBound));
        }

        // Propriedade 4: nó VERMELHO deve ter apenas filhos PRETOS
        if (node.getColor() == Color.RED) {
            if (colorOf(node.getLeft()) == Color.RED) {
                violations.add("Prop. 4: " + describe(node)
                        + " VERMELHO tem filho esquerdo VERMELHO.");
            }
            if (colorOf(node.getRight()) == Color.RED) {
                violations.add("Prop. 4: " + describe(node)
                        + " VERMELHO tem filho direito VERMELHO.");
            }
        }

        // Ponteiros pai
        if (node.getLeft() != null && node.getLeft().getParent() != node) {
            violations.add("Ponteiro pai inválido: filho esquerdo de " + describe(node)
                    + " não aponta de volta para o pai.");
        }
        if (node.getRight() != null && node.getRight().getParent() != node) {
            violations.add("Ponteiro pai inválido: filho direito de " + describe(node)
                    + " não aponta de volta para o pai.");
        }

        int leftBH  = checkNode(node.getLeft(),  minBound, rule,     violations);
        int rightBH = checkNode(node.getRight(), rule,     maxBound, violations);

        // Propriedade 5: altura-negra igual nos dois lados
        if (leftBH != rightBH) {
            violations.add("Prop. 5: altura-negra desigual em " + describe(node)
                    + " (esq=" + leftBH + ", dir=" + rightBH + ").");
        }

        return (node.getColor() == Color.BLACK ? 1 : 0) + leftBH;
    }

    private static Color colorOf(RedBlackNode node) {
        return node == null ? Color.BLACK : node.getColor();
    }

    private static String describe(RedBlackNode node) {
        return "[ID=" + node.getRule().getId() + " P=" + node.getRule().getPriority()
                + " " + node.getColor() + "]";
    }

    private static String describeRule(PacketRule r) {
        return "[ID=" + r.getId() + " P=" + r.getPriority() + "]";
    }
}
