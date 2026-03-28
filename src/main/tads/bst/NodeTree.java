package main.tads.bst;

import main.tads.Node;

/**
 * Representa um Nó específico para a construção de árvores binárias.
 * <p>
 * Esta classe herda de {@link Node} para armazenar os dados. Este Nó expande
 * a estrutura adicionando duas ramificações, permitindo a navegação baseada
 * em regras de ordenação.
 * </p>
 *
 * @param <T> o tipo de dado ou objeto que o Nó irá armazenar.
 * @see Node
 * @author Allan, Lucas Nóbrega
 * @version 2.0
 * @since 2026-03-11
 */
public class NodeTree<T> extends Node<T> {
    private NodeTree<T> right;
    private NodeTree<T> left;
    private NodeTree<T> parent;
    private int height;

    /**
     * Cria um Nó de árvore sem filhos.
     *
     * @param data dado a ser armazenado.
     */
    public NodeTree(T data) {
        super(data);
        this.right = null;
        this.left = null;
        this.parent = null;
        this.height = data == null ? -1 : 0;
    }

    /**
     * Cria um Nó de árvore com filhos definidos.
     *
     * @param data dado a ser armazenado.
     * @param right referencia para o filho da direita.
     * @param left referencia para o filho da esquerda.
     */
    public NodeTree(T data, NodeTree<T> right, NodeTree<T> left) {
        super(data);
        this.parent = null;
        setRight(right == null ? new NodeTree<>(null) : right);
        setLeft(left == null ? new NodeTree<>(null) : left);
        updateHeight();
    }

    /**
     * Cria um Nó de árvore com dado, filhos e pai definidos.
     *
     * @param data dado a ser armazenado.
     * @param right referencia para o filho da direita.
     * @param left referencia para o filho da esquerda.
     * @param parent referência para o Nó pai.
     */
    public NodeTree(T data, NodeTree<T> right, NodeTree<T> left, NodeTree<T> parent) {
        super(data);
        this.setParent(parent);
        this.setRight(right == null ? new NodeTree<>(null) : right);
        this.setLeft(left == null ? new NodeTree<>(null) : left);
        this.updateHeight();
    }

    /**
     * Retorna o filho da esquerda.
     *
     * @return referência para o filho esquerdo.
     */
    public NodeTree<T> getLeft() {
        return left;
    }

    /**
     * Retorna o filho da direita.
     *
     * @return referência para o filho direito.
     */
    public NodeTree<T> getRight() {
        return right;
    }

    /**
     * Atualiza o filho da esquerda.
     *
     * @param left nova referência para o filho esquerdo.
     */
    public void setLeft(NodeTree<T> left) {
        if (left == null) {
            throw new IllegalArgumentException("O filho esquerdo de um Nó nao pode ser nulo.");
        }

        this.left = left;
        if (!left.isNil()) {
            left.setParent(this);
        }
        updateHeight();
    }

    /**
     * Atualiza o filho da direita.
     *
     * @param right nova referência para o filho direito.
     */
    public void setRight(NodeTree<T> right) {
        if (right == null) {
            throw new IllegalArgumentException("O filho direito de um Nó nao pode ser nulo.");
        }

        this.right = right;
        if (!right.isNil()) {
            right.setParent(this);
        }
        updateHeight();
    }

    /**
     * Retorna o Nó pai.
     *
     * @return referência para o Nó pai.
     */
    public NodeTree<T> getParent() {
        return parent;
    }

    /**
     * Atualiza o Nó pai.
     *
     * @param parent nova referência para o Nó pai.
     */
    public void setParent(NodeTree<T> parent) {
        this.parent = parent;
    }

    /**
     * Retorna a altura atual armazenada no Nó.
     *
     * @return altura atual do Nó.
     */
    public int height() {
        return height;
    }

    /**
     * Recalcula a altura do Nó com base na altura dos filhos.
     * <p>
     * Um Nó folha possui altura 0. Um Nó nulo de sentinela (data nula)
     * utiliza altura -1 para facilitar os cálculos de balanceamento.
     * </p>
     */
    public void updateHeight() {
        if (isNil()) {
            this.height = -1;
            return;
        }

        int leftHeight = this.left == null ? -1 : this.left.height();
        int rightHeight = this.right == null ? -1 : this.right.height();
        this.height = 1 + Math.max(leftHeight, rightHeight);
    }
}
