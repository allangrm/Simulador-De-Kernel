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
 * @author Lucas N. Araujo
 * @version 1.0
 * @since 2026-03-12
 * @version 1.0
 */
public interface Queue<E> {
    /**
     * Insere um elemento no final da fila.
     *
     * @param element elemento a ser inserido.
     */
    void enqueue(E element);

    /**
     * Remove e retorna o elemento na frente da fila.
     *
     * @return elemento removido da fila.
     */
    E dequeue();

    /**
     * Consulta o elemento na frente da fila sem remove-lo.
     *
     * @return elemento atualmente na frente da fila.
     */
    E peek();

    /**
     * Informa se a fila esta vazia.
     *
     * @return {@code true} quando nao houver elementos armazenados.
     */
    boolean isEmpty();

    /**
     * Retorna a quantidade de elementos presentes na fila.
     *
     * @return total de elementos armazenados.
     */
    int size();
}
