package test.tads.hash;

import main.tads.hash.HashTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Representa a suíte de testes unitários para a estrutura HashTable.
 * <p>
 * Os cenários validam a correta alocação de capacidade baseada em primos seguros,
 * o armazenamento e recuperação em O(1), o tratamento dinâmico de colisões
 * por encadeamento e a resiliência da tabela contra buscas e remoções inexistentes.
 * </p>
 *
 * @see HashTable
 * @author Allan Guilherme
 * @version 1.0
 * @since 2026-03-29
 */
public class HashTableTest {

    private HashTable<Integer> hashTable;

    @BeforeEach
    public void setUp() {
        hashTable = new HashTable<>(10);
    }

    /**
     * Verifica se o construtor autoajusta a capacidade para o próximo número
     * primo que seja matematicamente distante de uma potência de 2.
     */
    @Test
    public void testConstructorAdjustsCapacityToSafePrime() {
        HashTable<Integer> customTable = new HashTable<>(8);

        assertEquals(11, customTable.getCapacity(), "A capacidade solicitada (8) deve ser ajustada para o primo seguro mais próximo (11).");
        assertTrue(customTable.isEmpty(), "A tabela recém-instanciada deve estar vazia.");
        assertEquals(0, customTable.getSize(), "O tamanho inicial da tabela deve ser 0.");
    }

    /**
     * Verifica a corretude do método público isSafe para diferentes valores.
     */
    @Test
    public void testIsSafeEvaluatesCorrectly() {
        assertFalse(hashTable.isSafe(8), "O número 8 (potência exata de 2) não deve ser seguro.");
        assertFalse(hashTable.isSafe(9), "O número 9 está muito próximo da potência 8, não deve ser seguro.");
        assertTrue(hashTable.isSafe(11), "O número 11 mantém uma margem > 10% das potências 8 e 16, deve ser seguro.");
    }

    /**
     * Verifica se a inserção incrementa o tamanho e armazena o dado acessível por busca.
     */
    @Test
    public void testInsertIncreasesSizeAndStoresData() {
        hashTable.insert(42);

        assertFalse(hashTable.isEmpty(), "A tabela não deve estar vazia após uma inserção.");
        assertEquals(1, hashTable.getSize(), "O tamanho da tabela deve ser 1.");
        assertEquals(42, hashTable.search(42), "A busca deve retornar exatamente o dado inserido.");
    }

    /**
     * Verifica se múltiplos elementos que resultam no mesmo índice (colisão)
     * são armazenados corretamente na mesma gaveta por encadeamento.
     */
    @Test
    public void testCollisionsAreHandledCorrectly() {
        int capacity = hashTable.getCapacity();
        int item1 = 1;
        int item2 = 1 + capacity;
        int item3 = 1 + (capacity * 2);

        hashTable.insert(item1);
        hashTable.insert(item2);
        hashTable.insert(item3);

        assertEquals(3, hashTable.getSize(), "O tamanho da tabela deve refletir todas as inserções, ignorando as colisões.");
        assertEquals(item1, hashTable.search(item1), "O primeiro item da colisão deve ser preservado e encontrado.");
        assertEquals(item2, hashTable.search(item2), "O segundo item da colisão deve ser preservado e encontrado.");
        assertEquals(item3, hashTable.search(item3), "O terceiro item da colisão deve ser preservado e encontrado.");
    }

    /**
     * Verifica se a busca retorna nulo quando a gaveta está vazia ou
     * quando o elemento não está na lista encadeada daquela gaveta.
     */
    @Test
    public void testSearchReturnsNullForMissingElement() {
        hashTable.insert(10);
        hashTable.insert(20);

        assertNull(hashTable.search(99), "A busca por um elemento inexistente deve retornar nulo.");
        assertNull(new HashTable<Integer>(5).search(10), "A busca em uma tabela vazia deve retornar nulo.");
    }

    /**
     * Verifica se a remoção extrai o dado correto e decrementa o tamanho global da estrutura.
     */
    @Test
    public void testRemoveDecrementsSizeAndRemovesData() {
        hashTable.insert(50);
        hashTable.insert(100);

        hashTable.remove(50);

        assertEquals(1, hashTable.getSize(), "O tamanho da tabela deve decrementar após a remoção.");
        assertNull(hashTable.search(50), "O elemento removido não deve mais ser encontrado pela busca.");
        assertEquals(100, hashTable.search(100), "A remoção de um elemento não deve afetar os demais.");
    }

    /**
     * Verifica se o método de remoção finaliza silenciosamente sem causar
     * erros de estado ou tamanho ao tentar remover elementos inexistentes.
     */
    @Test
    public void testRemoveDoesNothingForMissingElement() {
        hashTable.insert(77);
        int initialSize = hashTable.getSize();

        hashTable.remove(99);

        assertEquals(initialSize, hashTable.getSize(), "A tentativa de remoção de elemento inexistente não deve alterar o tamanho.");
        assertEquals(77, hashTable.search(77), "Os dados existentes não devem ser corrompidos por tentativas inválidas de remoção.");
    }

