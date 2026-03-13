package main;


/**
 * Representa a abstração de um processo dentro do simulador de kernel.
 * <p>
 * Esta classe armazena os metadados necessários para o escalonamento,
 * incluindo identificador único (PID), prioridade e o estado atual
 * no ciclo de vida do sistema operacional.
 * </p>
 * @author Lucas N. Araujo
 * @version 1.0
 * @since 2026-03-13
 */
public class Process implements Comparable<Process> {
    private int pid;
    private String name;
    private int priority;
    private int instructionsRemaining;
    private State state;

    /**
     * Construtor para a criação de um novo processo.
     * * @param pid identificador único do processo.
     * @param name nome legível do processo (ex: "Terminal").
     * @param priority nível de prioridade para o escalonador Max-Heap.
     * @param instructionsTotal quantidade total de ciclos de instrução necessários.
     */
    public Process(int pid, String name, int priority, int instructionsTotal) {
        this.pid = pid;
        this.name = name;
        this.priority = priority;
        this.instructionsRemaining = instructionsTotal;
        this.state = State.READY; // Todo processo inicia no estado PRONTO
    }

    /**
     * Simula a execução do processo pela CPU durante um determinado quantum.
     * * @param quantum quantidade de instruções que a CPU processará neste turno.
     */
    public void execute(int quantum) {
        this.state = State.RUNNING;
        this.instructionsRemaining -= quantum;

        if (this.instructionsRemaining <= 0) {
            this.instructionsRemaining = 0;
            this.state = State.FINISHED;
        } else {
            this.state = State.READY;
        }
    }

    // Getters e Setters em aqui...

    /**
     * Define a ordenação natural dos processos.
     * <p>
     * <b>Nota:</b> Para a Árvore AVL, a comparação é feita via PID.
     * Para a Max-Heap, a lógica de comparação deverá ser baseada na prioridade.
     * </p>
     */
    @Override
    public int compareTo(Process other) {
        return Integer.compare(this.pid, other.pid);
    }

    @Override
    public String toString() {
        return String.format("[PID: %d | Nome: %s | Prioridade: %d | Instruções: %d | Estado: %s]",
                pid, name, priority, instructionsRemaining, state);
    }
}
