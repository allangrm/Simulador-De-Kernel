package main.tads.bst;

/**
 * Define o contrato de uma árvore binária de busca.
 *
 * @param <T> tipo de dado armazenado nos Nós da árvore.
 * @author Lucas
 * @version 1.0
 * @since 2026-03-27
 */
public interface BinarySearchTree<T> {
    /**
     * Retorna a raiz da árvore.
     *
     * @return referencia para a raiz atual.
     */
    NodeTree<T> getRoot();

    /**
     * Informa se a árvore está vazia.
     *
     * @return {@code true} quando nao existem elementos; caso contrario, {@code false}.
     */
    boolean isEmpty();

    /**
     * Retorna a altura da árvore.
     *
     * @return altura atual da árvore ou valor negativo para árvore vazia.
     */
    int height();

    /**
     * Busca um Nó com base na chave informada.
     *
     * @param k Nó contendo a chave de busca.
     * @return Nó encontrado ou um Nó sentinela quando a chave estiver ausente.
     */
    NodeTree<T> search(NodeTree<T> k);

    /**
     * Insere um novo Nó na árvore.
     *
     * @param x Nó a ser inserido.
     */
    void insert(NodeTree<T> x);

    /**
     * Remove um Nó da árvore.
     *
     * @param x Nó com a chave que deve ser removida.
     */
    void remove(NodeTree<T> x);

    /**
     * Gera o percurso em pré-ordem.
     *
     * @return vetor de Nós no percurso em pré-ordem.
     */
    NodeTree<T>[] preOrder();

    /**
     * Gera o percurso em ordem.
     *
     * @return vetor de Nós no percurso em ordem.
     */
    NodeTree<T>[] inOrder();

    /**
     * Gera o percurso em pós-ordem.
     *
     * @return vetor de Nós no percurso em pós-ordem.
     */
    NodeTree<T>[] postOrder();

    /**
     * Retorna a quantidade de Nós da árvore.
     *
     * @return numero total de elementos armazenados.
     */
    int size();

    /**
     * Retorna o maior Nó da árvore.
     *
     * @return Nó com maior chave ou {@code null} quando a árvore estiver vazia.
     */
    NodeTree<T> maximum();

    /**
     * Retorna o menor Nó da árvore.
     *
     * @return Nó com menor chave ou {@code null} quando a árvore estiver vazia.
     */
    NodeTree<T> minimum();

    /**
     * Retorna o sucessor em ordem de um Nó.
     *
     * @param w Nó de referência.
     * @return sucessor em ordem ou {@code null} quando nao existir.
     */
    NodeTree<T> successor(NodeTree<T> w);

    /**
     * Retorna o predecessor em ordem de um Nó.
     *
     * @param w Nó de referência.
     * @return predecessor em ordem ou {@code null} quando nao existir.
     */
    NodeTree<T> predecessor(NodeTree<T> w);
}
