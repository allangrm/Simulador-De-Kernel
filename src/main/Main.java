package main;

/**
 * Executa uma demonstração completa do escalonador Round-Robin.
 * Utilizando a Tabela Hash (PCB) para buscas em O(1), a AVL como índice ordenado,
 * a Max-Heap para a fila de prontos e a Fila com 2 Pilhas para o I/O.
 */
public class Main {
    /**
     * Inicia um roteiro de simulação com preempção, bloqueio, finalização e
     * tratamento de exceções do Kernel.
     *
     * @param args argumentos de linha de comando nao utilizados.
     */
    public static void main(String[] args) {
        Kernel kernel = new Kernel(2, 11);

        Process firstProcess = new Process(1, 3, 0, 4, 0);
        Process secondProcess = new Process(2, 2, 0, 5, 3);
        Process thirdProcess = new Process(3, 1, 0, 3, 0);

        kernel.enqueueProcess(firstProcess);
        kernel.enqueueProcess(secondProcess);
        kernel.enqueueProcess(thirdProcess);

        System.out.println("=== Demonstração do Kernel Round-Robin com PCB (Hash) e AVL ===");
        System.out.println("Quantum configurado: " + kernel.getQuantum());
        System.out.println("Tamanho inicial da Hash (PCB): " + kernel.getProcessTableSize());
        System.out.println("Processos indexados na AVL (inicial): " + kernel.getProcessIndexSize());

        System.out.println("\n[Teste de Segurança] Tentando inserir processo com PID duplicado...");
        try {
            Process duplicateProcess = new Process(1, 5, 0, 10, 0);
            kernel.enqueueProcess(duplicateProcess);
        } catch (IllegalArgumentException e) {
            System.out.println("-> Sucesso ao bloquear invasor: " + e.getMessage());
        }

        System.out.println("\n--- Estado Inicial ---");
        printProcesses(firstProcess, secondProcess, thirdProcess);
        printProcessLookup(kernel, 1);
        printProcessLookup(kernel, 2);
        printProcessLookup(kernel, 3);

        int cycle = 1;
        while (!areAllFinished(firstProcess, secondProcess, thirdProcess) && cycle <= 15) {
            System.out.println();
            System.out.println("--- Ciclo " + cycle + " ---");

            if (kernel.getIoBufferSize() > 0 && cycle % 3 == 0) {
                System.out.println("[Hardware] Resolução de I/O acionada antes da CPU neste ciclo.");
                kernel.resolveIO();
            }

            kernel.executeCycle();

            printKernelState(kernel);
            printProcesses(firstProcess, secondProcess, thirdProcess);

            printProcessLookup(kernel, 1);
            printProcessLookup(kernel, 2);
            printProcessLookup(kernel, 3);

            cycle++;
        }

        System.out.println();
        System.out.println("=== Resumo final da Simulação ===");
        System.out.println("Processos ativos na Hash (PCB final): " + kernel.getProcessTableSize());
        System.out.println("Processos indexados na AVL (final): " + kernel.getProcessIndexSize());
        printProcessLookup(kernel, 2);
        printProcesses(firstProcess, secondProcess, thirdProcess);
    }

    /**
     * Verifica se todos os processos do roteiro ja terminaram.
     *
     * @param processes processos acompanhados pela demonstração.
     * @return {@code true} quando todos estiverem finalizados.
     */
    private static boolean areAllFinished(Process... processes) {
        for (Process process : processes) {
            if (process.getState() != State.FINISHED) {
                return false;
            }
        }
        return true;
    }

    /**
     * Exibe um retrato resumido do estado global do kernel, mostrando todas as estruturas.
     *
     * @param kernel kernel observado na demonstração.
     */
    private static void printKernelState(Kernel kernel) {
        Process cpuProcess = kernel.getCpu();
        String cpuDescription = cpuProcess == null
                ? "livre"
                : "PID " + cpuProcess.getPid() + " em " + cpuProcess.getState();

        System.out.println("CPU: " + cpuDescription);
        System.out.println("Quantum atual: " + kernel.getCurrentQuantum());
        System.out.println("Fila READY (Max-Heap): " + kernel.getReadyQueueSize() + " processo(s)");
        System.out.println("Buffer I/O (Fila): " + kernel.getIoBufferSize() + " processo(s)");
        System.out.println("Hash (PCB): " + kernel.getProcessTableSize() + " processo(s) ativo(s)");
        System.out.println("Indice AVL: " + kernel.getProcessIndexSize() + " processo(s) ativo(s)");
    }

    /**
     * Exibe o resultado de uma busca por PID na Tabela Hash (PCB) do kernel.
     *
     * @param kernel kernel utilizado na simulação.
     * @param pid identificador do processo consultado.
     */
    private static void printProcessLookup(Kernel kernel, int pid) {
        Process found = kernel.findProcessByPid(pid);
        if (found == null) {
            System.out.println("Busca na Hash por PID " + pid + ": nao encontrado (ja finalizado ou inexistente).");
            return;
        }

        System.out.println(
                "Busca na Hash por PID " + pid
                        + ": encontrado em " + found.getState()
                        + " com prioridade " + found.getPriority()
        );
    }

    /**
     * Exibe o estado individual dos processos acompanhados.
     *
     * @param processes processos do roteiro da simulação.
     */
    private static void printProcesses(Process... processes) {
        for (Process process : processes) {
            System.out.println(
                    "PID " + process.getPid()
                            + " | Estado: " + process.getState()
                            + " | Restante: " + process.getRemainingTime()
                            + " | Espera: " + process.getWaitingTime()
                            + " | Executado: " + process.getExecutedCycles()
            );
        }
    }
}