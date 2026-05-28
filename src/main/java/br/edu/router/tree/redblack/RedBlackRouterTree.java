package br.edu.router.tree.redblack;

import br.edu.router.model.PacketRule;
import br.edu.router.tree.RouterTree;

public class RedBlackRouterTree implements RouterTree {
    private RedBlackNode root;
    private int size;
    private int rotationCount;

    @Override
    public void insert(PacketRule rule) {
        if (rule == null) {
            throw new IllegalArgumentException("A regra nao pode ser nula.");
        }

        RedBlackNode newNode = new RedBlackNode(rule);
        RedBlackNode parent = null;
        RedBlackNode current = root;

        while (current != null) {
            parent = current;

            int comparison = rule.compareTo(current.rule);

            if (comparison < 0) {
                current = current.left;
            } else if (comparison > 0) {
                current = current.right;
            } else {
                current.rule = rule;
                return;
            }
        }

        newNode.parent = parent;

        if (parent == null) {
            root = newNode;
        } else if (rule.compareTo(parent.rule) < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }

        size++;
        fixInsert(newNode);
    }

    @Override
    public boolean search(PacketRule rule) {
        if (rule == null) {
            return false;
        }

        return searchNode(root, rule) != null;
    }

    private RedBlackNode searchNode(RedBlackNode node, PacketRule rule) {
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
            throw new IllegalArgumentException("A regra nao pode ser nula.");
        }

        RedBlackNode node = searchNode(root, rule);

        if (node == null) {
            return;
        }

        deleteNode(node);
        size--;

