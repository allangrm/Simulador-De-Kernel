package main;

/**
 * Representa os estados possíveis de um processo no ciclo de vida do simulador.
 * <p>
 * Este enum define as fases pelas quais um {@link Process} transita desde a sua
 * criação até a sua finalização, permitindo que o escalonador e o gerenciador
 * de I/O tomem decisões baseadas no estado atual do objeto.
 * </p>
 *
 * @author Lucas N. Araujo
 * @version 1.0
 * @since 2026-03-13
 */
public enum State {
    /**
     * O processo está carregado na memória e aguarda na fila de prioridade (Max-Heap)
     * para ser escalonado pela CPU.
     */
    READY,

    /**
     * O processo detém atualmente o controle da CPU e está executando suas
     * instruções dentro do quantum de tempo definido pelo sistema.
     */
    RUNNING,

    /**
     * O processo solicitou uma operação de entrada/saída e foi movido para a
     * fila de espera (QueueDoubleStack), aguardando a liberação do recurso.
     */
    BLOCKED,

    /**
     * O processo concluiu a execução de todas as suas instruções e está marcado
     * para remoção definitiva das estruturas de controle (Tabela Hash e Árvore AVL).
     */
    FINISHED
}
