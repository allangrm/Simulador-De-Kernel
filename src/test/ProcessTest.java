package test;

import main.Process;
import main.State;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Representa a suíte de testes unitários para o PCB da classe {@link Process}.
 * <p>
 * Os cenários desta suíte verificam validações de construção, transições de estado,
 * execução por ciclo, política de prioridade dinâmica (aging), ordenação por PID
 * para indexação em AVL e consistência de igualdade semântica por identificador.
 * </p>
 *
 * @see Process
 * @author Lucas
 * @version 1.0
 * @since 2026-03-27
 */
public class ProcessTest {

    /**
     * Verifica inicialização padrão do processo.
     */
    @Test
    public void testConstructorInitializesDefaultState() {
        Process process = new Process(1, 3, 0, 5, 2);

        assertEquals(1, process.getPid());
        assertEquals(3, process.getPriority());
        assertEquals(3, process.getBasePriority());
        assertEquals(0, process.getArrivalTime());
        assertEquals(5, process.getTotalBurstTime());
        assertEquals(5, process.getRemainingTime());
        assertEquals(State.READY, process.getState());
    }

    /**
     * Verifica validações obrigatórias de entrada no construtor.
     */
    @Test
    public void testConstructorValidatesInputs() {
        assertThrows(IllegalArgumentException.class, () -> new Process(0, 1, 0, 3, 0));
        assertThrows(IllegalArgumentException.class, () -> new Process(1, -1, 0, 3, 0));
        assertThrows(IllegalArgumentException.class, () -> new Process(1, 1, -1, 3, 0));
        assertThrows(IllegalArgumentException.class, () -> new Process(1, 1, 0, 0, 0));
        assertThrows(IllegalArgumentException.class, () -> new Process(1, 1, 0, 3, -1));
    }

    /**
     * Verifica execução de tick até finalizar o processo.
     */
    @Test
    public void testExecuteTickFinishesProcess() {
        Process process = new Process(10, 1, 0, 1, 0);
        process.setState(State.RUNNING);

        boolean movedToIo = process.executeTick();

        assertFalse(movedToIo);
        assertEquals(0, process.getRemainingTime());
        assertEquals(1, process.getExecutedCycles());
        assertEquals(State.FINISHED, process.getState());
    }

    /**
     * Verifica bloqueio por I/O quando atinge o limiar configurado.
     */
    @Test
    public void testExecuteTickBlocksOnIoThreshold() {
        Process process = new Process(11, 1, 0, 5, 2);
        process.setState(State.RUNNING);

        boolean firstTick = process.executeTick();
        process.setState(State.RUNNING);
        boolean secondTick = process.executeTick();

        assertFalse(firstTick);
        assertTrue(secondTick);
        assertEquals(State.BLOCKED, process.getState());
    }

    /**
     * Verifica que ioThreshold igual a zero impede bloqueio por I/O.
     */
    @Test
    public void testExecuteTickWithIoThresholdZeroNeverBlocks() {
        Process process = new Process(111, 1, 0, 3, 0);

        process.setState(State.RUNNING);
        boolean firstTick = process.executeTick();
        assertFalse(firstTick);
        assertEquals(State.RUNNING, process.getState());

        process.setState(State.RUNNING);
        boolean secondTick = process.executeTick();
        assertFalse(secondTick);
        assertEquals(State.RUNNING, process.getState());
    }

    /**
     * Verifica que finalizacao tem precedencia sobre bloqueio de I/O no ultimo ciclo.
     */
    @Test
    public void testExecuteTickFinishingHasPriorityOverIoBlock() {
        Process process = new Process(112, 1, 0, 1, 1);

        process.setState(State.RUNNING);
        boolean movedToIo = process.executeTick();

        assertFalse(movedToIo, "Ao finalizar no mesmo ciclo do limiar, o processo nao deve bloquear.");
        assertEquals(0, process.getRemainingTime());
        assertEquals(State.FINISHED, process.getState());
    }

    /**
     * Verifica proteção de estado inválido ao executar tick fora de RUNNING.
     */
    @Test
    public void testExecuteTickRequiresRunningState() {
        Process process = new Process(12, 1, 0, 3, 0);

        IllegalStateException exception = assertThrows(IllegalStateException.class, process::executeTick);
        assertEquals("O processo precisa estar em RUNNING para executar um ciclo.", exception.getMessage());
    }

    /**
     * Verifica aging de prioridade para processos em READY.
     */
    @Test
    public void testIncrementWaitAppliesAgingInReadyState() {
        Process process = new Process(13, 2, 0, 4, 0);
        process.setState(State.READY);

        process.incrementWait();

        assertEquals(1, process.getWaitingTime());
        assertEquals(3, process.getPriority());
    }

