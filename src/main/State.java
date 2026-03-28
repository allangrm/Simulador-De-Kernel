package main;

/**
 * Representa os estados possíveis de um processo no ciclo de vida do PCB.
 * <p>
 * Este enum define as fases pelas quais um {@link Process} transita durante
 * o escalonamento Round-Robin e o controle de I/O do simulador.
 * </p>
 *
 * @author Lucas N. Araujo
 * @version 1.0
 * @since 2026-03-13
 */
public enum State {
    /**
     * O processo está pronto para disputar CPU no escalonador.
     */
    READY,

    /**
     * O processo detém a CPU e está executando ciclos de instrução.
     */
    RUNNING,

    /**
     * O processo solicitou I/O e aguarda a liberação do recurso externo.
     */
    BLOCKED,

    /**
     * O processo concluiu todos os ciclos de CPU e não voltará ao escalonador.
     */
    FINISHED
}
