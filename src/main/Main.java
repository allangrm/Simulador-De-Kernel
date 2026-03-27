package main;

/**
 * Executa uma demonstração simples do escalonador Round-Robin.
 */
public class Main {
    /**
     * Inicia um roteiro simples de simulação com preempção, bloqueio e finalização.
     *
     * @param args argumentos de linha de comando nao utilizados.
     */
    public static void main(String[] args) {
        Kernel kernel = new Kernel(2);

        Process firstProcess = new Process(1, 3, 0, 4, 0);
        Process secondProcess = new Process(2, 2, 0, 5, 2);
        Process thirdProcess = new Process(3, 1, 0, 3, 0);

        kernel.enqueueProcess(firstProcess);
        kernel.enqueueProcess(secondProcess);
        kernel.enqueueProcess(thirdProcess);

        System.out.println("=== Demonstração simples do Kernel Round-Robin ===");
        System.out.println("Quantum configurado: " + kernel.getQuantum());
        System.out.println("Processos indexados na AVL (inicial): " + kernel.getProcessIndexSize());
        printProcesses(firstProcess, secondProcess, thirdProcess);
        printProcessLookup(kernel, 2);

        int cycle = 1;
        while (!areAllFinished(firstProcess, secondProcess, thirdProcess) && cycle <= 12) {
            System.out.println();
            System.out.println("--- Ciclo " + cycle + " ---");

            if (kernel.getIoBufferSize() > 0 && cycle % 3 == 0) {
                System.out.println("Resolução de I/O acionada antes da CPU neste ciclo.");
                kernel.resolveIO();
            }

            kernel.executeCycle();
            printKernelState(kernel);
            printProcesses(firstProcess, secondProcess, thirdProcess);
            printProcessLookup(kernel, 2);
            cycle++;
        }

        System.out.println();
        System.out.println("=== Resumo final ===");
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
     * Exibe um retrato resumido do estado global do kernel.
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
        System.out.println("Fila READY: " + kernel.getReadyQueueSize() + " processo(s)");
        System.out.println("Buffer I/O: " + kernel.getIoBufferSize() + " processo(s)");
        System.out.println("Indice AVL: " + kernel.getProcessIndexSize() + " processo(s) ativo(s)");
    }

    /**
     * Exibe o resultado de uma busca por PID no índice AVL do kernel.
     *
     * @param kernel kernel utilizado na simulação.
     * @param pid identificador do processo consultado.
     */
    private static void printProcessLookup(Kernel kernel, int pid) {
        Process found = kernel.findProcessByPid(pid);
        if (found == null) {
            System.out.println("Busca AVL por PID " + pid + ": nao encontrado (ja finalizado ou inexistente).");
            return;
        }

        System.out.println(
                "Busca AVL por PID " + pid
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