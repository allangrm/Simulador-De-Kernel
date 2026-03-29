package main.tads;

import main.tads.bst.NodeTree;
import main.tads.hash.NodeDouble;
import main.tads.linked_list.NodeList;

/**
 * Representa a estrutura fundamental base para a construção das estruturas de dados do sistema.
 * Utilizado para armazenar dados de qualquer tipo.
 * <p>
 * Esta classe é abstrata e serve como estrutura central para especializações como {@link NodeList}
 *, {@link NodeTree} e {@link NodeDouble}. O uso de tipos genéricos ({@code <T>}) garante que as estruturas possam
 * gerenciar diferentes objetos do dominio do mini-sistema.
 * </p>
 *
 * @param <T> o tipo de dado ou objeto que o Nó irá armazenar.
 * @author Allan Guilherme
 * @version 1.0
 * @since 2026-03-11
 */
public abstract class Node<T> {
    private T data;

    /**
     * Cria um novo Nó com o dado informado.
     *
     * @param data dado armazenado no no.
     */
    public Node(T data) {
        this.data = data;
    }

    /**
     * Retorna o dado armazenado no Nó.
     *
     * @return dado atualmente armazenado.
     */
    public T getData() {
        return data;
    }

    /**
     * Atualiza o dado armazenado no Nó.
     *
     * @param data novo dado a ser armazenado.
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * Informa se o Nó é um sentinela.
     *
     * @return {@code true} quando o dado armazenado é nulo; caso contrário, {@code false}.
     */
    public boolean isNil() {
        return this.data == null;
    }
}

