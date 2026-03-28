package main;

import java.util.Objects;


/**
 * Representa um bloco de controle de processo (PCB) completo dentro do simulador de kernel.
 * <p>
 * Esta classe concentra metadados imutáveis do processo, métricas de execução
 * e transições de estado necessárias para o escalonamento Round-Robin com
 * suporte a bloqueio por I/O.
 * </p>
 *
 * @author Lucas Nóbrega
 * @version 1.0
 * @since 2026-03-13
 */
public class Process implements Comparable<Process> {
    private final int pid;
    private final int basePriority;
    private final int arrivalTime;
    private final int totalBurstTime;
    private final int ioThreshold;

    private int priority;
    private int remainingTime;
    private int waitingTime;
    private int executedCycles;
    private State state;

    /**
     * Constrói um novo PCB com os atributos imutáveis definidos na criação.
     *
     * @param pid identificador único do processo.
     * @param priority nível de prioridade usado no escalonador.
     * @param arrivalTime ciclo de chegada do processo no sistema.
     * @param totalBurstTime total de ciclos de CPU necessários para finalizar.
     * @param ioThreshold limiar de ciclos para disparar bloqueio de I/O.
     */
    public Process(int pid, int priority, int arrivalTime, int totalBurstTime, int ioThreshold) {
        if (pid <= 0) {
            throw new IllegalArgumentException("O PID deve ser maior que zero.");
        }
        if (totalBurstTime <= 0) {
            throw new IllegalArgumentException("O tempo total de CPU deve ser maior que zero.");
        }
        if (priority < 0) {
            throw new IllegalArgumentException("A prioridade inicial nao pode ser negativa.");
        }
        if (arrivalTime < 0) {
            throw new IllegalArgumentException("O tempo de chegada não pode ser negativo.");
        }
        if (ioThreshold < 0) {
            throw new IllegalArgumentException("O limiar de I/O não pode ser negativo.");
        }

        this.pid = pid;
        this.basePriority = priority;
        this.priority = priority;
        this.arrivalTime = arrivalTime;
        this.totalBurstTime = totalBurstTime;
        this.ioThreshold = ioThreshold;

        this.remainingTime = totalBurstTime;
        this.waitingTime = 0;
        this.executedCycles = 0;
        this.state = State.READY;
    }

    /**
     * Executa exatamente um ciclo de CPU para o processo atual.
     *
     * @return {@code true} quando ocorre bloqueio de I/O ao final do ciclo;
     * {@code false} nos demais casos.
     */
    public boolean executeTick() {
        if (state != State.RUNNING) {
            throw new IllegalStateException("O processo precisa estar em RUNNING para executar um ciclo.");
        }

        remainingTime--;
        executedCycles++;

        if (remainingTime <= 0) {
            remainingTime = 0;
            state = State.FINISHED;
            return false;
        }

        if (ioThreshold > 0 && executedCycles % ioThreshold == 0) {
            state = State.BLOCKED;
            return true;
        }

        return false;
    }

    /**
     * Incrementa o tempo de espera quando o processo está apto ou bloqueado.
     * <p>
     * Para simular prioridade dinâmica, processos em READY sofrem aging e
     * recebem incremento de prioridade a cada ciclo de espera.
     * </p>
     */
    public void incrementWait() {
        if (state == State.READY || state == State.BLOCKED) {
            waitingTime++;
            if (state == State.READY) {
                priority++;
            }
        }
    }

    /**
     * Retorna o identificador do processo.
     *
     * @return identificador unico do processo.
     */
    public int getPid() {
        return pid;
    }

    /**
     * Retorna a prioridade associada ao processo.
     *
     * @return prioridade usada pelo escalonador.
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Retorna a prioridade base definida na criação do processo.
     *
     * @return prioridade inicial (sem aging).
     */
    public int getBasePriority() {
        return basePriority;
    }

    /**
     * Atualiza manualmente a prioridade dinâmica atual.
     *
     * @param priority nova prioridade dinâmica.
     */
    public void setPriority(int priority) {
        if (priority < 0) {
            throw new IllegalArgumentException("A prioridade não pode ser negativa.");
        }
        this.priority = priority;
    }

    /**
     * Restaura a prioridade dinâmica para a prioridade base.
     */
    public void resetPriority() {
        this.priority = this.basePriority;
    }

