import java.util.concurrent.*;
import java.util.stream.IntStream;
import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceExample {
    public static void main(String[] args) throws InterruptedException {
        // Inicializando o contador e o valor esperado
        Contador contador = new Contador();
        AtomicReference<Contador> atomic = new AtomicReference<>(contador);
        final int valorEsperado = 1000;

        // Criando um ExecutorService com 2 threads
        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {

            // Submetendo tarefas para o executor
            IntStream.range(0, valorEsperado).forEach(i -> {
                executor.submit(() -> {
                    // Obtém o contador atual de forma segura
                    Contador c = atomic.get();
                    c.increment();  // Incrementa o contador
                    atomic.set(c);  // Atualiza a referência atômica com o contador atualizado
                    // Exibe a thread e o valor atual
                    System.out.println("Thread " + Thread.currentThread().getName() + " - Valor Atual: " + c.getValor());
                });
            });

            // Finaliza a execução do executor
            executor.shutdown();
            // Espera até que todas as tarefas sejam concluídas
            if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
                System.out.println("Timeout atingido antes da conclusão de todas as tarefas.");
            }

            // Exibe o valor final do contador
            System.out.println("Valor final do contador: " + atomic.get().getValor());
            // Verifica se o valor final é igual ao esperado
            assert atomic.get().getValor() == valorEsperado : "Valor esperado não atingido!";
        }
    }
}

class Contador {
    private int valor = 0;

    public void increment() {
        valor++;
    }

    public int getValor() {
        return valor;
    }
}
