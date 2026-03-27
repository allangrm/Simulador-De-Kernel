package test.tads.bst.avl;

import main.tads.bst.NodeTree;
import main.tads.bst.avl.AVL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Representa a suíte de testes unitários para a Árvore AVL.
 * <p>
 * Os cenários validam operações de inserção, remoção, busca,
 * percursos e preservação das invariantes AVL/BST.
 * </p>
 *
 * @see AVL
 * @author Lucas
 * @version 1.0
 * @since 2026-03-27
 */
public class AVLTest {
    private AVL<Integer> avl;

    /**
     * Recria uma AVL vazia antes de cada cenário.
     */
    @BeforeEach
    public void setUp() {
        avl = new AVL<>();
    }

    /**
     * Verifica o estado inicial de uma árvore recém-instanciada.
     */
    @Test
    public void testTreeStartsEmpty() {
        assertTrue(avl.isEmpty(), "A árvore recém-criada deve iniciar vazia.");
        assertEquals(0, avl.size(), "O tamanho inicial deve ser 0.");
        assertEquals(-1, avl.height(), "A altura de uma árvore vazia deve ser -1.");
        assertNull(avl.minimum(), "A menor chave deve ser nula em árvore vazia.");
        assertNull(avl.maximum(), "A maior chave deve ser nula em árvore vazia.");
        assertEquals(0, avl.inOrder().length, "Percurso em ordem deve estar vazio.");
        assertEquals(0, avl.preOrder().length, "Percurso em pré-ordem deve estar vazio.");
        assertEquals(0, avl.postOrder().length, "Percurso em pós-ordem deve estar vazio.");
    }

    /**
     * Verifica inserção única e acesso aos extremos.
     */
    @Test
    public void testInsertSingleElement() {
        avl.insert(new NodeTree<>(42));

        assertFalse(avl.isEmpty(), "A árvore não deve permanecer vazia após inserir um elemento.");
        assertEquals(1, avl.size(), "O tamanho deve ser 1 após uma inserção.");
        assertEquals(0, avl.height(), "A altura de uma árvore com um único Nó deve ser 0.");
        assertEquals(42, avl.minimum().getData(), "O mínimo deve ser o único valor inserido.");
        assertEquals(42, avl.maximum().getData(), "O máximo deve ser o único valor inserido.");

        NodeTree<Integer> found = avl.search(new NodeTree<>(42));
        assertFalse(found.isNil(), "A busca deve encontrar o valor inserido.");
        assertEquals(42, found.getData(), "A chave encontrada deve ser igual à inserida.");
    }

    /**
     * Verifica rejeição de chave duplicada na inserção.
     */
    @Test
    public void testInsertDuplicateKeyThrowsException() {
        avl.insert(new NodeTree<>(10));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> avl.insert(new NodeTree<>(10))
        );

