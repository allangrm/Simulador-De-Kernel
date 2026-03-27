package main;

import main.tads.bst.NodeTree;
import main.tads.bst.avl.AVL;
import main.tads.heap.MaxHeapBinary;
import main.tads.queue.Queue;
import main.tads.queue.QueueDoubleStack;

/**
 * Simula o núcleo de escalonamento Round-Robin com foco em estados de processo
 * e controle de tempo por quantum.
 *
 * @author Lucas, Allan, Raffael
 * @version 2.0
 * @since 2026-03-13
 */
public class Kernel {
	private static final int INITIAL_READY_CAPACITY = 10;

	private final int quantum;
	private int currentQuantum;
	private Process cpu;
	private final MaxHeapBinary<ReadyEntry> readyQueue;
	private final Queue<Process> ioBuffer;
	private final AVL<Process> processIndex;

	/**
	 * Encapsula um processo para ordenacao da heap por prioridade dinamica.
	 */
	private static final class ReadyEntry implements Comparable<ReadyEntry> {
		private final Process process;

		private ReadyEntry(Process process) {
			this.process = process;
		}

		private Process getProcess() {
			return process;
		}

		@Override
		public int compareTo(ReadyEntry other) {
			return this.process.compareByPriority(other.process);
		}
	}

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
		this.readyQueue = new MaxHeapBinary<>(INITIAL_READY_CAPACITY);
		this.ioBuffer = new QueueDoubleStack<>();
		this.processIndex = new AVL<>();
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
		processIndex.insert(new NodeTree<>(process));
		readyQueue.insert(new ReadyEntry(process));
	}

	/**
	 * Realiza o despacho de um processo pronto para a CPU quando ela está livre.
	 */
	public void dispatch() {
		if (cpu == null && !readyQueue.isEmpty()) {
			cpu = readyQueue.extractMax().getProcess();
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
		Process deferredReady = null;

		if (cpu == null) {
			incrementReadyQueueWait();
			incrementQueueWait(ioBuffer);
			return;
		}

		boolean movedToIo = cpu.executeTick();
		currentQuantum++;

		if (cpu.getState() == State.FINISHED) {
			processIndex.remove(new NodeTree<>(cpu));
			cpu = null;
		} else if (movedToIo || cpu.getState() == State.BLOCKED) {
			ioBuffer.enqueue(cpu);
			cpu = null;
		} else if (cpu.getState() == State.RUNNING && currentQuantum == quantum) {
			cpu.setState(State.READY);
			deferredReady = cpu;
			cpu = null;
		}

		incrementReadyQueueWait();
		incrementQueueWait(ioBuffer);

		if (deferredReady != null) {
			readyQueue.insert(new ReadyEntry(deferredReady));
		}
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
		readyQueue.insert(new ReadyEntry(process));
	}

	/**
	 * Incrementa o tempo de espera de todos os processos presentes na heap de prontos.
	 */
	private void incrementReadyQueueWait() {
		int size = readyQueue.size();
		ReadyEntry[] snapshot = new ReadyEntry[size];

		for (int i = 0; i < size; i++) {
			snapshot[i] = readyQueue.extractMax();
			snapshot[i].getProcess().incrementWait();
		}

		for (int i = 0; i < size; i++) {
			readyQueue.insert(snapshot[i]);
		}
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

	/**
	 * Retorna a quantidade de processos ativos indexados na AVL.
	 *
	 * @return total de processos atualmente ativos no indice por PID.
	 */
	public int getProcessIndexSize() {
		return processIndex.size();
	}

	/**
	 * Busca um processo ativo pelo PID no indice AVL.
	 *
	 * @param pid identificador do processo procurado.
	 * @return processo correspondente ou {@code null} quando nao existir no indice.
	 */
	public Process findProcessByPid(int pid) {
		if (pid <= 0) {
			throw new IllegalArgumentException("O PID deve ser maior que zero.");
		}

		NodeTree<Process> result = processIndex.search(new NodeTree<>(createPidProbe(pid)));
		if (result == null || result.isNil()) {
			return null;
		}
		return result.getData();
	}

	/**
	 * Cria um processo auxiliar apenas para comparacao por PID no indice AVL.
	 */
	private Process createPidProbe(int pid) {
		return new Process(pid, 0, 0, 1, 0);
	}
}
