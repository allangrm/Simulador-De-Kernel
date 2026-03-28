package test.tads.linked_list;

import main.tads.linked_list.LinkedList;
import main.tads.linked_list.NodeList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
//quase ok
/**
 * Representa a suíte de testes unitários para a estrutura LinkedList.
 * <p>
 * Os cenários validam a inicialização, operações de inserção e remoção no início,
 * buscas por igualdade, controle de estado, comportamento sob grande volume de dados
 * e cenários de operações intercaladas.
 * </p>
 *
 * @see LinkedList
 * @author Allan
 * @version 1.0
 * @since 2026-03-28
 */

public class LinkedListTest {

    private LinkedList<String> list;

    /**
     * Prepara um ambiente limpo recriando uma lista vazia antes de cada cenário de teste.
     */
    @BeforeEach
    public void setUp() {
        list = new LinkedList<>();
    }

    /**
     * Verifica se o construtor inicializa a lista vazia corretamente,
     * com tamanho zero e sem referência de cabeça.
     */
    @Test
    public void testConstructorInitializesEmptyList() {
        assertTrue(list.isEmpty(), "A lista instanciada deve estar vazia.");
        assertEquals(0, list.getSize(), "O tamanho inicial da lista deve ser 0.");
        assertNull(list.getHead(), "A referência 'head' deve iniciar nula.");
    }

    @Test
    public void testListWithIntegers() {
        LinkedList<Integer> intList = new LinkedList<>();

        intList.insertFirst(10);
        intList.insertFirst(20);

        assertEquals(20, intList.getFirstElement(), "O primeiro elemento deve ser 20.");
        assertEquals(2, intList.getSize(), "O tamanho deve ser 2.");
    }

    /**
     * Verifica se a inserção no início da lista atualiza corretamente
     * a referência do primeiro nó e incrementa o tamanho da estrutura.
     */
    @Test
    public void testInsertFirstUpdatesHeadAndSize() {
        list.insertFirst("elementA");

        assertFalse(list.isEmpty(), "A lista não deve estar vazia após a inserção.");
        assertEquals(1, list.getSize(), "O tamanho da lista deve ser 1 após uma inserção.");
        assertEquals("elementA", list.getFirstElement(), "O elemento inserido deve estar no início da lista.");

        list.insertFirst("elementB");

        assertEquals(2, list.getSize(), "O tamanho da lista deve ser incrementado para 2.");
        assertEquals("elementB", list.getFirstElement(), "O novo elemento deve assumir a posição de cabeça (head).");
    }

    /**
     * Verifica se a remoção no início extrai o dado correto, atualiza
     * a cabeça da lista para o próximo elemento e decrementa o tamanho.
     */
    @Test
    public void testRemoveFirstExtractsDataAndUpdatesList() {
        list.insertFirst("elementA");
        list.insertFirst("elementB");

        String removedData = list.removeFirst();

        assertEquals("elementB", removedData, "O método deve retornar o dado que estava no topo da lista.");
        assertEquals(1, list.getSize(), "O tamanho da lista deve ser decrementado após a remoção.");
        assertEquals("elementA", list.getFirstElement(), "A cabeça da lista deve ser atualizada para o próximo elemento.");
    }

    /**
     * Verifica se uma exceção do tipo IllegalArgumentException é lançada
     * ao tentar remover um elemento de uma lista vazia.
     */
    @Test
    public void testRemoveFirstThrowsExceptionWhenListIsEmpty() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, list::removeFirst);
        assertEquals("A lista esta vazia.", exception.getMessage(), "A mensagem da exceção deve corresponder à validação de lista vazia.");
    }

    /**
     * Verifica se o metodo de busca iterativa localiza os elementos
     * armazenados em diferentes posições da estrutura.
     */
    @Test
    public void testSearchElementFindsExistingData() {
        list.insertFirst("elementC");
        list.insertFirst("elementB");
        list.insertFirst("elementA");

        assertEquals("elementA", list.searchElement("elementA"), "A busca deve localizar o elemento na cabeça da lista.");
        assertEquals("elementC", list.searchElement("elementC"), "A busca deve localizar o elemento no fim da lista.");
    }

    /**
     * Verifica se a busca retorna um valor nulo ao procurar por um
     * dado que não existe na lista atual ou em uma lista vazia.
     */
    @Test
    public void testSearchElementReturnsNullForMissingData() {
        list.insertFirst("elementA");

        assertNull(list.searchElement("elementX"), "A busca por um elemento inexistente deve retornar nulo.");
        assertNull(new LinkedList<String>().searchElement("elementY"), "A busca em uma lista recém-criada (vazia) deve retornar nulo.");
    }

    /**
     * Verifica o comportamento da lista com operações sucessivas e
     * intercaladas de inserção, busca e remoção, garantindo a integridade dos ponteiros.
     */
    @Test
    public void testInterleavingOperations() {
        list.insertFirst("node1");
        list.insertFirst("node2");

        assertEquals("node2", list.removeFirst(), "A remoção deve extrair o último elemento inserido.");
        assertEquals(1, list.getSize(), "O tamanho deve ser ajustado para 1.");

        list.insertFirst("node3");

        assertNotNull(list.searchElement("node1"), "O nó base inserido inicialmente deve continuar acessível.");
        assertEquals("node3", list.getFirstElement(), "O novo nó deve ocupar o início da estrutura.");
        assertEquals(2, list.getSize(), "O tamanho final da estrutura deve refletir as operações intercaladas.");
    }

    /**
     * Verifica a estabilidade da estrutura, o consumo de memória e a coerência
     * dos ponteiros ao inserir e remover um grande volume de dados.
     */
    @Test
    public void testLargeVolumeOfData() {
        int volume = 1000;

        for (int i = 0; i < volume; i++) {
            list.insertFirst("element_" + i);
        }

        assertEquals(volume, list.getSize(), "O tamanho da lista deve corresponder exatamente ao volume inserido.");
        assertEquals("element_999", list.getFirstElement(), "A cabeça deve ser o último elemento da iteração de inserção.");

        for (int i = volume - 1; i >= 0; i--) {
            assertEquals("element_" + i, list.removeFirst(), "Os elementos devem ser removidos na ordem correta da pilha implícita.");
        }

        assertTrue(list.isEmpty(), "A lista deve estar completamente vazia após o esgotamento do volume.");
    }
}