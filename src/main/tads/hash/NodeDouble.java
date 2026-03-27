package main.tads.hash;

import main.tads.Node;

/**
 * Representa um No especifico para a construcao da Lista Duplamente Encadeada.
 * <p>
 * Esta classe herda de {@link Node} para armazenar os dados, este No expande
 * a estrutura adicionando as referencias para o No anterior e para o proximo,
 * permitindo a navegacao bidirecional continua atraves da lista.
 * </p>
 *
 * @param <T> o tipo de dado ou objeto que o No ira armazenar.
 * @see Node
 * @author Allan
 * @version 1.0
 * @since 2026-03-14
 */
public class NodeDouble<T> extends Node<T>{
    private NodeDouble<T> next;
    private NodeDouble<T> previous;

    /**
     * Cria um No de ponteiro duplo isolado para lista encadeada.
     *
     * @param data dado a ser armazenado.
     */
    public NodeDouble(T data){
        super(data);
        this.next = null;
        this.previous = null;
    }

    /**
     * Cria um No ja encadeado com os elementos: sucessor e antecessor.
     *
     * @param data dado a ser armazenado.
     * @param next referencia para o proximo No.
     * @param previous referencia para o No anterior.
     */
    public NodeDouble(T data, NodeDouble<T> next, NodeDouble<T> previous ){
        super(data);
        this.next = next;
        this.previous = previous;
    }

    /**
     * Retorna o No anterior da sequencia.
     *
     * @return referencia para o No anterior.
     */
    public NodeDouble<T> getPrevious() {
        return previous;
    }

    /**
     * Retorna o proximo No da sequencia.
     *
     * @return referencia para o proximo No.
     */
    public NodeDouble<T> getNext() {
        return next;
    }

    /**
     * Atualiza a referencia para o No anterior.
     *
     * @param previous Novo No antecessor da sequencia.
     */
    public void setPrevious(NodeDouble<T> previous) {
        this.previous = previous;
    }

    /**
     * Atualiza a referencia para o proximo No.
     *
     * @param next Novo proximo No da sequencia.
     */
    public void setNext(NodeDouble<T> next) {
        this.next = next;
    }
}
