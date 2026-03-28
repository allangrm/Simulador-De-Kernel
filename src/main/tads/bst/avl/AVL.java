package main.tads.bst.avl;

import java.util.ArrayList;
import java.util.List;

import main.tads.bst.BinarySearchTree;
import main.tads.bst.NodeTree;

/**
 * Implementa uma Árvore AVL com uso de Nó sentinela para representar folhas externas.
 * <p>
 * A estrutura mantém a propriedade de busca binária e reequilibra a árvore por meio
 * de rotações durante operações de inserção e remoção.
 * </p>
 *
 * @param <T> tipo de chave armazenada nos Nós.
 * @see BinarySearchTree
 * @see NodeTree
 * @author Lucas Nóbrega
 * @version 1.0
 * @since 2026-03-27
 */
public class AVL<T extends Comparable<T>> implements BinarySearchTree<T> {
    private NodeTree<T> root;
    private final NodeTree<T> nil;

    /**
     * Cria uma árvore AVL vazia com um Nó sentinela compartilhado.
     */
    public AVL() {
        this.nil = new NodeTree<>(null);
        this.nil.setParent(this.nil);
        this.root = this.nil;
    }

    /**
     * Retorna uma cópia defensiva da raiz da árvore.
     * <p>
     * A cópia evita alterações externas diretas na estrutura interna da AVL.
     * </p>
     *
     * @return raiz copiada ou um Nó sentinela quando a árvore estiver vazia.
     */
    @Override
    public NodeTree<T> getRoot() {
        if (isEmpty()) {
            return new NodeTree<>(null);
        }

        NodeTree<T> copyNil = new NodeTree<>(null);
        copyNil.setParent(copyNil);
        return copySubtree(this.root, copyNil, copyNil);
    }

    /**
     * Informa se a árvore não possui elementos válidos.
     *
     * @return {@code true} quando a raiz é sentinela; caso contrário, {@code false}.
     */
    @Override
    public boolean isEmpty() {
        return root == null || root.isNil();
    }

    /**
     * Retorna a altura da árvore em tempo constante.
     *
     * @return altura da raiz ou {@code -1} quando a árvore estiver vazia.
     */
    @Override
    public int height() {
        return isEmpty() ? -1 : root.height();
    }

    /**
     * Busca um Nó pela chave informada.
     * <p>
     * Contrato de uso:
     * </p>
     * <ul>
     *   <li>Se o parâmetro for inválido (Nó nulo ou chave nula), lança {@link IllegalArgumentException}.</li>
     *   <li>Se a chave não existir na árvore, retorna o Nó sentinela {@code nil}.</li>
     *   <li>Se a chave existir, retorna a referência do Nó correspondente.</li>
     * </ul>
     *
     * @param k Nó contendo a chave a ser buscada.
     * @return Nó encontrado ou o sentinela {@code nil} quando a chave estiver ausente.
     * @throws IllegalArgumentException quando o Nó de busca for inválido.
     */
    @Override
    public NodeTree<T> search(NodeTree<T> k) {
        if (k == null || k.getData() == null) {
            throw new IllegalArgumentException("O Nó de busca deve possuir uma chave valida.");
        }

        NodeTree<T> aux = this.root;
        while (!aux.isNil() && k.getData().compareTo(aux.getData()) != 0) {
            if (k.getData().compareTo(aux.getData()) < 0) {
                aux = aux.getLeft();
            } else {
                aux = aux.getRight();
            }
        }
        return aux;
    }

