package test.tads;

import main.tads.Queue;
import main.tads.QueueDoubleStack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Representa a suíte de testes unitários para a estrutura: Fila utilizando Duas Pilhas.
 * <p>
 * Esta classe utiliza o framework JUnit 5 para garantir que a implementação
 * de {@link QueueDoubleStack} respeite rigorosamente o contrato FIFO e o
 * encapsulamento esperado das operações baseadas na transferência de elementos.
 * </p>
 *
 * @see QueueDoubleStack
 * @author Lucas N. Araujo
 * @version 1.0
 * @since 2026-03-13
 */
public class QueueDoubleStackTest {
    private Queue<String> queue;

    @BeforeEach
    public void setUp() { // Toda vez antes de um teste
        queue = new QueueDoubleStack<>();
    }

    @Test
    public void testQueueStartsEmpty() {
        assertTrue(queue.isEmpty(), "A fila recém-criada deve estar vazia.");
        assertEquals(0, queue.size(), "O tamanho inicial da fila deve ser 0.");
    }

    @Test
    public void testEnqueueIncreasesSize() {
        queue.enqueue("Processo_Sistema");
        queue.enqueue("Processo_Navegador");

        assertFalse(queue.isEmpty(), "A fila não deve estar vazia após inserções.");
        assertEquals(2, queue.size(), "O tamanho deve refletir o número de inserções.");
    }

    @Test
    public void testFifoBehavior() {
        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");

        assertEquals("A", queue.dequeue(), "A regra FIFO falhou no primeiro elemento.");
        assertEquals("B", queue.dequeue(), "A regra FIFO falhou no segundo elemento.");

        assertEquals(1, queue.size(), "O tamanho deve ser 1 após 3 inserções e 2 remoções.");
        assertEquals("C", queue.peek(), "O elemento 'C' deve ser o novo peek.");
    }

    @Test
    public void testPeekDoesNotRemoveElement() {
        queue.enqueue("Processo_Crítico");
        queue.enqueue("Processo_Fundo");

        assertEquals("Processo_Crítico", queue.peek(), "O peek deve ser o primeiro elemento inserido.");
        assertEquals(2, queue.size(), "O tamanho da fila não pode diminuir após chamar o peek().");
    }

    @Test
    public void testInterleavingOperations() {
        queue.enqueue("A");
        queue.enqueue("B");
        assertEquals("A", queue.dequeue());

        queue.enqueue("C");
        queue.enqueue("D");
        assertEquals("B", queue.dequeue());
        assertEquals("C", queue.dequeue());
        assertEquals("D", queue.dequeue());

        assertTrue(queue.isEmpty(), "A fila deve estar vazia após remover todos os elementos.");
    }

    @Test
    public void testClearAndReuse() {
        // Enche e esvazia
        queue.enqueue("Processo_A");
        queue.enqueue("Processo_B");
        queue.dequeue();
        queue.dequeue();

        assertTrue(queue.isEmpty(), "A fila deve estar vazia antes do reuso.");

        queue.enqueue("Processo_C");
        assertEquals(1, queue.size(), "O tamanho deve ser 1 após reuso.");
        assertEquals("Processo_C", queue.dequeue(), "A fila deve funcionar normalmente após ser esvaziada.");
    }

    @Test
    public void testLargeVolumeOfData() {
        int volume = 1000;
        for (int i = 0; i < volume; i++) {
            queue.enqueue("Process_" + i);
        }

        assertEquals(volume, queue.size(), "O tamanho deve ser exatamente o volume inserido.");
        assertEquals("Process_0", queue.peek(), "O primeiro elemento ainda deve ser o Process_0.");

        for (int i = 0; i < volume; i++) {
            assertEquals("Process_" + i, queue.dequeue(), "A ordem FIFO deve ser mantida em grandes volumes.");
        }

        assertTrue(queue.isEmpty(), "A fila deve estar vazia após o processamento de grande volume.");
    }

    @Test
    public void testExceptionOnEmptyDequeue() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            queue.dequeue();
        });

        assertEquals("Erro: A fila está vazia.", exception.getMessage(), "A mensagem da exceção deve ser a definida na classe.");
    }

    @Test
    public void testPeekOnEmptyQueue() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            queue.peek();
        });
        assertEquals("Erro: A fila está vazia.", exception.getMessage(), "O peek deve lançar exceção em fila vazia.");
    }
}