    /**
     * Verifica que processo bloqueado acumula espera sem envelhecimento de prioridade.
     */
    @Test
    public void testIncrementWaitInBlockedStateDoesNotIncreasePriority() {
        Process process = new Process(14, 2, 0, 4, 0);
        process.setState(State.BLOCKED);

        process.incrementWait();

        assertEquals(1, process.getWaitingTime());
        assertEquals(2, process.getPriority());
    }

    /**
     * Verifica que RUNNING e FINISHED nao acumulam espera nem alteram prioridade.
     */
    @Test
    public void testIncrementWaitDoesNothingInRunningAndFinished() {
        Process running = new Process(113, 5, 0, 3, 0);
        running.setState(State.RUNNING);
        running.incrementWait();

        assertEquals(0, running.getWaitingTime());
        assertEquals(5, running.getPriority());

        Process finished = new Process(114, 6, 0, 1, 0);
        finished.setState(State.RUNNING);
        finished.executeTick();
        finished.incrementWait();

        assertEquals(0, finished.getWaitingTime());
        assertEquals(6, finished.getPriority());
        assertEquals(State.FINISHED, finished.getState());
    }

    /**
     * Verifica reset da prioridade dinâmica para a prioridade base.
     */
    @Test
    public void testResetPriorityRestoresBasePriority() {
        Process process = new Process(15, 4, 0, 3, 0);
        process.setPriority(9);

        process.resetPriority();

        assertEquals(4, process.getPriority());
    }

    /**
     * Verifica rejeicao de prioridade negativa ao atualizar prioridade dinamica.
     */
    @Test
    public void testSetPriorityRejectsNegativeValue() {
        Process process = new Process(115, 4, 0, 3, 0);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> process.setPriority(-1)
        );

        assertEquals("A prioridade não pode ser negativa.", exception.getMessage());
    }

    /**
     * Verifica rejeicao de estado nulo.
     */
    @Test
    public void testSetStateRejectsNull() {
        Process process = new Process(116, 4, 0, 3, 0);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> process.setState(null)
        );

        assertEquals("O estado do processo não pode ser nulo.", exception.getMessage());
    }

    /**
     * Verifica ordenação natural por PID para indexação AVL.
     */
    @Test
    public void testCompareToUsesPidOrdering() {
        Process first = new Process(1, 5, 0, 3, 0);
        Process second = new Process(2, 1, 0, 3, 0);

        assertTrue(first.compareTo(second) < 0);
        assertTrue(second.compareTo(first) > 0);
        assertEquals(0, first.compareTo(new Process(1, 9, 0, 3, 0)));
    }

    /**
     * Verifica rejeicao de comparacao por PID com argumento nulo.
     */
    @Test
    public void testCompareToRejectsNull() {
        Process process = new Process(117, 1, 0, 3, 0);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> process.compareTo(null)
        );

        assertEquals("Não é possível comparar com um processo nulo.", exception.getMessage());
    }

    /**
     * Verifica comparação por prioridade dinâmica para o escalonador.
     */
    @Test
    public void testCompareByPriorityUsesDynamicPriority() {
        Process high = new Process(20, 8, 0, 3, 0);
        Process low = new Process(21, 2, 0, 3, 0);

        assertTrue(high.compareByPriority(low) > 0);
        assertTrue(low.compareByPriority(high) < 0);

        Process tieA = new Process(30, 5, 0, 3, 0);
        Process tieB = new Process(31, 5, 0, 3, 0);
        assertTrue(tieA.compareByPriority(tieB) > 0);
    }

    /**
     * Verifica rejeicao de comparacao por prioridade com argumento nulo.
     */
    @Test
    public void testCompareByPriorityRejectsNull() {
        Process process = new Process(118, 1, 0, 3, 0);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> process.compareByPriority(null)
        );

        assertEquals("Não é possível comparar com um processo nulo.", exception.getMessage());
    }

    /**
     * Verifica igualdade e hash baseados no PID.
     */
    @Test
    public void testEqualsAndHashCodeUsePid() {
        Process a = new Process(99, 1, 0, 3, 0);
        Process b = new Process(99, 9, 5, 7, 2);
        Process c = new Process(100, 1, 0, 3, 0);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
    }

    /**
     * Verifica comportamento de equals para nulo e tipo diferente.
     */
    @Test
    public void testEqualsAgainstNullAndDifferentType() {
        Process process = new Process(119, 1, 0, 3, 0);

        assertNotEquals(null, process);
        assertNotEquals("process", process);
    }
}
