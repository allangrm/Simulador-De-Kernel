package test.tads;

import main.tads.stack.LinkedStack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import main.tads.stack.Stack;


/**
 * Representa a suíte de testes unitários para a estrutura: Pilha utilizando Lista Encadeada.
 * <p>
 * Esta classe utiliza o framework JUnit 5 para garantir que a implementação
 * de {@link LinkedStack} respeite rigorosamente o contrato LIFO e o
 * encapsulamento esperado das operações baseadas na utilização de uma lista encadeada.
 * </p>
 *
 * @see LinkedStack
 * @author Raffael Wagner
 * @version 1.0
 * @since 2026-03-15
 */
public class LinkedStackTest {
    private Stack<String> stack;

    /**
     * Recria uma pilha vazia antes de cada cenário de teste.
     */
    @BeforeEach
    public void setUp() {
        stack = new LinkedStack<>();
    }
    /**
     * Verifica se a pilha inicia vazia.
     */
    @Test
    public void testStackStartsEmpty() {
        assertTrue(stack.isEmpty(), "A pilha recém-criada deve estar vazia.");
        assertEquals(0, stack.size(), "O tamanho inicial da pilha deve ser 0.");
    }
    /**
     * Verifica se o push aumenta o tamanho da pilha.
     */
    @Test
    public void testPushIncreasesSize() {
        stack.push("Processo_Sistema");
        stack.push("Processo_Navegador");
        assertEquals(2, stack.size(), "O tamanho da pilha deve ser 2 após 2 inserções.");
    }
    /**
     * Verifica se o pop diminui o tamanho da pilha.
     */
    @Test
    public void testPopDecreasesSize() {
        stack.push("Processo_Sistema");
        stack.push("Processo_Navegador");
        stack.pop();
        assertEquals(1, stack.size(), "O tamanho da pilha deve ser 1 após 2 inserções e 1 remoção.");
    }
    /**
     * Verifica se o peek nao remove o elemento consultado.
     */
    @Test
    public void testPeekDoesNotRemoveElement() {
        stack.push("Processo_Sistema");
        stack.push("Processo_Navegador");
        assertEquals("Processo_Navegador", stack.peek(), "O peek deve ser o último elemento inserido.");
        assertEquals(2, stack.size(), "O tamanho da pilha não pode diminuir após chamar o peek().");
    }
    /**
     * Verifica o comportamento correto com operacoes intercaladas.
     */
    @Test
    public void testInterleavingOperations() {
        stack.push("Processo_Sistema");
        stack.push("Processo_Navegador");
        assertEquals("Processo_Navegador", stack.pop(), "O pop deve retornar o último elemento inserido.");
        assertEquals(1, stack.size(), "O tamanho da pilha deve ser 1 após 2 inserções e 1 remoção.");
        assertEquals("Processo_Sistema", stack.peek(), "O peek deve ser o primeiro elemento inserido.");
        assertEquals(1, stack.size(), "O tamanho da pilha não pode diminuir após chamar o peek().");
    }
    /**
     * Verifica se a pilha pode ser reutilizada após esvaziamento completo.
     */
    @Test
    public void testClearAndReuse() {
        stack.push("Processo_Sistema");
        stack.push("Processo_Navegador");
        stack.clear();
        assertTrue(stack.isEmpty(), "A pilha deve estar vazia após o clear().");
        assertEquals(0, stack.size(), "O tamanho da pilha deve ser 0 após o clear().");
    }
    /**
     * Verifica o funcionamento da pilha com grande volume de dados.
     */
    @Test
    public void testLargeVolumeOfData() {
        int volume = 1000;

        for (int i = 0; i < volume; i++) {
            stack.push("Processo_" + i);
        }

        assertEquals(volume, stack.size(), "O tamanho da pilha deve ser exatamente o volume inserido.");

        for (int i = volume - 1; i >= 0; i--) {
            assertEquals("Processo_" + i, stack.pop(), "O pop deve retornar o elemento correto.");
        }

        assertTrue(stack.isEmpty(), "A pilha deve estar vazia após o processamento de grande volume.");
    }
    /**
     * Verifica se ocorre excecao ao remover de uma pilha vazia.
     */
    @Test
    public void testExceptionOnEmptyPop() {
        Exception exception = assertThrows(RuntimeException.class, () -> stack.pop());
        assertEquals("Erro: A pilha está vazia.", exception.getMessage(), "A mensagem da exceção deve ser a definida na classe.");
    }
    /**
     * Verifica se ocorre excecao ao consultar o topo de uma pilha vazia.
     */
    @Test
    public void testExceptionOnEmptyPeek() {
        Exception exception = assertThrows(RuntimeException.class, () -> stack.peek());
        assertEquals("Erro: A pilha está vazia.", exception.getMessage(), "A mensagem da exceção deve ser a definida na classe.");
    }
    /**
     * Verifica se a pilha respeita o contrato LIFO.
     */
    @Test
    public void testFullLIFOOrder() {
        stack.push("Processo_Sistema");
        stack.push("Processo_Navegador");
        stack.push("Processo_Editor");
        
        assertEquals("Processo_Editor", stack.pop());
        assertEquals("Processo_Navegador", stack.pop());
        assertEquals("Processo_Sistema", stack.pop());
        assertTrue(stack.isEmpty());
    }
}