    /**
     * Reestrutura localmente a subárvore desbalanceada formada por z, y e x.
     * <p>
     * O método trata os quatro casos canônicos de rotação AVL (LL, LR, RL, RR)
     * e reconecta o novo topo da subárvore ao ancestral de z.
     * </p>
     *
     * @param x neto crítico identificado durante o rebalanceamento.
     * @throws IllegalStateException quando os vínculos pai-filho estiverem inconsistentes.
     */
    private void restructure(NodeTree<T> x) {
        if (x == null || x.isNil()) {
            return;
        }

        NodeTree<T> y = x.getParent();
        if (y == null || y.isNil()) {
            return;
        }

        NodeTree<T> z = y.getParent();
        if (z == null || z.isNil()) {
            return;
        }

        if ((y.getLeft() != x && y.getRight() != x) || (z.getLeft() != y && z.getRight() != y)) {
            throw new IllegalStateException("Estrutura AVL inconsistente durante a reestruturacao.");
        }

        NodeTree<T> zParent = z.getParent();

        NodeTree<T> a, b, c, t0, t1, t2, t3;
        boolean xIsLeftChild = y.getLeft() == x;
        boolean yIsLeftChild = z.getLeft() == y;
        
        if (xIsLeftChild && yIsLeftChild) {
            a = x;
            b = y;
            c = z;
            t0 = a.getLeft();
            t1 = a.getRight();
            t2 = b.getRight();
            t3 = c.getRight();
        } else if (!xIsLeftChild && yIsLeftChild) {
            a = y;
            b = x;
            c = z;
            t0 = a.getLeft();
            t1 = b.getLeft();
            t2 = b.getRight();
            t3 = c.getRight();
        } else if (xIsLeftChild) {
            a = z;
            b = x;
            c = y;
            t0 = a.getLeft();
            t1 = b.getLeft();
            t2 = b.getRight();
            t3 = c.getRight();
        } else {
            a = z;
            b = y;
            c = x;
            t0 = a.getLeft();
            t1 = b.getLeft();
            t2 = c.getLeft();
            t3 = c.getRight();
        }
        
        if (zParent == null || zParent.isNil()) {
            this.root = b;
            b.setParent(this.nil);
        } else if (zParent.getLeft() == z) {
            zParent.setLeft(b);
        } else {
            zParent.setRight(b);
        }
        if (zParent != null && !zParent.isNil()) {
            b.setParent(zParent);
        }
        
        b.setLeft(a);
        b.setRight(c);
        
        a.setLeft(t0);
        a.setRight(t1);
        c.setLeft(t2);
        c.setRight(t3);
        
        a.updateHeight();
        c.updateHeight();
        b.updateHeight();
    }

    /**
     * Calcula o fator de balanceamento de um Nó.
     *
     * @param n Nó a ser avaliado.
     * @return altura da subárvore esquerda menos altura da subárvore direita.
     */
    private int getBalanceFactor(NodeTree<T> n) {
        if (n == null || n.isNil()) {
            return 0;
        }

        int leftHeight = nodeHeight(n.getLeft());
        int rightHeight = nodeHeight(n.getRight());
        return leftHeight - rightHeight;
    }

    /**
     * Rebalanceia a árvore no caminho de volta até a raiz.
     * <p>
     * O algoritmo atualiza a altura do Nó corrente, valida a invariante AVL
     * e, quando necessário, encontra o neto x para chamar o restructure.
     * </p>
     *
     * @param n Nó inicial da subida (normalmente o pai do Nó inserido/removido).
     */
    private void rebalance(NodeTree<T> n) {
        NodeTree<T> current = n;

        while (current != null && !current.isNil()) {
            current.updateHeight();
            int balanceFactor = getBalanceFactor(current);

            if (Math.abs(balanceFactor) > 1) {
                NodeTree<T> y = getHigherChild(current);
                NodeTree<T> x = getHigherChild(y);

                if (x != null && !x.isNil()) {
                    restructure(x);
                }
            }

            current = current.getParent();
        }
    }

    /**
     * Seleciona o filho mais alto de um Nó.
     * <p>
     * Em caso de empate, aplica um critério determinístico baseado no lado do pai.
     * </p>
     *
     * @param node Nó de referência.
     * @return filho com maior altura ou {@code null} quando o Nó for inválido.
     */
    private NodeTree<T> getHigherChild(NodeTree<T> node) {
        if (node == null || node.isNil()) {
            return null;
        }

        NodeTree<T> left = node.getLeft();
        NodeTree<T> right = node.getRight();

        int leftHeight = nodeHeight(left);
        int rightHeight = nodeHeight(right);

        if (leftHeight > rightHeight) {
            return left;
        }
        if (rightHeight > leftHeight) {
            return right;
        }

        // Em empate, privilegia o lado consistente com o proprio pai.
        NodeTree<T> parent = node.getParent();
        if (parent != null && !parent.isNil() && parent.getLeft() == node) {
            return left;
        }
        return right;
    }

