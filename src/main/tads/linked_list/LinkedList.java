package main.tads.linked_list;

/**
 * Representa a implementação da estrutura: Lista Simplesmente Encadeada.
 * <p>
 * Esta classe utiliza objetos do tipo {@link NodeList}
 * para montar a lista, gerando uma sequencia de elementos encadeados.
 * </p>
 *
 * @param <E> o tipo de dado ou objeto que a lista irá armazenar.
 * @see NodeList
 * @author Allan Guilherme
 * @version 1.2
 * @since 2026-03-11
 */
public class LinkedList<E> {
    private NodeList<E> head;
    private int size;

    /**
     * Cria uma lista inicialmente vazia.
     */
    public LinkedList() {
        this.head = null;
        this.size = 0;
    }

    /**
     * Insere um elemento No inicio da lista.
     *
     * @param data elemento a ser inserido.
     */
    public void insertFirst(E data) {
        head = new NodeList<>(data, head);
        size++;
    }

    /**
     * Remove e retorna o primeiro elemento da lista.
     *
     * @return elemento removido do inicio.
     */
    public E removeFirst() {
        if (isEmpty()) {
            throw new IllegalArgumentException("A lista esta vazia.");
        }

        E data = head.getData();
        head = head.getNext();
        size--;
        return data;
    }

    /**
     * Procura um elemento na lista por igualdade.
     *
     * @param data elemento procurado.
     * @return o elemento encontrado ou {@code null} quando inexistente.
     */
    public E searchElement(E data) {
        NodeList<E> currentNode = head;
        while (currentNode != null) {
            if (currentNode.getData().equals(data)) {
                return currentNode.getData();
            } else {
                currentNode = currentNode.getNext();
            }
        }
        return null;
    }

    /**
     * Retorna o primeiro elemento armazenado.
     *
     * @return elemento da cabeca da lista.
     */
    public E getFirstElement() {
        return head.getData();
    }

    /**
     * Informa se a lista esta vazia.
     *
     * @return {@code true} quando nao houver elementos.
     */
    public boolean isEmpty() {
        return this.head == null;
    }

    /**
     * Retorna a quantidade de elementos armazenados.
     *
     * @return tamanho atual da lista.
     */
    public int getSize() {
        return size;
    }

    /**
     * Retorna o No inicial da lista.
     *
     * @return cabeca atual da estrutura.
     */
    public NodeList<E> getHead() {
        return head;
    }

}