package main.tads;

public class HashTable<E> {
    private DoublyCircularLinkedList<E>[] table;
    private int capacity;
    private int size;

    public HashTable(int capacity){
        this.capacity = capacity;
        this.size = 0;

        this.table = (DoublyCircularLinkedList<E>[]) new DoublyCircularLinkedList[capacity];
        for(int i = 0; i < capacity; i++){
            this.table[i] = new DoublyCircularLinkedList<>();
        }
    }

    private int calculateIndex(E data){
        int num = data.hashCode();
        int k = Math.abs(num);
        int index = k % capacity;
        return index;
    }

    public void insert(E data){
        int index = calculateIndex(data);

        if(table[index].searchElement(data) !=null) return;

        table[index].insertFirst(data);

        size++;
    }

    public E search(E data){
        int index = calculateIndex(data);
        NodeDouble<E> nodeAux = table[index].searchElement(data);
        if(nodeAux !=null) {//caso houver
            return nodeAux.getData();
        }
        return null;
    }

    public void remove(E data){
        int index = calculateIndex(data);
        if (isEmpty()) throw new IllegalArgumentException("Lista vazia");

        if(table[index].searchElement(data) !=null){
            table[index].removeElement(data);
            size--;
        }
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getSize() {
        return size;
    }
}
