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
 *
 * @author Lucas, Raffael, Allan Guilherme
 * @version 2.0
 * @since 2026-03-17
 */
public class KernelTest {
    private Kernel kernel;

    /**
     * Recria o kernel antes de cada cenário de teste.
     */
    @BeforeEach
    public void setUp() {
        kernel = new Kernel(2,11);
    }

    /**
     * Verifica se o construtor rejeita quantum inválido.
     */
    @Test
    public void testKernelConstructorRejectsInvalidQuantum() {
        IllegalArgumentException zeroQuantum = assertThrows(
                IllegalArgumentException.class,
                () -> new Kernel(0,11)
        );
        assertEquals("O quantum deve ser maior que zero.", zeroQuantum.getMessage());

        IllegalArgumentException negativeQuantum = assertThrows(
                IllegalArgumentException.class,
                () -> new Kernel(-2,11)
        );
        assertEquals("O quantum deve ser maior que zero.", negativeQuantum.getMessage());
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
     * Verifica se enqueueProcess rejeita referência nula.
     */
    @Test
    public void testEnqueueProcessRejectsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> kernel.enqueueProcess(null)
        );

        assertEquals("O processo não pode ser nulo.", exception.getMessage());
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
     * Verifica se o despacho prioriza o processo de maior prioridade.
     */
    @Test
    public void testDispatchChoosesHighestPriorityProcess() {
        Process lowPriorityProcess = createProcess(20, 1, 0, 3, 0);
        Process highPriorityProcess = createProcess(21, 5, 0, 3, 0);
        kernel.enqueueProcess(lowPriorityProcess);
        kernel.enqueueProcess(highPriorityProcess);

        kernel.dispatch();

        assertSame(highPriorityProcess, kernel.getCpu(), "O processo de maior prioridade deve ser escolhido primeiro.");
        assertEquals(State.RUNNING, highPriorityProcess.getState(), "O processo escolhido deve entrar em RUNNING.");
        assertEquals(1, kernel.getReadyQueueSize(), "O outro processo deve permanecer aguardando na heap de prontos.");
    }