    /**
     * Insere um novo Nó na AVL e reequilibra a árvore no caminho de retorno.
     *
     * @param x Nó a ser inserido.
     * @throws IllegalArgumentException quando o Nó for inválido ou possuir chave duplicada.
     */
    @Override
    public void insert(NodeTree<T> x) {
        if (x == null || x.isNil() || x.getData() == null) {
            throw new IllegalArgumentException("O Nó inserido deve possuir uma chave valida.");
        }

        T key = x.getData();

        if (isEmpty()) {
            prepareNode(x);
            this.root = x;
            x.setParent(this.nil);
            return;
        }

        NodeTree<T> current = this.root;
        NodeTree<T> parent = this.nil;

        while (!current.isNil()) {
            parent = current;
            int comparison = key.compareTo(current.getData());

            if (comparison == 0) {
                throw new IllegalArgumentException("Chave duplicada nao permitida na AVL.");
            }

            if (comparison < 0) {
                current = current.getLeft();
            } else {
                current = current.getRight();
            }
        }

        prepareNode(x);
        x.setParent(parent);
        if (key.compareTo(parent.getData()) < 0) {
            parent.setLeft(x);
        } else {
            parent.setRight(x);
        }

        rebalance(parent);
    }

    /**
     * Remove um Nó da AVL e reequilibra a árvore quando necessário.
     *
     * @param x Nó com a chave a ser removida.
     * @throws IllegalArgumentException quando o Nó for inválido ou a chave não existir.
     * @throws IllegalStateException quando a árvore estiver vazia.
     */
    @Override
    public void remove(NodeTree<T> x) {
        if (x == null || x.getData() == null) {
            throw new IllegalArgumentException("O Nó removido deve possuir uma chave valida.");
        }

        if (isEmpty()) {
            throw new IllegalStateException("Nao e possivel remover de uma arvore vazia.");
        }

        NodeTree<T> target = search(x);
        if (target.isNil()) {
            throw new IllegalArgumentException("Chave inexistente na AVL.");
        }

        NodeTree<T> rebalanceStart;

        if (target.getLeft().isNil()) {
            rebalanceStart = target.getParent();
            replaceNodeInParent(target, target.getRight());
        } else if (target.getRight().isNil()) {
            rebalanceStart = target.getParent();
            replaceNodeInParent(target, target.getLeft());
        } else {
            NodeTree<T> successor = minimumFrom(target.getRight());

            if (successor.getParent() == target) {
                rebalanceStart = successor;
            } else {
                rebalanceStart = successor.getParent();
                replaceNodeInParent(successor, successor.getRight());
                successor.setRight(target.getRight());
            }

            replaceNodeInParent(target, successor);
            successor.setLeft(target.getLeft());
            successor.updateHeight();
        }

        target.setLeft(this.nil);
        target.setRight(this.nil);
        target.setParent(this.nil);
        target.updateHeight();

        if (this.root.isNil()) {
            return;
        }

        if (rebalanceStart == null || rebalanceStart.isNil()) {
            this.root.updateHeight();
            return;
        }

        rebalance(rebalanceStart);
    }

    /**
     * Gera o percurso em pré-ordem da árvore.
     *
     * @return vetor com os Nós visitados em pré-ordem.
     */
    @Override
    public NodeTree<T>[] preOrder() {
        List<NodeTree<T>> nodes = new ArrayList<>();
        collectPreOrder(root, nodes);
        return toNodeArray(nodes);
    }

    /**
     * Gera o percurso em ordem da árvore.
     *
     * @return vetor com os Nós visitados em ordem.
     */
    @Override
    public NodeTree<T>[] inOrder() {
        List<NodeTree<T>> nodes = new ArrayList<>();
        collectInOrder(root, nodes);
        return toNodeArray(nodes);
    }