    /**
     * Retorna o ciclo de chegada do processo.
     *
     * @return instante lógico de chegada.
     */
    public int getArrivalTime() {
        return arrivalTime;
    }

    /**
     * Retorna o tempo total de CPU exigido pelo processo.
     *
     * @return total de ciclos de CPU necessários.
     */
    public int getTotalBurstTime() {
        return totalBurstTime;
    }

    /**
     * Retorna o limiar configurado para bloqueio de I/O.
     *
     * @return quantidade de ciclos usada como limiar.
     */
    public int getIoThreshold() {
        return ioThreshold;
    }

    /**
     * Retorna o tempo restante de CPU.
     *
     * @return quantidade de ciclos ainda necessários.
     */
    public int getRemainingTime() {
        return remainingTime;
    }

    /**
     * Atualiza o tempo restante de CPU.
     *
     * @param remainingTime novo tempo restante.
     */
    public void setRemainingTime(int remainingTime) {
        if (remainingTime < 0) {
            throw new IllegalArgumentException("O tempo restante não pode ser negativo.");
        }
        this.remainingTime = remainingTime;
    }

    /**
     * Retorna o tempo acumulado em espera.
     *
     * @return total de ciclos aguardando recurso ou CPU.
     */
    public int getWaitingTime() {
        return waitingTime;
    }

    /**
     * Atualiza o tempo acumulado em espera.
     *
     * @param waitingTime novo tempo de espera.
     */
    public void setWaitingTime(int waitingTime) {
        if (waitingTime < 0) {
            throw new IllegalArgumentException("O tempo de espera não pode ser negativo.");
        }
        this.waitingTime = waitingTime;
    }

    /**
     * Retorna a quantidade total de ciclos já executados.
     *
     * @return total de ciclos consumidos na CPU.
     */
    public int getExecutedCycles() {
        return executedCycles;
    }

    /**
     * Atualiza a quantidade de ciclos executados.
     *
     * @param executedCycles novo total de ciclos executados.
     */
    public void setExecutedCycles(int executedCycles) {
        if (executedCycles < 0) {
            throw new IllegalArgumentException("Os ciclos executados não podem ser negativos.");
        }
        this.executedCycles = executedCycles;
    }

    /**
     * Retorna o estado atual do processo.
     *
     * @return estado atual na máquina de estados.
     */
    public State getState() {
        return state;
    }

    /**
     * Atualiza o estado atual do processo.
     *
     * @param state novo estado do processo.
     */
    public void setState(State state) {
        if (state == null) {
            throw new IllegalArgumentException("O estado do processo não pode ser nulo.");
        }
        this.state = state;
    }

    /**
     * Define a ordenação natural por PID (índice da AVL).
     */
    @Override
    public int compareTo(Process other) {
        if (other == null) {
            throw new IllegalArgumentException("Não é possível comparar com um processo nulo.");
        }

        return Integer.compare(this.pid, other.pid);
    }

    /**
     * Compara processos por prioridade dinâmica para uso no escalonador.
     * Em empate de prioridade, usa PID para garantir resultado determinístico.
     *
     * @param other processo comparado.
     * @return valor positivo quando este processo deve ter precedência.
     */
    public int compareByPriority(Process other) {
        if (other == null) {
            throw new IllegalArgumentException("Não é possível comparar com um processo nulo.");
        }

        int priorityOrder = Integer.compare(this.priority, other.priority);
        if (priorityOrder != 0) {
            return priorityOrder;
        }

        return Integer.compare(other.pid, this.pid);
    }

    /**
     * Retorna uma representacao textual resumida do processo.
     *
     * @return descricao textual do estado atual do processo.
     */
    @Override
    public String toString() {
        return String.format(
                "[PID: %d | Prioridade: %d | Chegada: %d | Restante: %d | Espera: %d | Executado: %d | Estado: %s]",
                pid, priority, arrivalTime, remainingTime, waitingTime, executedCycles, state
        );
    }

    /**
     * Define igualdade semântica baseada exclusivamente no PID.
     *
     * @param obj objeto comparado.
     * @return {@code true} quando ambos representam o mesmo PID.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Process other)) {
            return false;
        }
        return this.pid == other.pid;
    }

    /**
     * Retorna hash consistente com a igualdade por PID.
     *
     * @return hash baseado no PID.
     */
    @Override
    public int hashCode() {
        return Objects.hash(pid);
    }
}
