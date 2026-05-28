package br.edu.router.tree.redblack;

import br.edu.router.model.PacketRule;

public class RedBlackNode {
    PacketRule rule;
    RedBlackNode left;
    RedBlackNode right;
    RedBlackNode parent;
    Color color;

    RedBlackNode(PacketRule rule) {
        this.rule = rule;
        this.color = Color.RED;
    }
}