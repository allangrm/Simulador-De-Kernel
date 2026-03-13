package main.tads;

/**
 * Representa a implementação da estrutura: Fila utilizando Duas Pilhas.
 * <p>
 * Esta classe implementa a interface {@link Queue} e atende ao requisito
 * arquitetural de utilizar exclusivamente duas instâncias de Pilha para simular
 * o comportamento de enfileirar e desenfileirar elementos de forma FIFO.
 * </p>
 *
 * @param <E> o tipo de elemento que a fila irá armazenar.
 * @see Queue
 * @see LinkedStack
 * @author Lucas N. Araujo
 * @version 1.0
 * @since 2026-03-13
 */
public class QueueDoubleStack<E> implements Queue<E> {

    private Stack<E> stackIn;
    private Stack<E> stackOut;

    public QueueDoubleStack() {
        this.stackIn = new LinkedStack<>();
        this.stackOut = new LinkedStack<>();
    }

    @Override
    public void enqueue(E element) { // O(1)
        stackIn.push(element);
    }

    @Override
    public E dequeue() { // O(1) amortizado? Perguntar a JJ
        transferElements();

        if (stackOut.isEmpty()) {
            throw new RuntimeException("Erro: A fila está vazia.");
        }

        return stackOut.pop();
    }

    @Override
    public E peek() {
        transferElements();

        if (stackOut.isEmpty()) {
            throw new RuntimeException("Erro: A fila está vazia.");
        }

        return stackOut.peek();
    }

    @Override
    public boolean isEmpty() {
        return stackIn.isEmpty() && stackOut.isEmpty();
    }

    @Override
    public int size() {
        return stackIn.size() + stackOut.size();
    }

    /**
     * Realiza a transferência estrutural dos elementos da pilha de entrada para a pilha de saída.
     * <p>
     * Este método é o núcleo lógico que garante o comportamento FIFO da estrutura a partir
     * de duas pilhas (LIFO). Ele desempilha sistematicamente todos os elementos da
     * {@code stackIn} e os empilha na {@code stackOut}, invertendo a ordem cronológica.
     * </p>
     * <p>
     * <b>Análise de Complexidade:</b> A transferência ocorre de forma reativa e estritamente
     * quando a {@code stackOut} encontra-se vazia. Embora esta operação específica
     * possua custo de pior caso O(n), essa abordagem garante que as chamadas subsequentes
     * de remoção possuam um custo amortizado O(1).
     * </p>
     */
    private void transferElements() {
        if (stackOut.isEmpty()) {
            while (!stackIn.isEmpty()) {
                stackOut.push(stackIn.pop());
            }
        }
    }
}