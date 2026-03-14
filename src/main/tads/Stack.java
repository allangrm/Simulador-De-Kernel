package main.tads;

/**
 * Representa a interface da estrutura de dados: Pilha (Stack).
 * <p>
 * Esta interface define as operações padronizadas para o comportamento
 * de uma pilha baseada no princípio LIFO (Last-In-First-Out), garantindo
 * que o último elemento inserido seja o primeiro a ser removido.
 * </p>
 *
 * @param <E> o tipo de elemento que a pilha irá armazenar.
 * @author Raffael Wagner
 * @version 1.0
 * @since 2026-03-11
 */
public interface Stack<E> {
    void push(E element);
    E pop();
    E peek();
    boolean isEmpty();
    int size();
    void clear();
}
