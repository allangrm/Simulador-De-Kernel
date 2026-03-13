package main.tads;

/**
 * Representa um Nó específico para estruturas de dados lineares
 * <p>
 * Esta classe herda de {@link Node} para armazenar os dados e
 * adiciona a capacidade de encadeamento através de uma referência para o
 * próximo nó da sequência.
 * </p>
 *
 * @param <T> o tipo de dado ou objeto que o nó irá armazenar.
 * @see Node
 * @author Allan
 * @version 1.0
 */
public class NodeList<T> extends Node<T> {
    private NodeList<T> next;

    public NodeList(T data){
        super(data);
        this.next = null;
    }

    public NodeList(T data, NodeList<T> next){
        super(data);
        this.next = next;
    }

    public NodeList<T> getNext(){
        return next;
    }

    public void setNext(NodeList<T> next) {
        this.next = next;
    }
}
