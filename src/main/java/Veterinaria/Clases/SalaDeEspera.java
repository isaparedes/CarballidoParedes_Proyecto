package Veterinaria.Clases;

import java.util.LinkedList;
import java.util.Queue;

public class SalaDeEspera {
    private final Queue<Cliente> cola = new LinkedList<>();

    public synchronized void llegaCliente(Cliente c) {
        cola.add(c);
        notifyAll();
    }

    public synchronized Cliente tomarCliente() throws InterruptedException {
        while (cola.isEmpty()) {
            wait();
        }
        return cola.poll();
    }

    public synchronized boolean noHayMasClientes() {
        return cola.isEmpty();
    }
}
