package main.tads.hash;
/**
 * Representa a implementação da estrutura: Lista Duplamente Encadeada
 * com comportamento cíclico.
 * <p>
 * Esta classe utiliza de objetos do tipo {@link NodeDouble}
 * para montar a Lista, gerando uma sequência de elementos duplamente encadeados,
 * juntamente da abordagem cíclica conectando {@code head} e {@code tail}.
 * </p>
 *
 * @param <E> o tipo de dado ou objeto que o No irá armazenar.
 * @see NodeDouble
 * @author Allan
 * @version 1.1
 * @since 2026-03-14
 */
public class DoublyCircularLinkedList<E> {
    private NodeDouble<E> head;
    private NodeDouble<E> tail;
    private int size;


    /**
     * Cria uma lista inicialmente vazia.
     */
    public DoublyCircularLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    /**
     * Insere o elemento na posição do primeiro No da lista.
     * <p>
     * Se a lista estiver vazia, o novo No se torna simultaneamente {@code head} e {@code tail},
     * apontando para si mesmo, e iniciando a lista.
     * Caso a lista ja possua elementos, o novo No sera definido como
     * {@code previous} da atual {@code head} e {@code next} da atual {@code tail},
     * no final define a nova {@code head} como o novo No,
     * mantendo a  lista ciclica.
     * </p>
     *
     * @param data dado a ser inserido no início da lista.
     */
    public void insertFirst(E data){
        NodeDouble<E> newNode = new NodeDouble<>(data);

        if(isEmpty()) {
            head = newNode;
            tail = newNode;

            head.setNext(head);
            tail.setPrevious(head);
        }
        else{
            newNode.setNext(head);
            newNode.setPrevious(tail);

            head.setPrevious(newNode);
            tail.setNext(newNode);

            head = newNode;
        }
        size++;
    }

    /**
     * Insere o elemento na posição do último No da lista.
     * <p>
     * Se a lista estiver vazia, o novo No se torna simultaneamente {@code head} e {@code tail},
     * apontando para si mesmo, iniciando a lista.
     * Caso a lista ja possua elementos, o novo No sera definido como
     * {@code previous} da atual {@code head} e {@code next} da atual {@code tail},
     * no final define a nova {@code tail} como o novo No,
     * mantendo a lista cíclica.
     * </p>
     *
     * @param data dado a ser inserido no final da lista.
     */
    public void insertLast(E data){
        NodeDouble<E> newNode = new NodeDouble<>(data);
        if(isEmpty()) {
            head = newNode;
            tail = newNode;

            head.setNext(tail);
            tail.setPrevious(tail);
        }
        else{
            newNode.setNext(head);
            newNode.setPrevious(tail);

            head.setPrevious(newNode);
            tail.setNext(newNode);

            tail = newNode;
        }
        size++;
    }

    /**
     * Remove o elemento na posição do primeiro No da lista.
     * <p>
     * Se a lista tiver apenas um No, a {@code head} e a {@code tail} serão simultaneamente
     * excluídas, deixando a lista vazia.
     * Caso a lista possua múltiplos elementos, o No é removido (Garbage Collector) e o segundo elemento
     * será definido como a nova {@code head}, os encadeamentos serão refeitos com a {@code tail}
     * mantendo a lista cíclica.
     * </p>
     * @return o dado que estava armazenado no No removido.
     */
    public E removeFirst(){
        if(isEmpty()) throw new IllegalArgumentException("Lista está vazia");

        E data = head.getData();

        if(head == tail) {
            head = null;
            tail = null;
        }else {
            head = head.getNext();
            head.setPrevious(tail);
            tail.setNext(head);
        }
        size--;
        return data;
    }

