package br.edu.router.tree.avl;

import br.edu.router.model.PacketRule;

public class AVLNode {

    PacketRule rule;
    AVLNode left;
    AVLNode right;
    int height;

    public AVLNode(PacketRule rule) {
        this.rule = rule;
        this.height = 1;
    }

    public PacketRule getRule() {
        return rule;
    }

    public AVLNode getLeft() {
        return left;
    }

    public AVLNode getRight() {
        return right;
    }

    public int getHeight() {
        return height;
    }
}
