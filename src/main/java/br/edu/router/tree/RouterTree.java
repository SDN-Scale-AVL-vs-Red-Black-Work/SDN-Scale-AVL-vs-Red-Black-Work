package br.edu.router.tree;

import br.edu.router.model.PacketRule;

public interface RouterTree {

    void insert(PacketRule rule);

    boolean search(PacketRule rule);

    void delete(PacketRule rule);

    int getRotationCount();

    int size();

    int height();
}