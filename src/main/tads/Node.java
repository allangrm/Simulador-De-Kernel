package main.tads;

/**
 * Representa a estrutura fundamental (Nó) base para a construção das estruturas de dados do sistema.
 * Utilizado para armazenar dados de qualquer tipo.
 * <p>
 * Esta classe é abstrata e serve como estrutura central para especializações como {@link NodeList}
 * e {@link NodeTree}. O uso de tipos genéricos ({@code <T>}) garante que as estruturas possam
 * gerenciar diferentes objetos do domínio do mini-sistema.
 * </p>
 * @param <T> o tipo de dado ou objeto que o nó irá armazenar.
 * @author Allan
 * @version 1.0
 * @since 2026-03-11
 */
public abstract class Node<T> {
    private T data; //uso do generics para a lista T poder ter qualquer Tipo de entrada

    public Node(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}