    /**
     * Verifica se a estrutura suporta e indexa corretamente um alto volume
     * de dados com geração massiva de colisões inevitáveis.
     */
    @Test
    public void testLargeVolumeOfDataWithMassiveCollisions() {
        int volume = 1000;

        for (int i = 0; i < volume; i++) {
            hashTable.insert(i);
        }

        assertEquals(volume, hashTable.getSize(), "A Tabela Hash deve comportar o grande volume de inserções.");

        for (int i = 0; i < volume; i++) {
            assertEquals(i, hashTable.search(i), "Todos os itens devem ser recuperáveis mesmo após indexação massiva.");
        }

        for (int i = 0; i < volume; i++) {
            hashTable.remove(i);
        }

        assertTrue(hashTable.isEmpty(), "A tabela deve voltar ao estado limpo após a remoção de todos os itens.");
    }
    /**
     * Verifica se a estrutura reposiciona corretamente chaves que geram
     * hash codes negativos, prevenindo erros de índice fora dos limites do array.
     */
    @Test
    public void testNegativeHashCodeIsHandledCorrectly() {
        hashTable.insert(-42);
        hashTable.insert(-15);

        assertEquals(2, hashTable.getSize(), "O tamanho deve incrementar mesmo com chaves negativas.");
        assertEquals(-42, hashTable.search(-42), "O elemento negativo deve ser localizado com sucesso.");
        assertEquals(-15, hashTable.search(-15), "O elemento negativo deve ser localizado com sucesso.");

        hashTable.remove(-42);
        assertNull(hashTable.search(-42), "A remoção deve funcionar perfeitamente com hashes negativos.");
        assertEquals(1, hashTable.getSize(), "O tamanho deve decrementar após remoção de chave negativa.");
    }

    /**
     * Verifica o comportamento da tabela ao inserir elementos com valores idênticos,
     * garantindo que não há bloqueio e que o tamanho reflete a duplicidade.
     */
    @Test
    public void testInsertAllowsDuplicateElements() {
        hashTable.insert(50);
        hashTable.insert(50);
        hashTable.insert(50);

        assertEquals(3, hashTable.getSize(), "O tamanho deve refletir todas as inserções, incluindo dados duplicados.");
        assertEquals(50, hashTable.search(50), "A busca deve retornar a primeira ocorrência do dado duplicado.");
    }

    /**
     * Verifica se a lógica de saltos para o próximo número primo seguro
     * funciona para diferentes faixas de valores solicitados no construtor.
     */
    @Test
    public void testConstructorWithVariousCapacitiesToTriggerNextPrime() {
        HashTable<Integer> table1 = new HashTable<>(20);
        assertEquals(23, table1.getCapacity(), "A capacidade 20 deve saltar para o primo seguro 23.");

        HashTable<Integer> table2 = new HashTable<>(2);
        assertEquals(3, table2.getCapacity(), "A capacidade 2 deve saltar para o primo seguro 3.");

        HashTable<Integer> table3 = new HashTable<>(30);
        assertEquals(41, table3.getCapacity(), "A capacidade 30 deve saltar para o primo seguro 31.");
    }
    /**
     * Verifica se a tentativa de inserir, buscar ou remover dados nulos
     * dispara a exceção adequada de ponteiro nulo, validando o contrato de restrição.
     */
    @Test
    public void testOperationsWithNullDataThrowException() {
        assertThrows(NullPointerException.class, () -> hashTable.insert(null));
        assertThrows(NullPointerException.class, () -> hashTable.search(null));

        hashTable.insert(10);
        assertThrows(NullPointerException.class, () -> hashTable.remove(null));
    }

    /**
     * Verifica a resiliência do motor matemático ao receber capacidades
     * zeradas ou negativas, garantindo a autocorreção para o menor primo seguro.
     */
    @Test
    public void testConstructorRecoversFromZeroOrNegativeCapacity() {
        HashTable<Integer> tableZero = new HashTable<>(0);
        assertTrue(tableZero.getCapacity() >= 3, "A tabela deve se autocorrigir para um primo positivo seguro partindo de 0.");

        HashTable<Integer> tableNegative = new HashTable<>(-10);
        assertTrue(tableNegative.getCapacity() >= 3, "A tabela deve ignorar valores negativos e buscar o primeiro primo positivo.");
    }

    /**
     * Verifica a sincronia exata entre o tamanho da estrutura e o
     * estado de vazio após operações pontuais de inserção e remoção.
     */
    @Test
    public void testIsEmptyAndSizeSyncPerfectlyAfterFullClear() {
        hashTable.insert(100);
        hashTable.insert(200);

        hashTable.remove(100);
        assertFalse(hashTable.isEmpty(), "A tabela não deve relatar estado vazio enquanto houver elementos.");
        assertEquals(1, hashTable.getSize(), "O tamanho deve ser exatamente 1.");

        hashTable.remove(200);
        assertTrue(hashTable.isEmpty(), "A tabela deve relatar estado vazio assim que o último elemento for removido.");
        assertEquals(0, hashTable.getSize(), "O tamanho deve zerar perfeitamente.");
    }
}
