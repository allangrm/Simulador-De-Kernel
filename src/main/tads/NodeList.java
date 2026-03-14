package main.tads;

/**
 * Representa um no especifico para estruturas de dados lineares.
 * <p>
 * Esta classe herda de {@link Node} para armazenar os dados e
 * adiciona a capacidade de encadeamento atraves de uma referencia para o
 * proximo no da sequencia.
 * </p>
 *
 * @param <T> o tipo de dado ou objeto que o nó irá armazenar.
 * @see Node
 * @author Allan
 * @version 1.0
 */
public class NodeList<T> extends Node<T> {
    private NodeList<T> next;

    /**
     * Cria um no isolado para lista encadeada.
     *
     * @param data dado a ser armazenado.
     */
    public NodeList(T data) {
        super(data);
        this.next = null;
    }

    /**
     * Cria um no ja encadeado ao proximo elemento.
     *
     * @param data dado a ser armazenado.
     * @param next referencia para o proximo no.
     */
    public NodeList(T data, NodeList<T> next) {
        super(data);
        this.next = next;
    }

    /**
     * Retorna o proximo no da sequencia.
     *
     * @return referencia para o proximo no.
     */
    public NodeList<T> getNext() {
        return next;
    }

    /**
     * Atualiza a referencia para o proximo no.
     *
     * @param next novo proximo no da sequencia.
     */
    public void setNext(NodeList<T> next) {
        this.next = next;
    }
}
