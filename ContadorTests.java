package com.admin.frota.teste;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

import static java.lang.System.out;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ContadorTests {

    @RepeatedTest(1000)
    //@Test
    @DisplayName("Dado incremento em range de 0 a 1000 entao valor deve ser 1000, com synchronized")
    public void t1() throws InterruptedException {
        var contador = new Contador();
        final var valorEsperado = 1000;

        try (ExecutorService executor = newFixedThreadPool(2)) {

            IntStream.range(0, valorEsperado)
                    .forEach(i -> executor.submit(contador::increment));

          //  Thread.sleep(500);

            executor.shutdown();
            if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
                System.out.println("Timeout atingido antes da conclusão de todas as tarefas.");
            }

            out.println("Valor " + contador.getValor());
            assertEquals(valorEsperado, contador.getValor());
        }
    }

    //@Test
    @RepeatedTest(1000)
    @DisplayName("Dado incremento em range de 0 a 1000 entao valor deve ser 1000")
    public void t2() throws InterruptedException {
        var contador = new Contador();
        final var valorEsperado = 1000;
        AtomicReference<Contador> atomic = new AtomicReference<>(contador);


        try (ExecutorService executor = newFixedThreadPool(2)) {

            IntStream.range(0, valorEsperado)
                    .forEach(i -> {
                        Contador c = atomic.get();
                        c.increment();
                        atomic.set(c);
                    });

            //Thread.sleep(500);

            out.println("Valor " + contador.getValor());
            assertEquals(valorEsperado, contador.getValor());
            executor.shutdown();
        }
    }
}
