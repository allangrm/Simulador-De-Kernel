package main.tads.stack;


import main.tads.linked_list.NodeList;

/**
 * Representa a implementação da estrutura: Pilha utilizando Lista Encadeada.
 * <p>
 * Esta classe implementa a interface {@link Stack} e atende ao requisito
 * arquitetural de utilizar uma instância de Lista Encadeada para simular
 * o comportamento de uma Pilha.
 * </p>
 *
 * @param <E> o tipo de elemento que a pilha irá armazenar.
 * @see Stack
 * @see NodeList
 * @author Raffael Wagner
 * @version 1.0
 * @since 2026-03-12
 */
public class LinkedStack<E> implements Stack<E> {

    private NodeList<E> top;
    private int size;

    public LinkedStack() {
        this.top = null;
        this.size = 0;
    }

    @Override
    public void push(E element) {
        top = new NodeList<>(element, top);
        size++;
    }

    @Override
    public E pop() {
        if (isEmpty()) {
            throw new RuntimeException("Erro: A pilha está vazia.");
        }

        E data = top.getData();
        top = top.getNext();
        size--;

        return data;
    }

    @Override
    public E peek() {
        if (isEmpty()) {
            throw new RuntimeException("Erro: A pilha está vazia.");
        }

        return top.getData();
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        top = null;
        size = 0;
    }
}
