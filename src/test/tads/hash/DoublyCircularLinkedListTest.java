package test.tads.hash;

import main.tads.hash.DoublyCircularLinkedList;
import main.tads.hash.NodeDouble;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Representa a suíte de testes unitários para a estrutura DoublyCircularLinkedList.
 * <p>
 * Os cenários validam rigorosamente a manutenção do ciclo bidirecional,
 * garantindo que as referências de {@code head} e {@code tail} permaneçam
 * consistentemente conectadas durante inserções, remoções, buscas
 * bidirecionais e operações em massa.
 * </p>
 *
 * @see DoublyCircularLinkedList
 * @author Allan Guilherme
 * @version 1.0
 * @since 2026-03-28
 */
public class DoublyCircularLinkedListTest {

    private DoublyCircularLinkedList<Integer> list;

    /**
     * Prepara um ambiente limpo recriando uma lista vazia antes de cada cenário de teste.
     */
    @BeforeEach
    public void setUp() {
        list = new DoublyCircularLinkedList<>();
    }

    /**
     * Verifica se o construtor inicializa a lista vazia corretamente,
     * garantindo que head, tail sejam nulos e o tamanho seja zero.
     */
    @Test
    public void testConstructorInitializesEmptyList() {
        assertTrue(list.isEmpty(), "A lista instanciada deve ser vazia");
        assertEquals(0, list.getSize(), "O tamanho inicil da lista vazia deve ser 0");
        assertNull(list.getHead(), "Head deve ser nulo");
        assertNull(list.getTail(), "Tail deve ser nulo");
    }

    /**
     * Verifica se a inserção no início em uma lista vazia estabelece
     * o nó como head e tail simultaneamente, apontando para si mesmo.
     */
    @Test
    public void testInsertFirstInEmptyListEstablishesCircularity() {
        list.insertFirst(10);

        assertFalse(list.isEmpty(), "A lista não deve estar vazia após a inserção.");
        assertEquals(1, list.getSize(), "O tamanho da lista deve ser 1 após uma inserção.");
        assertEquals(10, list.getFirstElement(), "O elemento inserido deve estar no início da lista.");
        assertEquals(list.getHead(), list.getHead().getNext(), "Com apenas 1 item, o proximo  elemento deve apontar para si mesmo");
        assertEquals(list.getHead(), list.getTail(), "Com apenas 1 item, Head e Tail devem estar no mesmo local");

    }

    /**
     * Verifica se a inserção no início em uma lista populada atualiza
     * o head e mantém a conexão cíclica com o tail.
     */
    @Test
    public void testInsertFirstInPopulatedListUpdatesHeadAndCircularity() {

        list.insertFirst(10);
        list.insertFirst(20);
        list.insertFirst(30);


        assertFalse(list.isEmpty(), "A lista não deve estar vazia após a inserção.");
        assertEquals(30, list.getFirstElement(), "O primeiro elemento deve ser 30");
        assertEquals(10, list.getHead().getPrevious().getData(), "O anterior de 30(Head) deve ser 10(Tail)");
        assertEquals(30, list.getTail().getNext().getData(), "O proximo de 10(Tail) deve ser 30(Head)");
    }

    /**
     * Verifica se a inserção no final em uma lista vazia estabelece
     * o ciclo isolado corretamente (head == tail).
     */
    @Test
    public void testInsertLastInEmptyListEstablishesCircularity() {
        list.insertLast(20);

        assertFalse(list.isEmpty(), "A lista não deve estar vazia após a inserção.");
        assertEquals(20, list.getLastElement(), "O último elemento deve ser 20");
        assertEquals(1, list.getSize(), "O tamanho da lista deve ser 1 após uma inserção.");
        assertEquals(list.getTail(), list.getTail().getPrevious(), "Com apenas 1 elemento, o ultimo elemento aponta para si mesmo");
        assertEquals(list.getTail(), list.getHead(), "Com apenas 1 elemento, Tail deve ser igual a Head");
    }

