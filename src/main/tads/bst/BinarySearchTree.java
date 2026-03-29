package main.tads.bst;

/**
 * Define o contrato de uma árvore binária de busca.
 *
 * @param <T> tipo de dado armazenado nos Nós da árvore.
 * @author Lucas Nóbrega
 * @version 1.0
 * @since 2026-03-27
 */
public interface BinarySearchTree<T> {
    NodeTree<T> getRoot();
    boolean isEmpty();
    int height();
    NodeTree<T> search(NodeTree<T> k);
    void insert(NodeTree<T> x);
    void remove(NodeTree<T> x);
    NodeTree<T>[] preOrder();
    NodeTree<T>[] inOrder();
    NodeTree<T>[] postOrder();
    int size();
    NodeTree<T> maximum();
    NodeTree<T> minimum();
    NodeTree<T> successor(NodeTree<T> w);
    NodeTree<T> predecessor(NodeTree<T> w);
}