    /**
     * Remove o elemento na posição do último No da lista (Tail).
     * <p>
     * Se a lista tiver apenas um No, a {@code head} e a {@code tail} serao simultaneamente
     * excluidas, deixando a lista vazia.
     * Caso a lista possua multiplos elementos, o No e removido (Garbage Collector) e o penultimo elemento
     * sera definido como a nova {@code tail}, os encadeamentos serao refeitos com a {@code head}
     * mantendo a lista ciclica.
     * </p>
     * @return o dado que estava armazenado no No removido.
     */
    public E removeLast(){
        if(isEmpty()) throw new IllegalArgumentException("Lista está vazia");
        E data = tail.getData();

        if(head == tail) {
            head = null;
            tail = null;
        } else {
            tail = tail.getPrevious();
            head.setPrevious(tail);
            tail.setNext(head);
        }

        size--;
        return data;
    }

    /**
     * Remove a primeira ocorrência do elemento especificado na lista.
     * <p>
     * O metodo realiza uma busca pelo dado e, caso encontrado, desvincula o nó
     * correspondente ajustando os ponteiros dos nós vizinhos. Se o elemento
     * estiver nas extremidades({@code head} ou {@code tail}), atribui a ação para os métodos apropriados.
     * </p>
     *
     * @param data o dado a ser removido da lista.
     */
    public void removeElement(E data) {
        if(isEmpty()) throw new IllegalArgumentException("Lista está vazia");
        NodeDouble<E> removedNode = searchElement(data);
        if(removedNode == null) return;

        if(removedNode == head){
            removeFirst();
            return;
        }

        if(removedNode == tail){
            removeLast();
            return;
        }
        NodeDouble<E> previousNode = removedNode.getPrevious();
        NodeDouble<E> nextNode = removedNode.getNext();
        previousNode.setNext(nextNode);
        nextNode.setPrevious(previousNode);
        size--;
    }

    /** Realiza uma busca bidirecional na lista.
     * <p>
     *     A varredura e realizada simultaneamente tanto pela {@code head}, quanto pela {@code tail} da lista
     *     com ajuda de Nos auxiliares para cada.
     *     Caso um dos dois encontre o No relacionado, ira retornar imediatamente,
     *     se a varredura continuar até que os dois ponteiros se encontrem,
     *     a busca e encerrada e retorna nulo, indicando que o elemento nao existe na lista.
     * </p>
     * @param data dado que vai servir como parametro de busca.
     * @return o No relacionado ao dado buscado, caso exista.
     */

    public NodeDouble<E> searchElement(E data){
        NodeDouble<E> atualH = head;
        NodeDouble<E> atualT = tail;
        if (isEmpty()) throw new IllegalArgumentException("Lista vazia");

        while(true){
            if(atualH.getData().equals(data)){
                return atualH;
            }
            if (atualT.getData().equals(data)) {
                return atualT;
            }
            if (atualH == atualT || atualH.getNext() == atualT) {
                return null;
            }
            atualH = atualH.getNext();
            atualT = atualT.getPrevious();
        }
    }

    /**
     * Retorna os dados armazenados no primeiro No da lista
     *
     * @return dados da cabeca atual da estrutura
     */
    public E getFirstElement(){
        if (isEmpty()) throw new IllegalArgumentException("Lista vazia");
        return head.getData();
    }

    /**
     * Retorna os dados armazenados no ultimo No da lista
     *
     * @return dados da cauda atual da estrutura
     */
    public E getLastElement(){
        if (isEmpty()) throw new IllegalArgumentException("Lista vazia");
        return tail.getData();
    }

    /**
     * Informa se a lista esta vazia.
     *
     * @return {@code true} quando nao houver elementos.
     */
    public boolean isEmpty(){
        return this.head == null;
    }

    /**
     * Retorna a quantidade de elementos armazenados.
     *
     * @return tamanho atual da lista.
     */
    public int getSize(){
        return size;
    }

    /**
     * Retorna o No inicial da lista.
     *
     * @return cabeca atual da estrutura.
     */
    public NodeDouble<E> getHead() {
        return head;
    }

    /**
     * Retorna o ultimo No da lista.
     *
     * @return cauda atual da estrutura.
     */
    public NodeDouble<E> getTail() {
        return tail;
    }


}
