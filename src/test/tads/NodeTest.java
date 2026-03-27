package test.tads;

import main.tads.Node;
import main.tads.bst.avl.AVL;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Representa a suíte de testes unitários para a classe abstrata Node.
 * <p>
 * Como Node é abstrata, os testes utilizam uma implementação concreta mínima
 * apenas para viabilizar a instanciação nos cenários de validação.
 * </p>
 *
 * @see Node
 * @author Lucas
 * @version 1.0
 * @since 2026-03-27
 */
public class NodeTest {

    /**
     * Implementação concreta simples para testes de Node.
     */
    private static class TestNode<T> extends Node<T> {
        /**
         * Cria um Nó de teste com o dado informado.
         *
         * @param data dado inicial do Nó.
         */
        public TestNode(T data) {
            super(data);
        }
    }

    /**
     * Verifica se o construtor preserva o dado inicial.
     */
    @Test
    public void testConstructorPreservesInitialData() {
        TestNode<String> node = new TestNode<>("kernel");

        assertEquals("kernel", node.getData(), "O construtor deve preservar o dado inicial informado.");
        assertFalse(node.isNil(), "Um Nó com dado não nulo não pode ser sentinela.");
    }

    /**
     * Verifica se setData substitui corretamente o valor armazenado.
     */
    @Test
    public void testSetDataUpdatesStoredValue() {
        TestNode<Integer> node = new TestNode<>(10);

        node.setData(42);

        assertEquals(42, node.getData(), "O método setData deve atualizar o valor armazenado.");
    }

    /**
     * Verifica se um dado inicial nulo caracteriza o Nó como sentinela.
     */
    @Test
    public void testIsNilReturnsTrueWhenDataStartsNull() {
        TestNode<Object> node = new TestNode<>(null);

        assertTrue(node.isNil(), "Um Nó criado com dado nulo deve ser considerado sentinela.");
        assertNull(node.getData(), "O dado deve permanecer nulo após a construção.");
    }

    /**
     * Verifica as transições de isNil ao alternar entre dado nulo e não nulo.
     */
    @Test
    public void testIsNilTransitionsWhenDataChanges() {
        TestNode<String> node = new TestNode<>("process");

        assertFalse(node.isNil(), "Antes da atualização para nulo, o Nó não deve ser sentinela.");

        node.setData(null);
        assertTrue(node.isNil(), "Após definir dado nulo, o Nó deve ser sentinela.");

        node.setData("ready");
        assertFalse(node.isNil(), "Após definir dado não nulo, o Nó deve deixar de ser sentinela.");
    }
}
