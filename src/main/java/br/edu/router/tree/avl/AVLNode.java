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
}