package test.tads.bst;

import main.tads.bst.NodeTree;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Representa a suíte de testes unitários para a estrutura de Nó de árvore.
 * <p>
 * Os cenários validam inicialização, ligações entre pai e filhos,
 * cálculo de altura e validações de entrada inválida.
 * </p>
 *
 * @see NodeTree
 * @author Lucas
 * @version 1.0
 * @since 2026-03-27
 */
public class NodeTreeTest {

    /**
     * Verifica a inicialização básica do construtor com dado.
     */
    @Test
    public void testConstructorWithDataInitializesLeafNode() {
        NodeTree<Integer> node = new NodeTree<>(10);

        assertEquals(10, node.getData(), "O dado inicial deve ser preservado.");
        assertFalse(node.isNil(), "Um Nó com dado não pode ser sentinela.");
        assertTrue(node.getLeft() == null || node.getLeft().isNil(),
                "O filho esquerdo deve iniciar como nulo ou sentinela.");
        assertTrue(node.getRight() == null || node.getRight().isNil(),
                "O filho direito deve iniciar como nulo ou sentinela.");
        assertNull(node.getParent(), "O pai deve iniciar nulo.");
        assertEquals(0, node.height(), "Um Nó folha deve iniciar com altura 0.");
    }

    /**
     * Verifica se dado nulo cria um Nó sentinela.
     */
    @Test
    public void testConstructorWithNullDataCreatesSentinelNode() {
        NodeTree<Integer> nil = new NodeTree<>(null);

        assertTrue(nil.isNil(), "Um Nó com dado nulo deve ser sentinela.");
        assertEquals(-1, nil.height(), "A altura do sentinela deve ser -1.");
    }

    /**
     * Verifica ligação correta entre pai e filhos no construtor com filhos.
     */
    @Test
    public void testConstructorWithChildrenSetsParentAndHeight() {
        NodeTree<Integer> left = new NodeTree<>(5);
        NodeTree<Integer> right = new NodeTree<>(15);
        NodeTree<Integer> parent = new NodeTree<>(10, right, left);

        assertSame(left, parent.getLeft(), "O filho esquerdo deve ser ligado corretamente.");
        assertSame(right, parent.getRight(), "O filho direito deve ser ligado corretamente.");
        assertSame(parent, left.getParent(), "O pai do filho esquerdo deve ser atualizado.");
        assertSame(parent, right.getParent(), "O pai do filho direito deve ser atualizado.");
        assertEquals(1, parent.height(), "A altura deve refletir o maior caminho até as folhas.");
    }

    /**
     * Verifica a normalização de filhos nulos para sentinelas.
     */
    @Test
    public void testConstructorWithNullChildrenNormalizesToSentinels() {
        NodeTree<Integer> node = new NodeTree<>(10, null, null);

        assertNotNull(node.getLeft(), "O filho esquerdo não deve permanecer nulo.");
        assertNotNull(node.getRight(), "O filho direito não deve permanecer nulo.");
        assertTrue(node.getLeft().isNil(), "Filho esquerdo nulo deve ser normalizado para sentinela.");
        assertTrue(node.getRight().isNil(), "Filho direito nulo deve ser normalizado para sentinela.");
        assertEquals(0, node.height(), "Com sentinelas nos filhos, a altura deve ser 0.");
    }

    /**
     * Verifica se o construtor com pai preserva a referência informada.
     */
    @Test
    public void testConstructorWithParentSetsParentReference() {
        NodeTree<Integer> expectedParent = new NodeTree<>(20);
        NodeTree<Integer> left = new NodeTree<>(5);
        NodeTree<Integer> right = new NodeTree<>(15);

        NodeTree<Integer> node = new NodeTree<>(10, right, left, expectedParent);

        assertSame(expectedParent, node.getParent(), "O pai informado no construtor deve ser preservado.");
        assertSame(node, left.getParent(), "Filho esquerdo deve apontar para o novo pai.");
        assertSame(node, right.getParent(), "Filho direito deve apontar para o novo pai.");
    }

    /**
     * Verifica se setLeft e setRight rejeitam referência nula.
     */
    @Test
    public void testSetLeftAndSetRightThrowWhenNull() {
        NodeTree<Integer> node = new NodeTree<>(10);

        IllegalArgumentException leftException =
                assertThrows(IllegalArgumentException.class, () -> node.setLeft(null));
        assertEquals("O filho esquerdo de um Nó nao pode ser nulo.", leftException.getMessage());

        IllegalArgumentException rightException =
                assertThrows(IllegalArgumentException.class, () -> node.setRight(null));
        assertEquals("O filho direito de um Nó nao pode ser nulo.", rightException.getMessage());
    }

    /**
     * Verifica atualização de parent e altura após troca de filho.
     */
    @Test
    public void testSetChildrenUpdatesParentAndHeight() {
        NodeTree<Integer> node = new NodeTree<>(10, new NodeTree<>(null), new NodeTree<>(null));
        NodeTree<Integer> child = new NodeTree<>(5, new NodeTree<>(null), new NodeTree<>(null));

        node.setLeft(child);

        assertSame(child, node.getLeft(), "O filho esquerdo deve ser atualizado.");
        assertSame(node, child.getParent(), "O filho deve receber referência para o pai.");
        assertEquals(1, node.height(), "A altura deve ser recalculada após atualizar o filho.");
    }

    /**
     * Verifica se o sentinela mantém seu próprio pai ao ser conectado como filho.
     */
    @Test
    public void testSetSentinelChildDoesNotOverrideSentinelParent() {
        NodeTree<Integer> node = new NodeTree<>(10, new NodeTree<>(null), new NodeTree<>(null));
        NodeTree<Integer> sentinel = new NodeTree<>(null);
        sentinel.setParent(sentinel);

        node.setLeft(sentinel);

        assertSame(sentinel, node.getLeft(), "Atribuir o sentinela como filho deve manter a referência recebida.");
        assertSame(sentinel, sentinel.getParent(), "O pai do sentinela não deve ser sobrescrito ao ligar na árvore.");
    }

    /**
     * Verifica recálculo de altura após alterações estruturais em cadeia.
     */
    @Test
    public void testUpdateHeightRecalculatesAfterStructuralChanges() {
        NodeTree<Integer> root = new NodeTree<>(10, new NodeTree<>(null), new NodeTree<>(null));
        NodeTree<Integer> child = new NodeTree<>(5, new NodeTree<>(null), new NodeTree<>(null));
        NodeTree<Integer> grandChild = new NodeTree<>(2, new NodeTree<>(null), new NodeTree<>(null));

        root.setLeft(child);
        assertEquals(1, root.height(), "A altura da raiz deve ser 1 com um nível de profundidade.");

        child.setLeft(grandChild);
        child.updateHeight();
        root.updateHeight();

        assertEquals(1, child.height(), "A altura do filho deve refletir o neto inserido.");
        assertEquals(2, root.height(), "A altura da raiz deve crescer com o novo nível na subárvore.");
    }

    /**
     * Verifica se o sentinela preserva altura -1 após updateHeight.
     */
    @Test
    public void testUpdateHeightOnSentinelKeepsNegativeOne() {
        NodeTree<Integer> nil = new NodeTree<>(null);

        nil.updateHeight();

        assertEquals(-1, nil.height(), "O Nó sentinela deve manter altura -1 após recálculo.");
    }
}
