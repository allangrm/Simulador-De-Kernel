package test.tads.linked_list;

import main.tads.linked_list.NodeList;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Representa a suíte de testes unitários para a classe NodeList.
 * <p>
 * Os testes validam a instanciação isolada e encadeada do Nó,
 * bem como o correto funcionamento da referência unidirecional
 * (próximo) utilizada em listas simplesmente encadeadas.
 * </p>
 *
 * @see NodeList
 * @author Allan
 * @version 1.0
 * @since 2026-03-27
 */
public class NodeListTest {

    /**
     * Verifica se o construtor isolado inicializa o ponteiro como nulo e preserva o dado.
     */
    @Test
    public void testIsolatedConstructorInitializesPointerToNull() {
        NodeList<String> node = new NodeList<>("nodeA");

        assertEquals("nodeA", node.getData(), "O construtor deve preservar o dado inicial informado.");
        assertNull(node.getNext(), "A referência para o próximo Nó deve ser inicializada como nula.");
    }

    /**
     * Verifica se o construtor encadeado atribui corretamente a referência do nó sucessor.
     */
    @Test
    public void testLinkedConstructorAssignsNeighborCorrectly() {
        NodeList<String> nextNode = new NodeList<>("nodeB");
        NodeList<String> node = new NodeList<>("nodeA", nextNode);

        assertEquals("nodeA", node.getData(), "O dado armazenado deve ser preservado.");
        assertEquals(nextNode, node.getNext(), "A referência 'next' deve apontar para o Nó sucessor fornecido.");
    }

    /**
     * Verifica se o metodo setNext atualiza corretamente a referência.
     */
    @Test
    public void testSetNextUpdatesPointerCorrectly() {
        NodeList<Integer> node1 = new NodeList<>(10);
        NodeList<Integer> node2 = new NodeList<>(20);

        node1.setNext(node2);

        assertEquals(node2, node1.getNext(), "O método setNext deve atualizar a referência para o próximo Nó.");
    }

    /**
     * Verifica o comportamento de uma referência onde o Nó aponta para si mesmo.
     */
    @Test
    public void testCircularReferencePointsToItself() {
        NodeList<String> node = new NodeList<>("singleNode");

        node.setNext(node);

        assertEquals(node, node.getNext(), "Em um ciclo de elemento único, a referência 'next' deve apontar para o próprio Nó.");
    }
}