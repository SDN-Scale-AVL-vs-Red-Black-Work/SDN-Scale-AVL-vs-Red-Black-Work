package br.edu.router.tree.avl;

import br.edu.router.model.PacketRule;
import br.edu.router.tree.RouterTree;

public class AVLRouterTree implements RouterTree {

    private AVLNode root;
    private int size;
    private int rotationCount;

    @Override
    public void insert(PacketRule rule) {
        if (rule == null) {
            throw new IllegalArgumentException("A regra não pode ser nula.");
        }

        root = insert(root, rule);
    }

    private AVLNode insert(AVLNode node, PacketRule rule) {
        if (node == null) {
            size++;
            return new AVLNode(rule);
        }

        int comparison = rule.compareTo(node.rule);

        if (comparison < 0) {
            node.left = insert(node.left, rule);
        } else if (comparison > 0) {
            node.right = insert(node.right, rule);
        } else {
            node.rule = rule;
            return node;
        }

        updateHeight(node);
        return balance(node);
    }

    @Override
    public boolean search(PacketRule rule) {
        if (rule == null) {
        return false;
        }

        return searchNode(root, rule) != null;
    }

    private AVLNode searchNode(AVLNode node, PacketRule rule) {
        while (node != null) {
            int comparison = rule.compareTo(node.rule);

            if (comparison < 0) {
                node = node.left;
            } else if (comparison > 0) {
                node = node.right;
            } else {
                return node;
            }
        }

        return null;
    }

    @Override
    public void delete(PacketRule rule) {
        if (rule == null) {
            throw new IllegalArgumentException("A regra não pode ser nula.");
        }

        root = delete(root, rule);
    }

    private AVLNode delete(AVLNode node, PacketRule rule) {
        if (node == null) {
            return null;
        }

        int comparison = rule.compareTo(node.rule);

        if (comparison < 0) {
            node.left = delete(node.left, rule);
        } else if (comparison > 0) {
            node.right = delete(node.right, rule);
        } else {
            if (node.left == null || node.right == null) {
                size--;
                return node.left != null ? node.left : node.right;
            }

            AVLNode successor = findMin(node.right);
            node.rule = successor.rule;
            node.right = delete(node.right, successor.rule);
        }

        updateHeight(node);
        return balance(node);
    }

    private AVLNode findMin(AVLNode node) {
        AVLNode current = node;

        while (current.left != null) {
            current = current.left;
        }

        return current;
    }

    private AVLNode balance(AVLNode node) {
        int balanceFactor = getBalanceFactor(node);

        if (balanceFactor > 1) {
            if (getBalanceFactor(node.left) < 0) {
                node.left = rotateLeft(node.left);
            }

            return rotateRight(node);
        }

        if (balanceFactor < -1) {
            if (getBalanceFactor(node.right) > 0) {
                node.right = rotateRight(node.right);
            }

            return rotateLeft(node);
        }

        return node;
    }

    private AVLNode rotateRight(AVLNode y) {
        AVLNode x = y.left;
        AVLNode temp = x.right;

        x.right = y;
        y.left = temp;

        updateHeight(y);
        updateHeight(x);

        rotationCount++;

        return x;
    }

    private AVLNode rotateLeft(AVLNode x) {
        AVLNode y = x.right;
        AVLNode temp = y.left;

        y.left = x;
        x.right = temp;

        updateHeight(x);
        updateHeight(y);

        rotationCount++;

        return y;
    }

    private void updateHeight(AVLNode node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }

    private int height(AVLNode node) {
        return node == null ? 0 : node.height;
    }

    private int getBalanceFactor(AVLNode node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    @Override
    public int getRotationCount() {
        return rotationCount;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int height() {
        return height(root);
    }

    public AVLNode getRoot() {
        return root;
    }
}
