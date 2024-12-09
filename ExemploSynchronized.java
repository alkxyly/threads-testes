package com.admin.frota.teste;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class ExemploSynchronized {
    public static void main(String[] args) throws InterruptedException {
        // Inicializando o contador e o valor esperado
        ContadorSyn contador = new ContadorSyn();
        final int valorEsperado = 1000;

        // Criando um ExecutorService com 2 threads
        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {

            // Submetendo tarefas para o executor
            IntStream.range(0, valorEsperado).forEach(i -> {
                executor.submit(() -> {
                    // Incrementa o contador de forma sincronizada
                    contador.increment();
                    // Exibe a thread e o valor atual
                    System.out.println("Thread " + Thread.currentThread().getName() + " - Valor Atual: " + contador.getValor());
                });
            });

            // Finaliza a execução do executor
            executor.shutdown();
            // Espera até que todas as tarefas sejam concluídas
            if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
                System.out.println("Timeout atingido antes da conclusão de todas as tarefas.");
            }

            // Exibe o valor final do contador
            System.out.println("Valor final do contador: " + contador.getValor());
            // Verifica se o valor final é igual ao esperado
            assert contador.getValor() == valorEsperado : "Valor esperado não atingido!";
        }
    }
}

class ContadorSyn {
    private int valor = 0;

    // Método sincronizado para garantir acesso exclusivo ao contador
    public synchronized void increment() {
        valor++;  // Incrementa o valor do contador
    }

    public int getValor() {
        return valor;  // Retorna o valor atual
    }
}