    /**
     * Verifica se a inserção no final em uma lista populada atualiza
     * o tail e reconecta corretamente o ciclo com o head.
     */
    @Test
    public void testInsertLastInPopulatedListUpdatesTailAndCircularity() {

        list.insertLast(10);
        list.insertLast(20);
        list.insertLast(30);


        assertFalse(list.isEmpty(), "A lista não deve estar vazia após a inserção.");
        assertEquals(30, list.getLastElement(), "O último elemento deve ser 30");
        assertEquals(30, list.getHead().getPrevious().getData(), "O anterior de 10(Head) deve ser 30(Tail)");
        assertEquals(10, list.getTail().getNext().getData(), "O proximo de 30(Tail) deve ser 10(Head)");
    }


    /**
     * Verifica se a remoção do primeiro elemento extrai o dado correto,
     * atualiza o head e refaz a costura com o tail.
     */
    @Test
    public void testRemoveFirstExtractsDataAndMaintainsCircularity() {
        list.insertLast(10);
        list.insertLast(20);
        list.insertLast(30);

        int removed = list.removeFirst();

        assertEquals(10, removed, "O dado extraído deve ser 10 (antigo Head).");
        assertEquals(2, list.getSize(), "O tamanho deve cair para 2.");
        assertEquals(20, list.getFirstElement(), "O novo Head deve ser 20.");

        assertEquals(30, list.getHead().getPrevious().getData(), "O anterior do novo Head (20) deve ser o Tail (30).");
        assertEquals(20, list.getTail().getNext().getData(), "O próximo do Tail (30) deve ser o novo Head (20).");
    }

    /**
     * Verifica se a remoção do último elemento (tail) extrai o dado correto,
     * atualiza o tail para o penúltimo nó e refaz a costura com o head.
     */
    @Test
    public void testRemoveLastExtractsDataAndMaintainsCircularity() {
        list.insertLast(10);
        list.insertLast(20);
        list.insertLast(30);

        int removed = list.removeLast();

        assertEquals(30, removed, "O dado extraído deve ser 30 (antigo Tail).");
        assertEquals(2, list.getSize(), "O tamanho deve cair para 2.");
        assertEquals(20, list.getLastElement(), "O novo Tail deve ser 20.");

        assertEquals(20, list.getHead().getPrevious().getData(), "O anterior do Head (10) deve ser o novo Tail (20).");
        assertEquals(10, list.getTail().getNext().getData(), "O próximo do novo Tail (20) deve ser o Head (10).");
    }

    /**
     * Verifica se a remoção do único elemento existente na lista
     * redefine a estrutura para o estado de vazia (head nulo, tail nulo).
     */
    @Test
    public void testRemoveExtremesOnSingleElementListClearsStructure() {
        list.insertFirst(10);
        list.removeFirst();

        assertTrue(list.isEmpty(), "A lista deve voltar a ficar vazia.");
        assertEquals(0, list.getSize(), "O tamanho deve zerar.");
        assertNull(list.getHead(), "O Head deve voltar a ser nulo.");
        assertNull(list.getTail(), "O Tail deve voltar a ser nulo.");
    }


    /**
     * Verifica se o método removeElement consegue remover dados nas
     * extremidades (head ou tail) delegando corretamente a operação.
     */
    @Test
    public void testRemoveElementAtHeadAndTail() {
        list.insertLast(10);
        list.insertLast(20);
        list.insertLast(30);

        list.removeElement(10);
        assertEquals(20, list.getFirstElement(), "O novo Head deve ser 20 após remover o 10.");
        assertEquals(2, list.getSize());

        list.removeElement(30);
        assertEquals(20, list.getLastElement(), "O novo Tail deve ser 20 após remover o 30.");
        assertEquals(1, list.getSize());
    }

