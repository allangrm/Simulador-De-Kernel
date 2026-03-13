package main.tads;

/**
 * Representa um Nó específico para a construção de Árvores Binárias.
 * <p>
 * Esta classe herda de {@link Node} para armazenar os dados, este nó expande
 * a estrutura adicionando duas ramificações (filho à esquerda e filho à direita),
 * permitindo a navegação baseada em regras de ordenação.
 * </p>
 *
 * @param <T> o tipo de dado ou objeto que o nó irá armazenar.
 * @see Node
 * @author Allan
 * @version 1.0
 */
public class NodeTree<T> extends Node<T>{
    private NodeTree<T> right;
    private NodeTree<T> left;

    public NodeTree(T data){
        super(data);
        this.right = null;
        this.left = null;
    }
    public NodeTree(T data, NodeTree<T> right, NodeTree<T> left ){
        super(data);
        this.right = right;
        this.left = left;
    }

    public NodeTree<T> getLeft() {
        return left;
    }

    public NodeTree<T> getRight() {
        return right;
    }

    public void setLeft(NodeTree<T> left) {
        this.left = left;
    }

    public void setRight(NodeTree<T> right) {
        this.right = right;
    }
}
