package main.tads.queue;

/**
 * Representa a interface da estrutura de dados: Fila (Queue).
 * <p>
 * Esta interface define as operacoes padronizadas para o comportamento
 * de uma fila baseada no principio FIFO (First-In-First-Out), garantindo
 * que o primeiro elemento inserido seja o primeiro a ser removido.
 * </p>
 *
 * @param <E> o tipo de elemento que a fila irá armazenar.
 * @author Lucas Nóbrega
 * @version 1.0
 * @since 2026-03-12
 * @version 1.0
 */
public interface Queue<E> {
    void enqueue(E element);
    E dequeue();
    E peek();
    boolean isEmpty();
    int size();
}
