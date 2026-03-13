package main.tads;

/**
 * Representa a interface da estrutura de dados: Fila (Queue).
 * <p>
 * Esta interface define as operações padronizadas para o comportamento
 * de uma fila baseada no princípio FIFO (First-In-First-Out), garantindo
 * que o primeiro elemento inserido seja o primeiro a ser removido.
 * </p>
 *
 * @param <E> o tipo de elemento que a fila irá armazenar.
 * @author Lucas N. Araujo
 * @version 1.0
 * @since 2026-03-12
 */
public interface Queue<E> {
    void enqueue(E elemento);
    E dequeue();
    E peek();
    boolean isEmpty();
    int size();
}