    /**
     * Gera o percurso em pós-ordem da árvore.
     *
     * @return vetor com os Nós visitados em pós-ordem.
     */
    @Override
    public NodeTree<T>[] postOrder() {
        List<NodeTree<T>> nodes = new ArrayList<>();
        collectPostOrder(root, nodes);
        return toNodeArray(nodes);
    }

    /**
     * Retorna o número total de Nós válidos da árvore.
     *
     * @return quantidade de elementos armazenados.
     */
    @Override
    public int size() {
        return countNodes(root);
    }

    /**
     * Retorna o Nó com a maior chave da árvore.
     *
     * @return maior Nó ou {@code null} quando a árvore estiver vazia.
     */
    @Override
    public NodeTree<T> maximum() {
        if (isEmpty()) {
            return null;
        }
        NodeTree<T> current = root;
        while (!current.getRight().isNil()) {
            current = current.getRight();
        }
        return current;
    }

    /**
     * Retorna o Nó com a menor chave da árvore.
     *
     * @return menor Nó ou {@code null} quando a árvore estiver vazia.
     */
    @Override
    public NodeTree<T> minimum() {
        if (isEmpty()) {
            return null;
        }
        NodeTree<T> current = root;
        while (!current.getLeft().isNil()) {
            current = current.getLeft();
        }
        return current;
    }

    /**
     * Retorna o sucessor em ordem de um Nó informado.
     *
     * @param w Nó de referência.
     * @return sucessor em ordem ou {@code null} quando nao existir.
     */
    @Override
    public NodeTree<T> successor(NodeTree<T> w) {
        if (w == null || w.getData() == null || isEmpty()) {
            return null;
        }

        NodeTree<T> current = search(w);
        if (current == null || current.isNil()) {
            return null;
        }

        if (!current.getRight().isNil()) {
            return minimumFrom(current.getRight());
        }

        NodeTree<T> ancestor = this.nil;
        NodeTree<T> cursor = root;
        while (!cursor.isNil() && cursor.getData().compareTo(current.getData()) != 0) {
            if (current.getData().compareTo(cursor.getData()) < 0) {
                ancestor = cursor;
                cursor = cursor.getLeft();
            } else {
                cursor = cursor.getRight();
            }
        }
        return ancestor.isNil() ? null : ancestor;
    }

    /**
     * Retorna o predecessor em ordem de um Nó informado.
     *
     * @param w Nó de referência.
     * @return predecessor em ordem ou {@code null} quando nao existir.
     */
    @Override
    public NodeTree<T> predecessor(NodeTree<T> w) {
        if (w == null || w.getData() == null || isEmpty()) {
            return null;
        }

        NodeTree<T> current = search(w);
        if (current == null || current.isNil()) {
            return null;
        }

        if (!current.getLeft().isNil()) {
            return maximumFrom(current.getLeft());
        }

        NodeTree<T> ancestor = this.nil;
        NodeTree<T> cursor = root;
        while (!cursor.isNil() && cursor.getData().compareTo(current.getData()) != 0) {
            if (current.getData().compareTo(cursor.getData()) > 0) {
                ancestor = cursor;
                cursor = cursor.getRight();
            } else {
                cursor = cursor.getLeft();
            }
        }
        return ancestor.isNil() ? null : ancestor;
    }

    /**
     * Conta recursivamente os Nós válidos da subárvore.
     *
     * @param node raiz da subárvore analisada.
     * @return quantidade de Nós não sentinela.
     */
    private int countNodes(NodeTree<T> node) {
        if (node.isNil()) {
            return 0;
        }
        return 1 + countNodes(node.getLeft()) + countNodes(node.getRight());
    }

    /**
     * Coleta os Nós da subárvore em pré-ordem.
     *
     * @param node raiz da subárvore.
     * @param nodes lista de acumulação do percurso.
     */
    private void collectPreOrder(NodeTree<T> node, List<NodeTree<T>> nodes) {
        if (node.isNil()) {
            return;
        }
        nodes.add(node);
        collectPreOrder(node.getLeft(), nodes);
        collectPreOrder(node.getRight(), nodes);
    }

