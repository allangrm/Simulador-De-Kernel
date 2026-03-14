package test;

import main.Kernel;
import main.Process;
import main.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Representa a suíte de testes unitários para a simulação de estados
 * e temporização Round-Robin da classe {@link Kernel}.
 * <p>
 * Esta classe valida o comportamento observável do kernel durante o despacho,
 * a execução por ciclo, a preempção por quantum, o bloqueio por I/O
 * e o retorno de processos para a fila de prontos.
 * </p>
 */
public class KernelTest {
    private Kernel kernel;

    /**
     * Recria o kernel antes de cada cenário de teste.
     */
    @BeforeEach
    public void setUp() {
        kernel = new Kernel(2);
    }

    /**
     * Verifica se um processo enfileirado entra corretamente em READY.
     */
    @Test
    public void testEnqueueProcessAddsReadyProcessToReadyQueue() {
        Process process = createProcess(1, 4, 0);
        process.setState(State.RUNNING);

        kernel.enqueueProcess(process);

        assertEquals(State.READY, process.getState(), "O processo deve ser normalizado para READY ao entrar no sistema.");
        assertEquals(1, kernel.getReadyQueueSize(), "A fila de prontos deve conter o processo enfileirado.");
        assertNull(kernel.getCpu(), "A CPU deve permanecer livre após apenas enfileirar um processo.");
    }

    /**
     * Verifica se o despacho move o primeiro processo pronto para a CPU.
     */
    @Test
    public void testDispatchMovesProcessFromReadyQueueToCpu() {
        Process process = createProcess(2, 5, 0);
        kernel.enqueueProcess(process);

        kernel.dispatch();

        assertSame(process, kernel.getCpu(), "O processo despachado deve ocupar a CPU.");
        assertEquals(State.RUNNING, process.getState(), "O processo despachado deve entrar em RUNNING.");
        assertEquals(0, kernel.getReadyQueueSize(), "A fila de prontos deve perder o processo despachado.");
        assertEquals(0, kernel.getCurrentQuantum(), "O contador do quantum deve ser reiniciado no despacho.");
    }

    /**
     * Verifica se um processo e finalizado e removido da CPU no ultimo ciclo.
     */
    @Test
    public void testExecuteCycleFinishesProcessAndReleasesCpu() {
        Process process = createProcess(3, 1, 0);
        kernel.enqueueProcess(process);

        kernel.executeCycle();

        assertNull(kernel.getCpu(), "A CPU deve ser liberada quando o processo finalizar.");
        assertEquals(State.FINISHED, process.getState(), "O processo deve terminar quando o tempo restante chegar a zero.");
        assertEquals(0, process.getRemainingTime(), "O tempo restante deve ser zerado na finalizacao.");
        assertEquals(0, kernel.getReadyQueueSize(), "Não deve restar processo pronto após a finalização.");
        assertEquals(0, kernel.getIoBufferSize(), "A fila de I/O deve permanecer vazia quando não há bloqueio.");
    }

    /**
     * Verifica se o bloqueio por I/O move o processo para o buffer apropriado.
     */
    @Test
    public void testExecuteCycleBlocksProcessAndMovesItToIoBuffer() {
        Process process = createProcess(4, 3, 1);
        kernel.enqueueProcess(process);

        kernel.executeCycle();

        assertNull(kernel.getCpu(), "A CPU deve ser liberada quando o processo bloqueia por I/O.");
        assertEquals(State.BLOCKED, process.getState(), "O processo deve entrar em BLOCKED ao atingir o limiar de I/O.");
        assertEquals(0, kernel.getReadyQueueSize(), "A fila de prontos deve ficar vazia após o bloqueio do único processo.");
        assertEquals(1, kernel.getIoBufferSize(), "O buffer de I/O deve receber o processo bloqueado.");
    }

    /**
     * Verifica se a preempção por quantum devolve o processo para READY.
     */
    @Test
    public void testExecuteCyclePreemptsProcessWhenQuantumExpires() {
        Process process = createProcess(5, 5, 0);
        kernel.enqueueProcess(process);

        kernel.executeCycle();
        assertSame(process, kernel.getCpu(), "O processo deve continuar na CPU antes do fim do quantum.");
        assertEquals(State.RUNNING, process.getState(), "O processo deve continuar em RUNNING antes da preempção.");
        assertEquals(1, kernel.getCurrentQuantum(), "O contador deve refletir o primeiro ciclo consumido.");

        kernel.executeCycle();

        assertNull(kernel.getCpu(), "A CPU deve ser liberada quando o quantum expirar.");
        assertEquals(State.READY, process.getState(), "O processo deve voltar para READY após a preempção.");
        assertEquals(1, kernel.getReadyQueueSize(), "O processo preemptado deve retornar para a fila de prontos.");
        assertEquals(2, kernel.getCurrentQuantum(), "O contador deve registrar o total de ciclos consumidos no quantum.");
    }

    /**
     * Verifica se a resolução de I/O devolve um processo bloqueado para READY.
     */
    @Test
    public void testResolveIoReturnsBlockedProcessToReadyQueue() {
        Process process = createProcess(6, 4, 1);
        kernel.enqueueProcess(process);
        kernel.executeCycle();

        kernel.resolveIO();

        assertEquals(State.READY, process.getState(), "O processo desbloqueado deve voltar ao estado READY.");
        assertEquals(0, kernel.getIoBufferSize(), "O buffer de I/O deve perder o processo resolvido.");
        assertEquals(1, kernel.getReadyQueueSize(), "O processo resolvido deve retornar para a fila de prontos.");
    }

    /**
     * Verifica se processos fora da CPU acumulam tempo de espera corretamente.
     */
    @Test
    public void testExecuteCycleIncrementsWaitingTimeForReadyProcesses() {
        Process runningProcess = createProcess(7, 4, 0);
        Process waitingProcess = createProcess(8, 4, 0);
        kernel.enqueueProcess(runningProcess);
        kernel.enqueueProcess(waitingProcess);

        kernel.executeCycle();

        assertEquals(1, waitingProcess.getWaitingTime(), "Processos em READY devem acumular tempo de espera a cada ciclo alheio.");
        assertEquals(0, runningProcess.getWaitingTime(), "O processo em CPU não deve acumular tempo de espera enquanto executa.");
    }

    /**
     * Cria um processo de apoio para cenários de teste previsíveis.
     *
     * @param pid identificador do processo.
     * @param totalBurstTime total de ciclos de CPU exigidos.
     * @param ioThreshold limiar para bloqueio de I/O.
     * @return processo configurado para o cenário de teste.
     */
    private Process createProcess(int pid, int totalBurstTime, int ioThreshold) {
        return new Process(pid, 1, 0, totalBurstTime, ioThreshold);
    }
}