    /**
     * Verifica se o aging altera a ordem de escalonamento ao longo dos ciclos.
     * <p>
     * Um processo inicialmente com prioridade menor deve, após aguardar ciclos
     * suficientes em READY, ultrapassar o concorrente e executar antes dele.
     * </p>
     */
    @Test
    public void testAgingAffectsSchedulingOrder() {
        Kernel agingKernel = new Kernel(1,11);

        Process dominantProcess = createProcess(100, 5, 0, 20, 0);
        Process waitingProcess = createProcess(1, 1, 0, 6, 0);

        agingKernel.enqueueProcess(dominantProcess);
        agingKernel.enqueueProcess(waitingProcess);

        for (int cycle = 0; cycle < 8 && waitingProcess.getExecutedCycles() == 0; cycle++) {
            agingKernel.executeCycle();
        }

        assertTrue(
                waitingProcess.getExecutedCycles() > 0,
                "Com aging dinâmico, o processo aguardando deve eventualmente assumir a CPU."
        );
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
     * Verifica se resolveIO em buffer vazio nao altera o estado global do kernel e suas estruturas.
     */
    @Test
    public void testResolveIoOnEmptyBufferDoesNothing() {
        Process process = createProcess(60, 3, 0);
        kernel.enqueueProcess(process);

        int readyBefore = kernel.getReadyQueueSize();
        int indexBefore = kernel.getProcessIndexSize();
        int tableBefore = kernel.getProcessTableSize();

        kernel.resolveIO();

        assertEquals(readyBefore, kernel.getReadyQueueSize(), "A fila READY nao deve ser alterada sem processos bloqueados.");
        assertEquals(0, kernel.getIoBufferSize(), "O buffer de I/O deve permanecer vazio.");
        assertEquals(indexBefore, kernel.getProcessIndexSize(), "O indice AVL nao deve sofrer alteracoes.");
        assertEquals(tableBefore, kernel.getProcessTableSize(), "A Tabela Hash nao deve sofrer alteracoes.");
        assertNull(kernel.getCpu(), "A CPU deve permanecer livre quando nao ha despacho no metodo resolveIO.");
    }

    /**
     * Verifica se dispatch nao troca o processo quando a CPU ja esta ocupada.
     */
    @Test
    public void testDispatchDoesNothingWhenCpuBusy() {
        Process first = createProcess(61, 5, 0, 4, 0);
        Process second = createProcess(62, 1, 0, 4, 0);
        kernel.enqueueProcess(first);
        kernel.enqueueProcess(second);

        kernel.dispatch();
        Process cpuBefore = kernel.getCpu();
        int readyBefore = kernel.getReadyQueueSize();

        kernel.dispatch();

        assertSame(cpuBefore, kernel.getCpu(), "O dispatch nao pode substituir o processo em CPU enquanto ela estiver ocupada.");
        assertEquals(readyBefore, kernel.getReadyQueueSize(), "A fila READY nao deve ser alterada por dispatch com CPU ocupada.");
    }

    /**
     * Verifica se processos fora da CPU acumulam tempo de espera corretamente.
     */
    @Test
    public void testExecuteCycleIncrementsWaitingTimeForReadyProcesses() {
        Process runningProcess = createProcess(9, 10, 0, 4, 0);
        Process waitingProcess = createProcess(1, 1, 0, 4, 0);
        kernel.enqueueProcess(runningProcess);
        kernel.enqueueProcess(waitingProcess);

        kernel.executeCycle();

        assertEquals(1, waitingProcess.getWaitingTime(), "Processos em READY devem acumular tempo de espera a cada ciclo alheio.");
        assertEquals(0, runningProcess.getWaitingTime(), "O processo em CPU não deve acumular tempo de espera enquanto executa.");
    }

    /**
     * Verifica se processos bloqueados em I/O acumulam tempo de espera em ciclos seguintes.
     */
    @Test
    public void testWaitingTimeAlsoIncrementsForBlockedProcessesInIoBuffer() {
        Process ioBound = createProcess(70, 5, 0, 3, 1);
        kernel.enqueueProcess(ioBound);

        kernel.executeCycle();
        assertEquals(State.BLOCKED, ioBound.getState(), "O processo deve bloquear no primeiro ciclo para entrar no buffer de I/O.");
        assertEquals(1, ioBound.getWaitingTime(), "O processo bloqueado deve acumular espera no proprio ciclo do bloqueio.");

        Process cpuBound = createProcess(71, 1, 0, 4, 0);
        kernel.enqueueProcess(cpuBound);

        kernel.executeCycle();
        assertEquals(State.BLOCKED, ioBound.getState(), "Sem resolveIO, o processo deve permanecer bloqueado.");
        assertEquals(2, ioBound.getWaitingTime(), "O processo no buffer de I/O deve continuar acumulando espera nos ciclos seguintes.");
    }

    /**
     * Verifica se o tamanho do indice AVL e da Tabela Hash acompanham o ciclo de vida dos processos.
     */
    @Test
    public void testProcessStructuresSizeReflectsLifecycle() {
        Process p1 = createProcess(80, 1, 0);
        Process p2 = createProcess(81, 1, 0);
        Process p3 = createProcess(82, 1, 0);

        kernel.enqueueProcess(p1);
        kernel.enqueueProcess(p2);
        kernel.enqueueProcess(p3);

        assertEquals(3, kernel.getProcessIndexSize(), "A AVL deve conter todos os processos ativos.");
        assertEquals(3, kernel.getProcessTableSize(), "A Tabela Hash (PCB) deve registrar todos os processos ativos.");

        kernel.executeCycle();
        assertEquals(2, kernel.getProcessIndexSize(), "A AVL deve reduzir para 2 apos a primeira finalizacao.");
        assertEquals(2, kernel.getProcessTableSize(), "A Tabela Hash deve reduzir para 2 apos a primeira finalizacao.");

        kernel.executeCycle();
        assertEquals(1, kernel.getProcessIndexSize(), "A AVL deve reduzir para 1.");
        assertEquals(1, kernel.getProcessTableSize(), "A Tabela Hash deve reduzir para 1.");

        kernel.executeCycle();
        assertEquals(0, kernel.getProcessIndexSize(), "A AVL deve ficar vazia apos a finalizacao total.");
        assertEquals(0, kernel.getProcessTableSize(), "A Tabela Hash deve ficar vazia apos a finalizacao total.");
    }

    /**
     * Verifica consulta por PID na Tabela Hash durante vida ativa e após finalização.
     */
    @Test
    public void testFindProcessByPidFindsActiveProcessAndReturnsNullAfterFinish() {
        Process process = createProcess(30, 1, 0, 1, 0);

        kernel.enqueueProcess(process);
        assertEquals(1, kernel.getProcessIndexSize(), "A AVL deve indexar o processo ativo após enqueue.");
        assertEquals(1, kernel.getProcessTableSize(), "A Tabela Hash deve registrar o processo ativo após enqueue.");
        assertSame(process, kernel.findProcessByPid(30), "A busca por PID (O(1)) deve retornar o processo ativo da Hash.");

        kernel.executeCycle();

        assertEquals(State.FINISHED, process.getState(), "O processo deve finalizar no primeiro ciclo deste cenário.");
        assertNull(kernel.findProcessByPid(30), "Após finalizar, o processo deve sair do PCB.");
        assertEquals(0, kernel.getProcessIndexSize(), "O tamanho da AVL deve decrementar após remoção.");
        assertEquals(0, kernel.getProcessTableSize(), "O tamanho da Tabela Hash deve decrementar após remoção.");
    }

    /**
     * Verifica se findProcessByPid rejeita PID inválido.
     */
    @Test
    public void testFindProcessByPidRejectsInvalidPid() {
        IllegalArgumentException zeroPid = assertThrows(
                IllegalArgumentException.class,
                () -> kernel.findProcessByPid(0)
        );
        assertEquals("O PID deve ser maior que zero.", zeroPid.getMessage());

        IllegalArgumentException negativePid = assertThrows(
                IllegalArgumentException.class,
                () -> kernel.findProcessByPid(-10)
        );
        assertEquals("O PID deve ser maior que zero.", negativePid.getMessage());
    }
    /**
     * Verifica se o kernel rejeita a entrada de processos com PIDs duplicados,
     * garantindo a integridade da Tabela Hash e da AVL.
     */
    @Test
    public void testEnqueueProcessRejectsDuplicatePid() {
        Process process1 = createProcess(100, 4, 0);
        Process process2 = createProcess(100, 2, 0);

        kernel.enqueueProcess(process1);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> kernel.enqueueProcess(process2)
        );

        assertEquals("Conflito no Kernel: O PID 100 já está em uso por outro processo ativo.", exception.getMessage());
        assertEquals(1, kernel.getProcessTableSize(), "A Hash não deve inserir o processo duplicado.");
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
        return createProcess(pid, 1, 0, totalBurstTime, ioThreshold);
    }

    /**
     * Cria um processo configurando explicitamente a prioridade para cenarios de escalonamento.
     */
    private Process createProcess(int pid, int priority, int arrivalTime, int totalBurstTime, int ioThreshold) {
        return new Process(pid, priority, arrivalTime, totalBurstTime, ioThreshold);
    }
}