        assertEquals("Chave duplicada nao permitida na AVL.", exception.getMessage());
        assertEquals(1, avl.size(), "A duplicidade não deve alterar o tamanho da árvore.");
    }

    /**
     * Verifica exceção ao inserir referência nula.
     */
    @Test
    public void testInsertNullNodeThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> avl.insert(null)
        );

        assertEquals("O Nó inserido deve possuir uma chave valida.", exception.getMessage());
    }

    /**
     * Verifica exceção ao inserir nó sentinela.
     */
    @Test
    public void testInsertSentinelNodeThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> avl.insert(new NodeTree<>(null))
        );

        assertEquals("O Nó inserido deve possuir uma chave valida.", exception.getMessage());
    }

    /**
     * Verifica exceção ao buscar com nó inválido (chave nula).
     */
    @Test
    public void testSearchWithInvalidNodeThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> avl.search(new NodeTree<>(null))
        );

        assertEquals("O Nó de busca deve possuir uma chave valida.", exception.getMessage());
    }

    /**
     * Verifica exceção ao buscar com referência nula.
     */
    @Test
    public void testSearchNullNodeThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> avl.search(null)
        );

        assertEquals("O Nó de busca deve possuir uma chave valida.", exception.getMessage());
    }

    /**
     * Verifica retorno do sentinela ao buscar chave ausente.
     */
    @Test
    public void testSearchReturnsSentinelWhenKeyDoesNotExist() {
        avl.insert(new NodeTree<>(7));
        avl.insert(new NodeTree<>(3));
        avl.insert(new NodeTree<>(11));

        NodeTree<Integer> missing = avl.search(new NodeTree<>(99));

        assertTrue(missing.isNil(), "A busca por chave ausente deve retornar Nó sentinela.");
    }

    /**
     * Verifica manutenção da ordenação BST após múltiplas inserções.
     */
    @Test
    public void testInOrderRemainsSortedAfterSeveralInsertions() {
        int[] values = {30, 20, 10, 25, 40, 50, 22, 5, 35};
        for (int value : values) {
            avl.insert(new NodeTree<>(value));
        }

        Integer[] inOrder = extractValues(avl.inOrder());
        Integer[] expected = {5, 10, 20, 22, 25, 30, 35, 40, 50};

        assertArrayEquals(expected, inOrder, "O percurso em ordem deve permanecer ordenado.");
        assertAvlAndBstInvariants(getInternalRoot());
    }

    /**
     * Verifica percursos pré-ordem e pós-ordem em árvore não vazia.
     */
    @Test
    public void testPreOrderAndPostOrderOnNonEmptyTree() {
        int[] values = {20, 10, 30, 5, 15, 25, 35};
        for (int value : values) {
            avl.insert(new NodeTree<>(value));
        }

        assertArrayEquals(
                new Integer[]{20, 10, 5, 15, 30, 25, 35},
                extractValues(avl.preOrder()),
                "O percurso em pré-ordem não corresponde ao esperado para a estrutura balanceada."
        );

        assertArrayEquals(
                new Integer[]{5, 15, 10, 25, 35, 30, 20},
                extractValues(avl.postOrder()),
                "O percurso em pós-ordem não corresponde ao esperado para a estrutura balanceada."
        );
    }

    /**
     * Verifica a rotação LL no rebalanceamento.
     */
    @Test
    public void testRotationLLUpdatesRootAsExpected() {
        avl.insert(new NodeTree<>(30));
        avl.insert(new NodeTree<>(20));
        avl.insert(new NodeTree<>(10));

        NodeTree<Integer> root = getInternalRoot();
        assertEquals(20, root.getData(), "A rotação LL deve promover a chave 20 para a raiz.");
        assertEquals(10, root.getLeft().getData(), "O filho esquerdo da nova raiz deve ser 10.");
        assertEquals(30, root.getRight().getData(), "O filho direito da nova raiz deve ser 30.");
        assertAvlAndBstInvariants(root);
    }

    /**
     * Verifica a rotação RR no rebalanceamento.
     */
    @Test
    public void testRotationRRUpdatesRootAsExpected() {
        avl.insert(new NodeTree<>(10));
        avl.insert(new NodeTree<>(20));
        avl.insert(new NodeTree<>(30));

        NodeTree<Integer> root = getInternalRoot();
        assertEquals(20, root.getData(), "A rotação RR deve promover a chave 20 para a raiz.");
        assertEquals(10, root.getLeft().getData(), "O filho esquerdo da nova raiz deve ser 10.");
        assertEquals(30, root.getRight().getData(), "O filho direito da nova raiz deve ser 30.");
        assertAvlAndBstInvariants(root);
    }

    /**
     * Verifica a rotação LR no rebalanceamento.
     */
    @Test
    public void testRotationLRUpdatesRootAsExpected() {
        avl.insert(new NodeTree<>(30));
        avl.insert(new NodeTree<>(10));
        avl.insert(new NodeTree<>(20));

        NodeTree<Integer> root = getInternalRoot();
        assertEquals(20, root.getData(), "A rotação LR deve promover a chave 20 para a raiz.");
        assertEquals(10, root.getLeft().getData(), "O filho esquerdo da nova raiz deve ser 10.");
        assertEquals(30, root.getRight().getData(), "O filho direito da nova raiz deve ser 30.");
        assertAvlAndBstInvariants(root);
    }

    /**
     * Verifica a rotação RL no rebalanceamento.
     */
    @Test
    public void testRotationRLUpdatesRootAsExpected() {
        avl.insert(new NodeTree<>(10));
        avl.insert(new NodeTree<>(30));
        avl.insert(new NodeTree<>(20));

        NodeTree<Integer> root = getInternalRoot();
        assertEquals(20, root.getData(), "A rotação RL deve promover a chave 20 para a raiz.");
        assertEquals(10, root.getLeft().getData(), "O filho esquerdo da nova raiz deve ser 10.");
        assertEquals(30, root.getRight().getData(), "O filho direito da nova raiz deve ser 30.");
        assertAvlAndBstInvariants(root);
    }

    /**
     * Verifica cálculo de sucessor e predecessor em casos comuns e bordas.
     */
    @Test
    public void testSuccessorAndPredecessor() {
        int[] values = {20, 10, 30, 5, 15, 25, 35};
        for (int value : values) {
            avl.insert(new NodeTree<>(value));
        }

        assertEquals(25, avl.successor(new NodeTree<>(20)).getData(), "Sucessor de 20 deve ser 25.");
        assertEquals(15, avl.predecessor(new NodeTree<>(20)).getData(), "Predecessor de 20 deve ser 15.");
        assertNull(avl.successor(new NodeTree<>(35)), "Maior elemento não deve ter sucessor.");
        assertNull(avl.predecessor(new NodeTree<>(5)), "Menor elemento não deve ter predecessor.");
    }

    /**
     * Verifica remoção de nó folha.
     */
    @Test
    public void testRemoveLeafNode() {
        int[] values = {20, 10, 30, 5, 15};
        for (int value : values) {
            avl.insert(new NodeTree<>(value));
        }

        avl.remove(new NodeTree<>(5));

        assertEquals(4, avl.size(), "A remoção de folha deve diminuir o tamanho em 1.");
        assertTrue(avl.search(new NodeTree<>(5)).isNil(), "A chave removida não deve mais existir.");
        assertAvlAndBstInvariants(getInternalRoot());
    }

    /**
     * Verifica remoção de nó com um único filho.
     */
    @Test
    public void testRemoveNodeWithOneChild() {
        int[] values = {20, 10, 30, 25};
        for (int value : values) {
            avl.insert(new NodeTree<>(value));
        }

        avl.remove(new NodeTree<>(30));

        assertEquals(3, avl.size(), "A remoção deve reduzir corretamente o tamanho da árvore.");
        assertTrue(avl.search(new NodeTree<>(30)).isNil(), "A chave removida não deve permanecer na estrutura.");
        assertArrayEquals(new Integer[]{10, 20, 25}, extractValues(avl.inOrder()));
        assertAvlAndBstInvariants(getInternalRoot());
    }

    /**
     * Verifica remoção de nó com dois filhos.
     */
    @Test
    public void testRemoveNodeWithTwoChildren() {
        int[] values = {20, 10, 30, 5, 15, 25, 35};
        for (int value : values) {
            avl.insert(new NodeTree<>(value));
        }

        avl.remove(new NodeTree<>(20));

        assertEquals(6, avl.size(), "A remoção com dois filhos deve reduzir o tamanho em 1.");
        assertTrue(avl.search(new NodeTree<>(20)).isNil(), "A chave removida não deve mais ser encontrada.");
        assertArrayEquals(new Integer[]{5, 10, 15, 25, 30, 35}, extractValues(avl.inOrder()));
        assertAvlAndBstInvariants(getInternalRoot());
    }

    /**
     * Verifica remoções sucessivas da raiz até esvaziar a árvore.
     */
    @Test
    public void testRemoveRootUntilTreeBecomesEmpty() {
        int[] values = {40, 20, 60, 10, 30, 50, 70};
        for (int value : values) {
            avl.insert(new NodeTree<>(value));
        }

        List<Integer> removalOrder = Arrays.asList(40, 50, 30, 10, 20, 60, 70);
        for (Integer key : removalOrder) {
            avl.remove(new NodeTree<>(key));
            if (!avl.isEmpty()) {
                assertAvlAndBstInvariants(getInternalRoot());
            }
        }

        assertTrue(avl.isEmpty(), "A árvore deve ficar vazia após remover todos os elementos.");
        assertEquals(0, avl.size(), "O tamanho final deve ser 0.");
        assertEquals(-1, avl.height(), "A altura final deve ser -1.");
    }

    /**
     * Verifica exceção ao remover de árvore vazia.
     */
    @Test
    public void testRemoveOnEmptyTreeThrowsException() {
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> avl.remove(new NodeTree<>(1))
        );

        assertEquals("Nao e possivel remover de uma arvore vazia.", exception.getMessage());
    }

    /**
     * Verifica exceção ao remover referência nula.
     */
    @Test
    public void testRemoveNullNodeThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> avl.remove(null)
        );

        assertEquals("O Nó removido deve possuir uma chave valida.", exception.getMessage());
    }

    /**
     * Verifica exceção ao remover nó com chave nula.
     */
    @Test
    public void testRemoveNodeWithNullKeyThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> avl.remove(new NodeTree<>(null))
        );

        assertEquals("O Nó removido deve possuir uma chave valida.", exception.getMessage());
    }

    /**
     * Verifica exceção ao remover chave inexistente.
     */
    @Test
    public void testRemoveNonExistingKeyThrowsException() {
        avl.insert(new NodeTree<>(8));
        avl.insert(new NodeTree<>(4));
        avl.insert(new NodeTree<>(12));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> avl.remove(new NodeTree<>(99))
        );

        assertEquals("Chave inexistente na AVL.", exception.getMessage());
        assertEquals(3, avl.size(), "A tentativa de remoção inválida não deve alterar o tamanho.");
    }

    /**
     * Verifica proteção contra mutação externa no nó raiz retornado.
     */
    @Test
    public void testGetRootReturnsDefensiveCopy() {
        avl.insert(new NodeTree<>(10));
        avl.insert(new NodeTree<>(5));
        avl.insert(new NodeTree<>(15));

        NodeTree<Integer> externalRoot = avl.getRoot();
        Integer originalRootData = avl.getRoot().getData();

        externalRoot.setData(999);

        assertEquals(originalRootData, avl.getRoot().getData(),
                "Alterações no Nó retornado por getRoot não devem afetar a árvore interna.");
    }

    /**
     * Verifica que a cópia defensiva de getRoot é profunda.
     */
    @Test
    public void testGetRootReturnsDeepDefensiveCopy() {
        avl.insert(new NodeTree<>(10));
        avl.insert(new NodeTree<>(5));
        avl.insert(new NodeTree<>(15));

        NodeTree<Integer> externalRoot = avl.getRoot();
        externalRoot.getLeft().setData(999);

        NodeTree<Integer> stillExisting = avl.search(new NodeTree<>(5));
        assertFalse(stillExisting.isNil(), "A alteração externa não pode remover a chave interna original.");
        assertEquals(5, stillExisting.getData(), "A chave interna deve permanecer inalterada após mutação da cópia.");
    }

    /**
     * Converte vetor de nós em vetor de chaves para facilitar asserções.
     *
     * @param nodes vetor de nós retornado pelos percursos.
     * @return vetor contendo apenas as chaves dos nós.
     */
    private Integer[] extractValues(NodeTree<Integer>[] nodes) {
        List<Integer> values = new ArrayList<>();
        for (NodeTree<Integer> node : nodes) {
            values.add(node.getData());
        }
        return values.toArray(new Integer[0]);
    }

    /**
     * Dispara validação recursiva das invariantes AVL e BST.
     *
     * @param root raiz da estrutura a ser validada.
     */
    private void assertAvlAndBstInvariants(NodeTree<Integer> root) {
        validateSubtree(root, null, null);
    }

    /**
     * Acessa a raiz interna da AVL para validar a estrutura real do objeto.
     *
     * @return referência da raiz interna da instância em teste.
     */
    @SuppressWarnings("unchecked")
    private NodeTree<Integer> getInternalRoot() {
        try {
            Field rootField = AVL.class.getDeclaredField("root");
            rootField.setAccessible(true);
            return (NodeTree<Integer>) rootField.get(avl);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new AssertionError("Falha ao acessar a raiz interna da AVL para validacao estrutural.", e);
        }
    }

    /**
     * Valida recursivamente ordenação BST, balanceamento AVL e altura armazenada.
     *
     * @param node nó atual da recursão.
     * @param minExclusive limite inferior exclusivo para propriedade BST.
     * @param maxExclusive limite superior exclusivo para propriedade BST.
     * @return altura estrutural calculada para o nó atual.
     */
    private int validateSubtree(NodeTree<Integer> node, Integer minExclusive, Integer maxExclusive) {
        if (node == null || node.isNil()) {
            return -1;
        }

        Integer data = node.getData();
        if (minExclusive != null) {
            assertTrue(data > minExclusive, "A propriedade BST foi violada no limite inferior.");
        }
        if (maxExclusive != null) {
            assertTrue(data < maxExclusive, "A propriedade BST foi violada no limite superior.");
        }

        int leftHeight = validateSubtree(node.getLeft(), minExclusive, data);
        int rightHeight = validateSubtree(node.getRight(), data, maxExclusive);

        assertTrue(Math.abs(leftHeight - rightHeight) <= 1,
                "A propriedade de balanceamento AVL foi violada.");

        int expectedHeight = 1 + Math.max(leftHeight, rightHeight);
        assertEquals(expectedHeight, node.height(),
                "A altura armazenada no Nó não corresponde à altura estrutural calculada.");

        return expectedHeight;
    }
}
