package br.edu.router.tree;

import br.edu.router.model.PacketRule;

public interface RouterTree {

    void insert(PacketRule rule);

    boolean search(int id);

    void delete(int id);

    int getRotationCount();

    int size();

    int height();
}