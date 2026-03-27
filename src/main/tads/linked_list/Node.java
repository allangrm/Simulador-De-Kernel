package main.tads.linked_list;

import main.tads.bst.NodeTree;
import main.tads.hash.NodeDouble;

/**
 * Representa a estrutura fundamental base para a construcao das estruturas de dados do sistema.
 * Utilizado para armazenar dados de qualquer tipo.
 * <p>
 * Esta classe e abstrata e serve como estrutura central para especializacoes como {@link NodeList}
 *, {@link NodeTree} e {@link NodeDouble}. O uso de tipos genericos ({@code <T>}) garante que as estruturas possam
 * gerenciar diferentes objetos do dominio do mini-sistema.
 * </p>
 *
 * @param <T> o tipo de dado ou objeto que o No ira armazenar.
 * @author Allan
 * @version 1.0
 * @since 2026-03-11
 */
public abstract class Node<T> {
    private T data;

    /**
     * Cria um novo No com o dado informado.
     *
     * @param data dado armazenado no no.
     */
    public Node(T data) {
        this.data = data;
    }

    /**
     * Retorna o dado armazenado no No.
     *
     * @return dado atualmente armazenado.
     */
    public T getData() {
        return data;
    }

    /**
     * Atualiza o dado armazenado no No.
     *
     * @param data novo dado a ser armazenado.
     */
    public void setData(T data) {
        this.data = data;
    }

}