        if (root != null) {
            root.color = Color.BLACK;
        }
    }

    private void deleteNode(RedBlackNode node) {
        if (node.left != null && node.right != null) {
            RedBlackNode successor = findMin(node.right);
            node.rule = successor.rule;
            node = successor;
        }

        RedBlackNode replacement = node.left != null ? node.left : node.right;

        if (replacement != null) {
            replacement.parent = node.parent;

            if (node.parent == null) {
                root = replacement;
            } else if (node == node.parent.left) {
                node.parent.left = replacement;
            } else {
                node.parent.right = replacement;
            }

            if (node.color == Color.BLACK) {
                fixDelete(replacement);
            }
        } else if (node.parent == null) {
            root = null;
        } else {
            if (node.color == Color.BLACK) {
                fixDelete(node);
            }

            if (node.parent != null) {
                if (node == node.parent.left) {
                    node.parent.left = null;
                } else {
                    node.parent.right = null;
                }
            }
        }
    }

    private void fixInsert(RedBlackNode node) {
        while (node != root && colorOf(parentOf(node)) == Color.RED) {
            RedBlackNode parent = parentOf(node);
            RedBlackNode grandparent = parentOf(parent);

            if (parent == leftOf(grandparent)) {
                RedBlackNode uncle = rightOf(grandparent);

                if (colorOf(uncle) == Color.RED) {
                    setColor(parent, Color.BLACK);
                    setColor(uncle, Color.BLACK);
                    setColor(grandparent, Color.RED);
                    node = grandparent;
                } else {
                    if (node == rightOf(parent)) {
                        node = parent;
                        rotateLeft(node);
                    }

                    setColor(parentOf(node), Color.BLACK);
                    setColor(parentOf(parentOf(node)), Color.RED);
                    rotateRight(parentOf(parentOf(node)));
                }
            } else {
                RedBlackNode uncle = leftOf(grandparent);

                if (colorOf(uncle) == Color.RED) {
                    setColor(parent, Color.BLACK);
                    setColor(uncle, Color.BLACK);
                    setColor(grandparent, Color.RED);
                    node = grandparent;
                } else {
                    if (node == leftOf(parent)) {
                        node = parent;
                        rotateRight(node);
                    }

                    setColor(parentOf(node), Color.BLACK);
                    setColor(parentOf(parentOf(node)), Color.RED);
                    rotateLeft(parentOf(parentOf(node)));
                }
            }
        }

        root.color = Color.BLACK;
    }

    private void fixDelete(RedBlackNode node) {
        while (node != root && colorOf(node) == Color.BLACK) {
            if (node == leftOf(parentOf(node))) {
                RedBlackNode sibling = rightOf(parentOf(node));

                if (colorOf(sibling) == Color.RED) {
                    setColor(sibling, Color.BLACK);
                    setColor(parentOf(node), Color.RED);
                    rotateLeft(parentOf(node));
                    sibling = rightOf(parentOf(node));
                }

                if (colorOf(leftOf(sibling)) == Color.BLACK && colorOf(rightOf(sibling)) == Color.BLACK) {
                    setColor(sibling, Color.RED);
                    node = parentOf(node);
                } else {
                    if (colorOf(rightOf(sibling)) == Color.BLACK) {
                        setColor(leftOf(sibling), Color.BLACK);
                        setColor(sibling, Color.RED);
                        rotateRight(sibling);
                        sibling = rightOf(parentOf(node));
                    }

                    setColor(sibling, colorOf(parentOf(node)));
                    setColor(parentOf(node), Color.BLACK);
                    setColor(rightOf(sibling), Color.BLACK);
                    rotateLeft(parentOf(node));
                    node = root;
                }
            } else {
                RedBlackNode sibling = leftOf(parentOf(node));

                if (colorOf(sibling) == Color.RED) {
                    setColor(sibling, Color.BLACK);
                    setColor(parentOf(node), Color.RED);
                    rotateRight(parentOf(node));
                    sibling = leftOf(parentOf(node));
                }

                if (colorOf(rightOf(sibling)) == Color.BLACK && colorOf(leftOf(sibling)) == Color.BLACK) {
                    setColor(sibling, Color.RED);
                    node = parentOf(node);
                } else {
                    if (colorOf(leftOf(sibling)) == Color.BLACK) {
                        setColor(rightOf(sibling), Color.BLACK);
                        setColor(sibling, Color.RED);
                        rotateLeft(sibling);
                        sibling = leftOf(parentOf(node));
                    }

                    setColor(sibling, colorOf(parentOf(node)));
                    setColor(parentOf(node), Color.BLACK);
                    setColor(leftOf(sibling), Color.BLACK);
                    rotateRight(parentOf(node));
                    node = root;
                }
            }
        }

        setColor(node, Color.BLACK);
    }

    private void rotateLeft(RedBlackNode node) {
        RedBlackNode newRoot = node.right;
        node.right = newRoot.left;

        if (newRoot.left != null) {
            newRoot.left.parent = node;
        }

        newRoot.parent = node.parent;

        if (node.parent == null) {
            root = newRoot;
        } else if (node == node.parent.left) {
            node.parent.left = newRoot;
        } else {
            node.parent.right = newRoot;
        }

        newRoot.left = node;
        node.parent = newRoot;
        rotationCount++;
    }

    private void rotateRight(RedBlackNode node) {
        RedBlackNode newRoot = node.left;
        node.left = newRoot.right;

        if (newRoot.right != null) {
            newRoot.right.parent = node;
        }

        newRoot.parent = node.parent;

        if (node.parent == null) {
            root = newRoot;
        } else if (node == node.parent.right) {
            node.parent.right = newRoot;
        } else {
            node.parent.left = newRoot;
        }

        newRoot.right = node;
        node.parent = newRoot;
        rotationCount++;
    }

    private RedBlackNode findMin(RedBlackNode node) {
        RedBlackNode current = node;

        while (current.left != null) {
            current = current.left;
        }

        return current;
    }

    private int height(RedBlackNode node) {
        if (node == null) {
            return 0;
        }

        return 1 + Math.max(height(node.left), height(node.right));
    }

    private Color colorOf(RedBlackNode node) {
        return node == null ? Color.BLACK : node.color;
    }

    private RedBlackNode parentOf(RedBlackNode node) {
        return node == null ? null : node.parent;
    }

    private RedBlackNode leftOf(RedBlackNode node) {
        return node == null ? null : node.left;
    }

    private RedBlackNode rightOf(RedBlackNode node) {
        return node == null ? null : node.right;
    }

    private void setColor(RedBlackNode node, Color color) {
        if (node != null) {
            node.color = color;
        }
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

    public RedBlackNode getRoot() {
        return root;
    }
}
