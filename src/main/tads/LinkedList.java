package main.tads;

/**
 * Representa a implementação da estrutura: Lista Simplesmente Encadeada.
 * <p>
 * Esta classe utiliza objetos do tipo {@link NodeList}
 * para montar a lista, gerando uma sequencia de elementos encadeados.
 * </p>
 *
 * @param <T> o tipo de dado ou objeto que a lista irá armazenar.
 * @see NodeList
 * @author Allan
 * @version 1.0
 */
public class LinkedList<T> {
    private NodeList<T> head;
    private int size;

    /**
     * Cria uma lista inicialmente vazia.
     */
    public LinkedList() {
        this.head = null;
        this.size = 0;
    }

    /**
     * Insere um elemento no inicio da lista.
     *
     * @param data elemento a ser inserido.
     */
    public void insertFirst(T data) {
        head = new NodeList<>(data, head);
        size++;
    }

    /**
     * Remove e retorna o primeiro elemento da lista.
     *
     * @return elemento removido do inicio.
     */
    public T removeFirst() {
        if (isEmpty()) {
            throw new IllegalArgumentException("A lista esta vazia.");
        }

        T data = head.getData();
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
    public T searchElement(T data) {
        NodeList<T> currentNode = head;
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
    public T getFirstElement() {
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
     * Retorna o no inicial da lista.
     *
     * @return cabeca atual da estrutura.
     */
    public NodeList<T> getHead() {
        return head;
    }

}