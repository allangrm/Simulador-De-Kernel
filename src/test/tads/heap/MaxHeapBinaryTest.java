package test.tads.heap;

import main.tads.heap.MaxHeapBinary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Representa a suite de testes unitarios para a estrutura: Heap Binario Maximo.
 *
 * @see MaxHeapBinary
 * @author Raffael Wagner
 * @version 2.0
 * @since 2026-03-28
 */
public class MaxHeapBinaryTest {

    private MaxHeapBinary<Integer> heap;

    /**
     * Recria um heap binario maximo vazio antes de cada cenario de teste.
     */
    @BeforeEach
    public void setUp() {
        heap = new MaxHeapBinary<>(10);
    }

    /**
     * Verifica se o heap inicia vazio.
     */
    @Test
    public void testHeapStartsEmpty() {
        assertTrue(heap.isEmpty(), "O heap deve iniciar vazio.");
        assertEquals(0, heap.size(), "O tamanho inicial do heap deve ser 0.");
    }

    /**
     * Verifica se insercoes aumentam o tamanho e mantem o maior valor na raiz.
     */
    @Test
    public void testInsertIncreasesSizeAndUpdatesPeek() {
        heap.insert(4);
        heap.insert(9);
        heap.insert(2);

        assertFalse(heap.isEmpty(), "O heap nao deve estar vazio apos insercoes.");
        assertEquals(3, heap.size(), "O tamanho deve refletir o numero de insercoes.");
        assertEquals(9, heap.peek(), "O maior elemento deve permanecer na raiz.");
    }

    /**
     * Verifica se a heap respeita a ordem decrescente em extracoes sucessivas.
     */
    @Test
    public void testExtractMaxReturnsElementsInDescendingOrder() {
        heap.insert(7);
        heap.insert(1);
        heap.insert(10);
        heap.insert(3);
        heap.insert(8);

        assertEquals(10, heap.extractMax(), "A primeira extracao deve retornar o maior elemento.");
        assertEquals(8, heap.extractMax(), "A segunda extracao deve retornar o proximo maior elemento.");
        assertEquals(7, heap.extractMax(), "A terceira extracao deve manter a ordem decrescente.");
        assertEquals(3, heap.extractMax(), "A quarta extracao deve manter a ordem decrescente.");
        assertEquals(1, heap.extractMax(), "A ultima extracao deve retornar o menor elemento restante.");
        assertTrue(heap.isEmpty(), "O heap deve ficar vazio apos remover todos os elementos.");
    }

    /**
     * Verifica se a extracao reorganiza corretamente a estrutura.
     */
    @Test
    public void testExtractMaxReorganizesHeapAndDecreasesSize() {
        heap.insert(15);
        heap.insert(10);
        heap.insert(12);
        heap.insert(8);
        heap.insert(9);

        assertEquals(15, heap.extractMax(), "A extracao deve devolver o maior elemento atual.");
        assertEquals(4, heap.size(), "O tamanho deve diminuir apos extrair o maximo.");
        assertEquals(12, heap.peek(), "A nova raiz deve ser o maior elemento remanescente.");
    }

    /**
     * Verifica se o peek nao remove o elemento consultado.
     */
    @Test
    public void testPeekDoesNotRemoveElement() {
        heap.insert(5);
        heap.insert(11);

        assertEquals(11, heap.peek(), "O peek deve retornar o maior elemento.");
        assertEquals(2, heap.size(), "O tamanho nao pode diminuir apos chamar peek().");
    }

    /**
     * Verifica se o heap cresce automaticamente quando a capacidade inicial e excedida.
     */
    @Test
    public void testInsertBeyondInitialCapacityTriggersResize() {
        MaxHeapBinary<Integer> smallHeap = new MaxHeapBinary<>(2);

        smallHeap.insert(4);
        smallHeap.insert(9);
        smallHeap.insert(1);
        smallHeap.insert(7);

        assertEquals(4, smallHeap.size(), "O heap deve aceitar mais elementos do que a capacidade inicial.");
        assertEquals(9, smallHeap.peek(), "O resize nao pode comprometer a raiz da heap.");
        assertEquals(9, smallHeap.extractMax());
        assertEquals(7, smallHeap.extractMax());
        assertEquals(4, smallHeap.extractMax());
        assertEquals(1, smallHeap.extractMax());
    }

    /**
     * Verifica se a heap pode ser limpa e reutilizada normalmente.
     */
    @Test
    public void testClearEmptiesHeapAndAllowsReuse() {
        heap.insert(6);
        heap.insert(2);
        heap.insert(14);

        heap.clear();

        assertTrue(heap.isEmpty(), "O heap deve ficar vazio apos clear().");
        assertEquals(0, heap.size(), "O tamanho deve ser 0 apos clear().");

        heap.insert(20);
        assertEquals(1, heap.size(), "O heap deve aceitar novas insercoes apos clear().");
        assertEquals(20, heap.peek(), "A estrutura deve funcionar normalmente apos reuso.");
    }

    /**
     * Verifica acesso posicional apenas para indices validos.
     */
    @Test
    public void testGetElementAtReturnsElementForValidIndex() {
        heap.insert(3);
        heap.insert(10);
        heap.insert(7);

        assertEquals(10, heap.getElementAt(0), "A raiz deve conter o maior elemento.");
        assertNotNull(heap.getElementAt(1), "Indices validos devem retornar elementos existentes.");
        assertNotNull(heap.getElementAt(2), "Indices validos devem retornar elementos existentes.");
    }

    /**
     * Verifica se o construtor rejeita capacidade invalida.
     */
    @Test
    public void testConstructorRejectsInvalidCapacity() {
        IllegalArgumentException zeroCapacity = assertThrows(
                IllegalArgumentException.class,
                () -> new MaxHeapBinary<>(0)
        );
        assertEquals("A capacidade inicial deve ser maior que zero.", zeroCapacity.getMessage());

        IllegalArgumentException negativeCapacity = assertThrows(
                IllegalArgumentException.class,
                () -> new MaxHeapBinary<>(-5)
        );
        assertEquals("A capacidade inicial deve ser maior que zero.", negativeCapacity.getMessage());
    }

    /**
     * Verifica se a insercao rejeita referencia nula.
     */
    @Test
    public void testInsertRejectsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> heap.insert(null)
        );

        assertEquals("Elemento não pode ser nulo.", exception.getMessage());
    }

    /**
     * Verifica se peek em heap vazio lanca excecao.
     */
    @Test
    public void testPeekOnEmptyHeapThrowsException() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> heap.peek());
        assertEquals("Heap está vazia.", exception.getMessage());
    }

    /**
     * Verifica se extractMax em heap vazio lanca excecao.
     */
    @Test
    public void testExtractMaxOnEmptyHeapThrowsException() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> heap.extractMax());
        assertEquals("Heap está vazia.", exception.getMessage());
    }

    /**
     * Verifica se getElementAt rejeita indices invalidos.
     */
    @Test
    public void testGetElementAtRejectsInvalidIndex() {
        heap.insert(5);

        IndexOutOfBoundsException negativeIndex = assertThrows(
                IndexOutOfBoundsException.class,
                () -> heap.getElementAt(-1)
        );
        assertEquals("Indice está fora dos limites da heap.", negativeIndex.getMessage());

        IndexOutOfBoundsException oversizedIndex = assertThrows(
                IndexOutOfBoundsException.class,
                () -> heap.getElementAt(1)
        );
        assertEquals("Indice está fora dos limites da heap.", oversizedIndex.getMessage());
    }
}
