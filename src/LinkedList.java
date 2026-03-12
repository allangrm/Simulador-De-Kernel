/**
 * Representa a implementação da estrutura: Lista Simplesmente Encadeada.
 * <p>
 * Esta classe utiliza de objetos do tipo {@link NodeList}
 * para montar a Lista, gerando uma sequência de elementos encadeados.
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

    public LinkedList() {
        this.head = null;
        this.size = 0;
    }

    public void insertFirst(T data){
        head = new NodeList<>(data, head);
        size++;
    }

    public T removeFirst(){
        if(isEmpty()) throw new IllegalArgumentException("Lista está vazia");

        T data = head.getData();
        head = head.getNext();
        size--;
        return data;
    }

    public T getFirstElement(){
        return head.getData();
    }

    public boolean isEmpty(){
        return this.head == null;
    }

    public int getSize(){
        return size;
    }

    public NodeList<T> getHead() {
        return head;
    }

}