    /**
     * Coleta os Nós da subárvore em ordem.
     *
     * @param node raiz da subárvore.
     * @param nodes lista de acumulação do percurso.
     */
    private void collectInOrder(NodeTree<T> node, List<NodeTree<T>> nodes) {
        if (node.isNil()) {
            return;
        }
        collectInOrder(node.getLeft(), nodes);
        nodes.add(node);
        collectInOrder(node.getRight(), nodes);
    }

    /**
     * Coleta os Nós da subárvore em pós-ordem.
     *
     * @param node raiz da subárvore.
     * @param nodes lista de acumulação do percurso.
     */
    private void collectPostOrder(NodeTree<T> node, List<NodeTree<T>> nodes) {
        if (node.isNil()) {
            return;
        }
        collectPostOrder(node.getLeft(), nodes);
        collectPostOrder(node.getRight(), nodes);
        nodes.add(node);
    }

    /**
     * Retorna o menor Nó a partir de uma subárvore informada.
     *
     * @param node raiz da subárvore de busca.
     * @return menor Nó da subárvore.
     */
    private NodeTree<T> minimumFrom(NodeTree<T> node) {
        NodeTree<T> current = node;
        while (current != null && current.getLeft() != null && !current.getLeft().isNil()) {
            current = current.getLeft();
        }
        return current;
    }

    /**
     * Retorna o maior Nó a partir de uma subárvore informada.
     *
     * @param node raiz da subárvore de busca.
     * @return maior Nó da subárvore.
     */
    private NodeTree<T> maximumFrom(NodeTree<T> node) {
        NodeTree<T> current = node;
        while (current != null && current.getRight() != null && !current.getRight().isNil()) {
            current = current.getRight();
        }
        return current;
    }

    /**
     * Retorna a altura de um Nó considerando segurança para nulos e sentinelas.
     *
     * @param node Nó avaliado.
     * @return altura do Nó ou {@code -1} quando nulo/sentinela.
     */
    private int nodeHeight(NodeTree<T> node) {
        return (node == null || node.isNil()) ? -1 : node.height();
    }

    /**
     * Cria uma cópia recursiva de uma subárvore utilizando um sentinela próprio.
     *
     * @param source Nó origem da cópia.
     * @param copyNil sentinela compartilhado da árvore copiada.
     * @param parent pai do Nó copiado no novo grafo.
     * @return raiz da subárvore copiada.
     */
    private NodeTree<T> copySubtree(NodeTree<T> source, NodeTree<T> copyNil, NodeTree<T> parent) {
        if (source == null || source.isNil()) {
            return copyNil;
        }

        NodeTree<T> copied = new NodeTree<>(source.getData());
        copied.setParent(parent);
        copied.setLeft(copySubtree(source.getLeft(), copyNil, copied));
        copied.setRight(copySubtree(source.getRight(), copyNil, copied));
        copied.updateHeight();
        return copied;
    }

    /**
     * Normaliza um Nó para uso interno na AVL, conectando-o ao sentinela.
     *
     * @param node Nó a ser preparado para inserção.
     */
    private void prepareNode(NodeTree<T> node) {
        node.setLeft(this.nil);
        node.setRight(this.nil);
        node.setParent(this.nil);
        node.updateHeight();
    }

    /**
     * Substitui um Nó por outro no vínculo do pai imediato.
     *
     * @param oldNode Nó que será removido do vínculo atual.
     * @param newNode Nó que assumirá a posição de {@code oldNode}.
     */
    private void replaceNodeInParent(NodeTree<T> oldNode, NodeTree<T> newNode) {
        NodeTree<T> parent = oldNode.getParent();

        if (parent == null || parent.isNil()) {
            this.root = newNode;
            if (!newNode.isNil()) {
                newNode.setParent(this.nil);
            }
            return;
        }

        if (parent.getLeft() == oldNode) {
            parent.setLeft(newNode);
        } else {
            parent.setRight(newNode);
        }
    }

    /**
     * Converte uma lista de Nós para vetor tipado.
     *
     * @param nodes lista a ser convertida.
     * @return vetor contendo os mesmos elementos da lista.
     */
    @SuppressWarnings("unchecked")
    private NodeTree<T>[] toNodeArray(List<NodeTree<T>> nodes) {
        return nodes.toArray(new NodeTree[0]);
    }
}
