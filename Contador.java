package com.admin.frota.teste;

public class Contador {
    private int valor = 0;

    public synchronized void increment() {
        valor++;
    }

    public synchronized int getValor() {
        return valor;
    }
}
