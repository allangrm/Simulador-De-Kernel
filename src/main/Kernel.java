package main;

import main.tads.queue.Queue;
import main.tads.queue.QueueDoubleStack;

/**
 * Simula o núcleo de escalonamento Round-Robin com foco em estados de processo
 * e controle de tempo por quantum.
 *
 * @author Lucas N. Araujo
 * @version 1.0
 * since 2026-03-13
 */
public class Kernel {
	private final int quantum;
	private int currentQuantum;
	private Process cpu;
	private final Queue<Process> readyQueue;
	private final Queue<Process> ioBuffer;

	/**
	 * Cria um kernel com quantum fixo para preempção por tempo.
	 *
	 * @param quantum limite máximo de ciclos contínuos na CPU.
	 */
	public Kernel(int quantum) {
		if (quantum <= 0) {
			throw new IllegalArgumentException("O quantum deve ser maior que zero.");
		}

		this.quantum = quantum;
		this.currentQuantum = 0;
		this.cpu = null;
		this.readyQueue = new QueueDoubleStack<>();
		this.ioBuffer = new QueueDoubleStack<>();
	}

	/**
	 * Enfileira um processo na fila de prontos e normaliza seu estado.
	 *
	 * @param process processo a ser inserido no sistema.
	 */
	public void enqueueProcess(Process process) {
		if (process == null) {
			throw new IllegalArgumentException("O processo não pode ser nulo.");
		}

		process.setState(State.READY);
		readyQueue.enqueue(process);
	}

	/**
	 * Realiza o despacho de um processo pronto para a CPU quando ela está livre.
	 */
	public void dispatch() {
		if (cpu == null && !readyQueue.isEmpty()) {
			cpu = readyQueue.dequeue();
			currentQuantum = 0;
			cpu.setState(State.RUNNING);
		}
	}

	/**
	 * Executa um ciclo lógico completo do escalonador.
	 * <p>
	 * Ordem do ciclo:
	 * despacha se a CPU estiver livre, executa um tick do processo atual,
	 * e aplica as regras de finalização, bloqueio de I/O ou preempção por quantum.
	 * </p>
	 */
	public void executeCycle() {
		dispatch();

		if (cpu == null) {
			incrementQueueWait(readyQueue);
			incrementQueueWait(ioBuffer);
			return;
		}

		boolean movedToIo = cpu.executeTick();
		currentQuantum++;

		if (cpu.getState() == State.FINISHED) {
			cpu = null;
		} else if (movedToIo || cpu.getState() == State.BLOCKED) {
			ioBuffer.enqueue(cpu);
			cpu = null;
		} else if (cpu.getState() == State.RUNNING && currentQuantum == quantum) {
			cpu.setState(State.READY);
			readyQueue.enqueue(cpu);
			cpu = null;
		}

		incrementQueueWait(readyQueue);
		incrementQueueWait(ioBuffer);
	}

	/**
	 * Resolve uma solicitação de I/O movendo um processo bloqueado para pronto.
	 */
	public void resolveIO() {
		if (ioBuffer.isEmpty()) {
			return;
		}

		Process process = ioBuffer.dequeue();
		process.setState(State.READY);
		readyQueue.enqueue(process);
	}

	/**
	 * Incrementa o tempo de espera de todos os processos presentes na fila.
	 */
	private void incrementQueueWait(Queue<Process> queue) {
		int size = queue.size();
		for (int i = 0; i < size; i++) {
			Process process = queue.dequeue();
			process.incrementWait();
			queue.enqueue(process);
		}
	}

	/**
	 * Retorna o quantum configurado para o escalonador.
	 *
	 * @return limite de ciclos contínuos por despacho.
	 */
	public int getQuantum() {
		return quantum;
	}

	/**
	 * Retorna o quantum já consumido pelo processo atual.
	 *
	 * @return quantidade de ciclos já executados no despacho corrente.
	 */
	public int getCurrentQuantum() {
		return currentQuantum;
	}

	/**
	 * Retorna o processo atualmente alocado na CPU.
	 *
	 * @return processo em execução ou {@code null} quando a CPU estiver livre.
	 */
	public Process getCpu() {
		return cpu;
	}

	/**
	 * Retorna a quantidade de processos prontos aguardando CPU.
	 *
	 * @return total de processos na fila de prontos.
	 */
	public int getReadyQueueSize() {
		return readyQueue.size();
	}

	/**
	 * Retorna a quantidade de processos aguardando resolução de I/O.
	 *
	 * @return total de processos bloqueados no buffer de I/O.
	 */
	public int getIoBufferSize() {
		return ioBuffer.size();
	}
}
