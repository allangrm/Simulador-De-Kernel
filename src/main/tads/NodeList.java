package main.tads;

/**
 * Representa um No especifico para estruturas de dados lineares.
 * <p>
 * Esta classe herda de {@link Node} para armazenar os dados e
 * adiciona a capacidade de encadeamento atraves de uma referencia para o
 * proximo No da sequencia.
 * </p>
 *
 * @param <T> o tipo de dado ou objeto que o No irá armazenar.
 * @see Node
 * @author Allan
 * @version 1.0
 * @since 2026-03-11
 */
public class NodeList<T> extends Node<T> {
    private NodeList<T> next;

    /**
     * Cria um No isolado para lista encadeada.
     *
     * @param data dado a ser armazenado.
     */
    public NodeList(T data) {
        super(data);
        this.next = null;
    }

    /**
     * Cria um No ja encadeado ao proximo elemento.
     *
     * @param data dado a ser armazenado.
     * @param next referencia para o proximo No.
     */
    public NodeList(T data, NodeList<T> next) {
        super(data);
        this.next = next;
    }

    /**
     * Retorna o proximo No da sequencia.
     *
     * @return referencia para o proximo No.
     */
    public NodeList<T> getNext() {
        return next;
    }

    /**
     * Atualiza a referencia para o proximo No.
     *
     * @param next Novo proximo No da sequencia.
     */
    public void setNext(NodeList<T> next) {
        this.next = next;
    }
}
