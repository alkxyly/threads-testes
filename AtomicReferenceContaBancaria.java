package com.admin.frota;
import java.util.concurrent.atomic.AtomicReference;
public class AtomicReferenceContaBancaria {

    public static void main(String[] args) throws InterruptedException {
        // Inicializa a conta bancária com saldo de 1000
        ContaBancaria conta = new ContaBancaria(1000);
        AtomicReference<ContaBancaria> refConta = new AtomicReference<>(conta);

        // Simula duas threads tentando modificar o saldo ao mesmo tempo
        Thread t1 = new Thread(() -> {
            ContaBancaria c = refConta.get();
            c.deposito(500);  // A thread t1 faz um depósito de 500
            refConta.set(c);   // Atualiza a referência de conta com o novo saldo
            System.out.println("Thread 1 - Saldo após depósito: " + c.getSaldo());
        });

        Thread t2 = new Thread(() -> {
            ContaBancaria c = refConta.get();
            c.deposito(200);  // A thread t2 faz um depósito de 200
            refConta.set(c);   // Atualiza a referência de conta com o novo saldo
            System.out.println("Thread 2 - Saldo após depósito: " + c.getSaldo());
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        // Exibe o saldo final da conta
        System.out.println("Saldo final da conta: " + refConta.get().getSaldo());
    }

    static class ContaBancaria {
        private double saldo;

        public ContaBancaria(double saldo) {
            this.saldo = saldo;
        }

        public double getSaldo() {
            return saldo;
        }

        public void deposito(double valor) {
            this.saldo += valor;
        }
    }
}