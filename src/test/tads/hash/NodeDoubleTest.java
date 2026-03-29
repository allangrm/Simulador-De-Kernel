package test.tads.hash;

import main.tads.hash.NodeDouble;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Representa a suíte de testes unitários para a classe NodeDouble.
 * <p>
 * Os testes validam a instanciação isolada e encadeada do Nó,
 * estados de sentinela (herdados da classe Node) e o correto
 * funcionamento das referências bidirecionais utilizadas em
 * listas duplamente encadeadas.
 * </p>
 *
 * @see NodeDouble
 * @author Allan Guilherme
 * @version 1.0
 * @since 2026-03-27
 */
public class NodeDoubleTest {

    /**
     * Verifica se o construtor isolado inicializa os ponteiros como nulos e preserva o dado.
     */
    @Test
    public void testIsolatedConstructorInitializesPointersToNull() {
        NodeDouble<String> node = new NodeDouble<>("nodeA");

        assertEquals("nodeA", node.getData(), "O construtor deve preservar o dado inicial informado.");
        assertNull(node.getNext(), "A referência para o próximo Nó deve ser inicializada como nula.");
        assertNull(node.getPrevious(), "A referência para o Nó anterior deve ser inicializada como nula.");
        assertFalse(node.isNil(), "Um Nó instanciado com dado válido não deve ser sentinela.");
    }

    /**
     * Verifica se o construtor encadeado atribui corretamente as referências dos nós vizinhos.
     */
    @Test
    public void testLinkedConstructorAssignsNeighborsCorrectly() {
        NodeDouble<Integer> nextNode = new NodeDouble<>(30);
        NodeDouble<Integer> prevNode = new NodeDouble<>(10);

        NodeDouble<Integer> node = new NodeDouble<>(20, nextNode, prevNode);

        assertEquals(nextNode, node.getNext(), "A referência 'next' deve apontar para o Nó sucessor fornecido.");
        assertEquals(prevNode, node.getPrevious(), "A referência 'previous' deve apontar para o Nó antecessor fornecido.");
    }

    /**
     * Verifica se a inicialização com dado nulo caracteriza o Nó como sentinela.
     */
    @Test
    public void testConstructorWithNullDataCreatesSentinelNode() {
        NodeDouble<String> node = new NodeDouble<>(null);

        assertTrue(node.isNil(), "Um Nó instanciado com dado nulo deve ser identificado como sentinela.");
        assertNull(node.getData(), "O dado armazenado deve permanecer nulo.");
    }

    /**
     * Verifica se o metodo setData atualiza o valor e altera o estado do sentinela.
     */
    @Test
    public void testSetDataUpdatesValueAndSentinelState() {
        NodeDouble<Integer> node = new NodeDouble<>(null);
        assertTrue(node.isNil(), "O Nó deve iniciar como sentinela.");

        node.setData(42);

        assertEquals(42, node.getData(), "O método setData deve atualizar o valor armazenado.");
        assertFalse(node.isNil(), "Após a inserção de um dado válido, o Nó não deve mais ser sentinela.");
    }

    /**
     * Verifica a reatribuição de referências para nulo (desvinculação proposital).
     */
    @Test
    public void testSettersAllowNullForUnlinking() {
        NodeDouble<Integer> nextNode = new NodeDouble<>(20);
        NodeDouble<Integer> node = new NodeDouble<>(10, nextNode, null);

        assertNotNull(node.getNext(), "A referência 'next' deve estar preenchida inicialmente.");

        node.setNext(null);

        assertNull(node.getNext(), "O método setNext deve permitir atribuição nula para desvinculação (unlinking).");
    }

    /**
     * Verifica o comportamento de uma referência cíclica onde o Nó aponta para si mesmo.
     */
    @Test
    public void testCircularReferencePointsToItself() {
        NodeDouble<String> node = new NodeDouble<>("singleNode");

        node.setNext(node);
        node.setPrevious(node);

        assertEquals(node, node.getNext(), "Em um ciclo de elemento único, a referência 'next' deve apontar para o próprio Nó.");
        assertEquals(node, node.getPrevious(), "Em um ciclo de elemento único, a referência 'previous' deve apontar para o próprio Nó.");
    }
}