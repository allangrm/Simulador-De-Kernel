# Simulador de Kernel (Gerenciador de Processos)

O **Simulador-De-Kernel** é um projeto em Java que emula o núcleo lógico de um sistema operacional. Ele gerencia o ciclo de vida, a prioridade e a organização de processos virtuais, integrando de forma prática estruturas de dados avançadas. O sistema utiliza um motor de escalonamento baseado em **Round-Robin com Preempção por Quantum**, mesclado com prioridade dinâmica.

Este projeto foi desenvolvido como requisito para a Unidade 2 da disciplina de **Laboratório de Estruturas de Dados**. O objetivo principal é projetar, implementar manualmente e analisar o custo assintótico de estruturas de dados clássicas sem depender das coleções nativas do Java (como `java.util.*`).

## 🏗️ Arquitetura e Estruturas de Dados

Para simular o comportamento de um computador real, cada estrutura de dados exigida no projeto assumiu um papel vital na arquitetura do Kernel:

* **Tabela Hash (Process Control Block - PCB):**
    * **Função:** Atua como o registro mestre de processos. Permite ao Kernel encontrar e encerrar processos pelo `PID` com eficiência média de $O(1)$.
    * **Implementação:** Utiliza a técnica de *Chaining* (encadeamento) para resolver colisões, empregando internamente uma **Lista Duplamente Encadeada Circular**.
* **Max-Heap Binária (Escalonador da CPU):**
    * **Função:** Fila de processos "Prontos". Garante que o processo com a maior prioridade receba o tempo de processador imediatamente.
    * **Implementação:** Baseada em vetor, com métodos de rebaixamento e promoção matemáticos para reordenar a árvore em $O(\log n)$.
* **Árvore AVL (Índice de Processos):**
    * **Função:** Mantém os processos ativos rigorosamente balanceados e ordenados pelo `PID`. Permite a geração de relatórios de sistema ordenados de forma eficiente.
    * **Implementação:** Árvore binária de busca com fator de balanceamento dinâmico e rotações (simples e duplas) à esquerda e à direita. Busca no pior caso: $O(\log n)$.
* **Fila (Buffer de I/O):**
    * **Função:** Fila de espera (FIFO) para processos bloqueados que aguardam recursos externos (como leitura de disco ou rede).
    * **Implementação:** Construída rigorosamente utilizando **duas instâncias de Pilha**, que por sua vez operam sobre uma **Lista Simplesmente Encadeada** construída do zero.

## ⚙️ Como a Simulação Funciona

1. **Criação:** Um objeto `Processo` é instanciado e cadastrado simultaneamente na Tabela Hash e na Árvore AVL. Ele nasce com o estado `PRONTO` e entra na Max-Heap.
2. **Execução (Round-Robin):** O simulador retira o processo do topo da Max-Heap e altera seu estado para `EXECUTANDO`. O processo roda na CPU por uma fatia de tempo definida (Quantum).
3. **Preempção:** Se o processo não finalizar suas instruções dentro do Quantum, ele sofre preempção e retorna para a Max-Heap.
4. **Bloqueio de I/O:** Se um processo requisitar um recurso externo, ele é retirado da disputa pela CPU e vai para a Fila de I/O, assumindo o estado `BLOQUEADO_IO`.
5. **Finalização:** Quando as instruções chegam a zero, o processo é considerado `FINALIZADO` e suas referências são removidas da Tabela Hash e da Árvore AVL, liberando memória.

## 🚀 Tecnologias e Requisitos

* **Java (JDK 17 ou superior):** Linguagem base do projeto.
* **JUnit 5:** Framework utilizado para a elaboração e execução das suítes de testes unitários de cada estrutura.

## 👨‍💻 Autores (Grupo)

* **Lucas Nóbrega** - [https://github.com/lucrasn]
* **Allan Guilherme** - [https://github.com/allangrm]
* (**Raffael Wagner**) - [https://github.com/Raffael-Wagner]

---

**Nota:** Os arquivos fonte deste projeto estão disponibilizados de forma avulsa e não compactada, conforme os padrões de entrega da disciplina. O link para o vídeo de análise de complexidade e demonstração prática pode ser encontrado na planilha de entregas oficial.
