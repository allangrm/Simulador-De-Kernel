package main.tads;

/**
 * Classe que representa um heap binário máximo.
 * @param <E> o tipo dos elementos do heap.
 * @author Raffael Wagner
 * @version 1.0
 * @since 2026-03-19
 */
public class MaxHeapBinary<E extends Comparable<E>> {

    private E[] heap;
    private int size;
    private int capacity;

    /**
     * Construtor da classe MaxHeapBinary.
     * @param capacity a capacidade inicial do heap.
     */
    public MaxHeapBinary(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("A capacidade inicial deve ser maior que zero.");
        }

        this.capacity = capacity;
        this.size = 0;
        this.heap = (E[]) new Comparable[capacity];
    }

    /**
     * Método que retorna o pai de um determinado índice.
     * @param index o índice do elemento.
     * @return o pai do elemento.
     */
    private int parent(int index) {
        return (index - 1) / 2;
    }

    /**
     * Método que retorna o filho esquerdo de um determinado índice.
     * @param index o índice do elemento.
     * @return o filho esquerdo do elemento.
     */
    private int leftChild(int index) {
        return 2 * index + 1;
    }

    /**
     * Método que retorna o filho direito de um determinado índice.
     * @param index o índice do elemento.
     * @return o filho direito do elemento.
     */
    private int rightChild(int index) {
        return 2 * index + 2;
    }

    /**
     * Método que insere um elemento no heap.
     * @param element o elemento a ser inserido.
     */
    public void insert(E element) {
        if (element == null) {
            throw new IllegalArgumentException("Elemento não pode ser nulo.");
        }

        if (size >= capacity) {
            resize();
        }

        heap[size] = element;
        size++;
        heapifyUp(size - 1);
    }

    /**
     * Método que extrai o elemento máximo do heap.
     * @return o elemento máximo do heap.
     */
    public E extractMax() {
        if (isEmpty()) {
            throw new RuntimeException("Heap está vazia.");
        }

        E max = heap[0];

        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;

        heapifyDown(0);

        return max;
    }

    /**
     * Método que retorna o elemento máximo do heap sem removê-lo.
     * @return o elemento máximo do heap.
     */
    public E peek() {
        if (isEmpty()) {
            throw new RuntimeException("Heap está vazia.");
        }
        return heap[0];
    }

    /**
     * Método que verifica se o heap está vazio.
     * @return true se o heap está vazio, false caso contrário.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Método que retorna o tamanho do heap.
     * @return o tamanho do heap.
     */
    public int size() {
        return size;
    }

    /**
     * Método que retorna o elemento de um determinado índice.
     * @param index o índice do elemento.
     * @return o elemento do índice.
     */
    public E getElementAt(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Indice está fora dos limites da heap.");
        }
        return heap[index];
    }

    /**
     * Método que rearranja o heap de cima para baixo.
     * @param index o índice do elemento.
     */
    private void heapifyUp(int index) {
        while (index > 0 && heap[index].compareTo(heap[parent(index)]) > 0) {
            swap(index, parent(index));
            index = parent(index);
        }
    }

    /**
     * Método que rearranja o heap de baixo para cima.
     * @param index o índice do elemento.
     */
    private void heapifyDown(int index) {
        int largest = index;

        int left = leftChild(index);
        int right = rightChild(index);

        if (left < size && heap[left].compareTo(heap[largest]) > 0) {
            largest = left;
        }

        if (right < size && heap[right].compareTo(heap[largest]) > 0) {
            largest = right;
        }

        if (largest != index) {
            swap(index, largest);
            heapifyDown(largest);
        }
    }

    /**
     * Método que troca dois elementos de lugar.
     * @param i o índice do primeiro elemento.
     * @param j o índice do segundo elemento.
     */
    private void swap(int i, int j) {
        E temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    /**
     * Método que aumenta a capacidade do heap.
     */
    private void resize() {
        capacity *= 2;
        E[] newHeap = (E[]) new Comparable[capacity];

        for (int i = 0; i < size; i++) {
            newHeap[i] = heap[i];
        }

        heap = newHeap;
    }

    /**
     * Método que limpa o heap.
     */
    public void clear() {
        for (int i = 0; i < size; i++) {
            heap[i] = null;
        }
        size = 0;
    }

}