    /**
     * Verifica se o método removeElement extrai corretamente um nó localizado
     * no meio da estrutura, costurando os nós vizinhos de forma isolada.
     */
    @Test
    public void testRemoveElementInMiddleMaintainsListIntegrity() {
        list.insertLast(10);
        list.insertLast(20);
        list.insertLast(30);

        list.removeElement(20);

        assertEquals(2, list.getSize(), "O tamanho deve ser 2.");
        assertEquals(10, list.getFirstElement(), "O Head continua sendo 10.");
        assertEquals(30, list.getLastElement(), "O Tail continua sendo 30.");

        assertEquals(30, list.getHead().getNext().getData(), "O próximo de 10 deve ser direto o 30.");
        assertEquals(10, list.getTail().getPrevious().getData(), "O anterior de 30 deve ser direto o 10.");
    }


    /**
     * Verifica se a busca bidirecional localiza elementos que estão mais
     * próximos do head e elementos que estão mais próximos do tail.
     */
    @Test
    public void testSearchElementFindsDataViaBidirectionalTraversal() {
        list.insertLast(10);
        list.insertLast(20);
        list.insertLast(30);
        list.insertLast(40);

        NodeDouble<Integer> nodeHeadSide = list.searchElement(20);
        NodeDouble<Integer> nodeTailSide = list.searchElement(30);

        assertNotNull(nodeHeadSide, "A busca deve encontrar o 20.");
        assertEquals(20, nodeHeadSide.getData());

        assertNotNull(nodeTailSide, "A busca deve encontrar o 30.");
        assertEquals(30, nodeTailSide.getData());
    }

    /**
     * Verifica se a busca retorna nulo ao varrer a lista inteira e
     * não localizar o dado solicitado (cruzamento de ponteiros).
     */
    @Test
    public void testSearchElementReturnsNullForMissingData() {
        list.insertLast(10);
        list.insertLast(20);

        NodeDouble<Integer> result = list.searchElement(99);
        assertNull(result, "A busca por um elemento inexistente deve retornar nulo.");
    }

    /**
     * Verifica se operações que exigem dados lançam a exceção
     * IllegalArgumentException quando a lista está vazia.
     */
    @Test
    public void testOperationsThrowExceptionWhenListIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> list.removeFirst());
        assertThrows(IllegalArgumentException.class, () -> list.removeLast());
        assertThrows(IllegalArgumentException.class, () -> list.removeElement(10));
        assertThrows(IllegalArgumentException.class, () -> list.searchElement(10));
        assertThrows(IllegalArgumentException.class, () -> list.getFirstElement());
        assertThrows(IllegalArgumentException.class, () -> list.getLastElement());
    }


    /**
     * Verifica a resiliência do ciclo durante uma série caótica de
     * inserções e remoções intercaladas.
     */
    @Test
    public void testInterleavingOperationsMaintainStructuralIntegrity() {
        list.insertFirst(20);
        list.insertLast(30);
        list.insertFirst(10);
        list.removeLast();
        list.insertLast(40);
        list.removeFirst();

        assertEquals(2, list.getSize(), "Tamanho final deve ser 2.");
        assertEquals(20, list.getFirstElement(), "O Head final deve ser 20.");
        assertEquals(40, list.getLastElement(), "O Tail final deve ser 40.");

        assertEquals(40, list.getHead().getPrevious().getData());
        assertEquals(20, list.getTail().getNext().getData());
    }

    /**
     * Verifica se a estrutura suporta grande volume de dados sem
     * corromper a ligação entre head e tail.
     */
    @Test
    public void testLargeVolumeOfDataMaintainsPerformanceAndPointers() {
        int volume = 1000;

        for (int i = 0; i < volume; i++) {
            list.insertLast(i);
        }

        assertEquals(volume, list.getSize(), "Deve suportar 1000 itens.");
        assertEquals(0, list.getFirstElement(), "O Head deve ser o item 0.");
        assertEquals(999, list.getLastElement(), "O Tail deve ser o item 999.");

        assertEquals(999, list.getHead().getPrevious().getData());

        for (int i = 0; i < volume; i++) {
            list.removeFirst();
        }

        assertTrue(list.isEmpty(), "A lista deve ficar perfeitamente limpa após o esgotamento do volume.");
    }
}