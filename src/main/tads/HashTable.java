package main.tads;

/**
 * Representa a implementação da estrutura: Tabela Hash.
 * <p>
 * Esta estrutura mapeia e armazena os dados em um array de Listas Duplamente
 * Encadeadas Cíclicas ({@link DoublyCircularLinkedList}). Cada posição do array
 * atua como um compartimento independente, garantindo que colisões de índices
 * sejam tratadas de forma dinâmica e sem perda de dados.
 * </p>
 * <p>
 * <b>Otimizações implementadas:</b>
 * <ul>
 * <li><b>Capacidade Prima Dinâmica:</b> A tabela intercepta a capacidade solicitada
 * no construtor e a autoajusta para o próximo número primo disponível.</li>
 * <li><b>Verificação de Potências:</b> Evita números primos
 * que estejam na "zona de perigo" de potências de 2, diminuindo problemas de
 * distribuição em sistemas binários.</li>
 * <li><b>Tratamento de Índices Negativos:</b> Processa corretamente chaves que geram
 * {@code hashCodes} negativos reposicionando o resultado dentro dos limites válidos do array,
 * o que previne falhas de overflow.</li>
 * </ul>
 * </p>
 *
 * @param <E> o tipo de elemento a ser armazenado nesta Tabela Hash.
 * @author Allan
 * @version 1.1
 * @since 2026-03-14
 */
public class HashTable<E> {
    private DoublyCircularLinkedList<E>[] table;
    private int capacity;
    private int size;

    public HashTable(int capacity){
        this.capacity = nextPrime(capacity);
        this.size = 0;

        this.table = (DoublyCircularLinkedList<E>[]) new DoublyCircularLinkedList[this.capacity];
        for(int i = 0; i < this.capacity; i++){
            this.table[i] = new DoublyCircularLinkedList<>();
        }
    }

    /**
     * Calcula o índice na Tabela Hash para um determinado elemento.
     * <p>
     * O metodo obtém o {@code hashCode()} do objeto e aplica a operação de módulo
     * em relação à capacidade atual da tabela. Garante de que o indice sempre
     * seja positivo e menor que o tamanho maximo da tabela.
     * </p>
     *
     * @param data o elemento cujo índice será calculado.
     * @return um número inteiro positivo representando a posição no array.
     */
    private int calculateIndex(E data){
        int index = data.hashCode() % capacity;
        if(index< 0){
            index += capacity;
        }
        return index;
    }

    /**
     * Insere um novo elemento na Tabela Hash.
     * <p>
     * O metodo calcula o índice apropriado para o dado e o insere no início da
     * lista duplamente encadeada correspondente.
     * </p>
     *
     * @param data o dado a ser inserido na tabela.
     */
    public void insert(E data){
        int index = calculateIndex(data);
        table[index].insertFirst(data);

        size++;
    }

    /**
     * Realiza a busca de um elemento específico dentro da Tabela Hash.
     * <p>
     * O metodo calcula o índice correspondente ao dado e delega a busca
     * para a lista daquela posição.
     * </p>
     *
     * @param data o elemento a ser buscado na tabela.
     * @return o elemento encontrado, ou {@code null} caso o dado não exista na tabela.
     */
    public E search(E data){
        int index = calculateIndex(data);
        NodeDouble<E> nodeAux = table[index].searchElement(data);
        if(nodeAux !=null) {
            return nodeAux.getData();
        }
        return null;
    }

    /**
     * Remove a ocorrência do elemento especificado na Tabela Hash, caso exista.
     *
     * @param data numero que sera removido da tabela.
     */
    public void remove(E data){
        int index = calculateIndex(data);
        if (isEmpty()) throw new IllegalArgumentException("Lista vazia");

        if(table[index].searchElement(data) !=null){
            table[index].removeElement(data);
            size--;
        }
    }

    /**
     * Verifica se o numero inserido e primo.
     * <p>
     * Se o número tiver um divisor cujo resto da divisão seja igual a zero,
     * o metodo retornara {@code false}.
     * </p>
     * Nota: Qualquer numero composto tera um divisor menor ou igual a sua raiz quadrada
     *
     * @param number numero que sera analisado com primo ou nao primo
     * @return {@code true} se o número for primo, ou {@code false} caso contrário.
     */
    private boolean isPrime(int number) {
        if (number <= 1) return false;

        for(int i = 2; i * i <= number; i++){
            if(number % i == 0) return false;
        }
        return true;
    }

    /**
     * Encontra o próximo número primo disponível a partir do valor inserido.
     *<p>
     *     Verifica se o numero informado é primo, enquanto nao for, ira somar +1
     *     ao valor atual. Quando a verificacao confirmar que o numero é primo encerra o laço
     *</p>
     * @param current numero que sera avaliado
     * @return proximo primo maior que o numero inserido
     */
    private int nextPrime(int current) {
        while(!(isPrime(current) && isSafe(current))){
            current++;
        }
        return current;

    }

    /**
     * Verifica se o numero inserido está distante de uma
     * potencia de 2, a fim de evitar colisões no sistema de
     * distribuição binário da Tabela Hash.
     *
     * <p>
     *     O metodo encontra a proxima potencia de 2 acima do numero inserido, calcula a
     *     distancia entre o numero e as potencias superior e a inferior,
     *     e verifica se esta há uma distancia de pelo menos 10%
     *     em relacao a potencia mais proxima
     * </p>
     *
     * @param number O número a ser verificado.
     * @return {@code true} caso seja um numero distante de uma potencia de 2,
     * ou {@code false} caso esteja proximo
     */
    public boolean isSafe(int number){
        int pow = 1;

        while(pow < number){
            pow *= 2;
        }

        int topDistance = pow - number;
        int bottomDistance = number - (pow / 2);

        int shortDistance = Math.min(topDistance,bottomDistance);
        if (shortDistance < pow*0.10) return false;

        return true;
    }

    /**
     * Informa se a lista esta vazia.
     *
     * @return {@code true} quando nao houver elementos.
     */
    public boolean isEmpty() {
        return this.size == 0;
    }

    /**
     * Retorna a capacidade atual da tabela.
     *
     * @return capacidade maxima da lista.
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Retorna a quantidade de elementos armazenados.
     *
     * @return tamanho atual da lista.
     */
    public int getSize() {
        return size;
    }
}
