package br.edu.router;

/* Teste temporário apenas para validar se a lógica funcionou de maneira correta e coerente */

import br.edu.router.model.PacketRule;
import br.edu.router.tree.avl.AVLRouterTree;
import br.edu.router.tree.redblack.RedBlackRouterTree;

public class Main {
    public static void main(String[] args) {
        AVLRouterTree avl = new AVLRouterTree();
        RedBlackRouterTree rb = new RedBlackRouterTree();

        PacketRule rule1 = new PacketRule(1, "10.0.0.1", "10.0.0.2", 5);
        PacketRule rule2 = new PacketRule(2, "10.0.0.3", "10.0.0.4", 10);
        PacketRule rule3 = new PacketRule(3, "10.0.0.5", "10.0.0.6", 3);

        avl.insert(rule1);
        avl.insert(rule2);
        avl.insert(rule3);

        rb.insert(rule1);
        rb.insert(rule2);
        rb.insert(rule3);

        System.out.println("AVL tamanho: " + avl.size());
        System.out.println("AVL busca rule2: " + avl.search(rule2));

        avl.delete(rule2);

        System.out.println("AVL tamanho apos delete: " + avl.size());
        System.out.println("AVL busca rule2 apos delete: " + avl.search(rule2));

        System.out.println("RB tamanho: " + rb.size());
        System.out.println("RB busca rule2: " + rb.search(rule2));

        rb.delete(rule2);

        System.out.println("RB tamanho apos delete: " + rb.size());
        System.out.println("RB busca rule2 apos delete: " + rb.search(rule2));
    }
}