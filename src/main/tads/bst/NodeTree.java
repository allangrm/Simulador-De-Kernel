package main.tads.bst;

import main.tads.linked_list.Node;

/**
 * Representa um No especifico para a construcao de arvores binarias.
 * <p>
 * Esta classe herda de {@link Node} para armazenar os dados. Este No expande
 * a estrutura adicionando duas ramificacoes, permitindo a navegacao baseada
 * em regras de ordenacao.
 * </p>
 *
 * @param <T> o tipo de dado ou objeto que o No ira armazenar.
 * @see Node
 * @author Allan
 * @version 1.0
 * @since 2026-03-11
 */
public class NodeTree<T> extends Node<T> {
    private NodeTree<T> right;
    private NodeTree<T> left;

    /**
     * Cria um No de arvore sem filhos.
     *
     * @param data dado a ser armazenado.
     */
    public NodeTree(T data) {
        super(data);
        this.right = null;
        this.left = null;
    }

    /**
     * Cria um No de arvore com filhos definidos.
     *
     * @param data dado a ser armazenado.
     * @param right referencia para o filho da direita.
     * @param left referencia para o filho da esquerda.
     */
    public NodeTree(T data, NodeTree<T> right, NodeTree<T> left) {
        super(data);
        this.right = right;
        this.left = left;
    }

    /**
     * Retorna o filho da esquerda.
     *
     * @return referencia para o filho esquerdo.
     */
    public NodeTree<T> getLeft() {
        return left;
    }

    /**
     * Retorna o filho da direita.
     *
     * @return referencia para o filho direito.
     */
    public NodeTree<T> getRight() {
        return right;
    }

    /**
     * Atualiza o filho da esquerda.
     *
     * @param left nova referencia para o filho esquerdo.
     */
    public void setLeft(NodeTree<T> left) {
        this.left = left;
    }

    /**
     * Atualiza o filho da direita.
     *
     * @param right nova referencia para o filho direito.
     */
    public void setRight(NodeTree<T> right) {
        this.right = right;
    }
}
