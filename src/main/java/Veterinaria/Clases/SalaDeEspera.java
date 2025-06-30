package Veterinaria.Clases;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class SalaDeEspera {
    private final Queue<Cliente> cola = new LinkedList<>();
    private final Semaphore semaforoSalaEspera = new Semaphore(9); // 9 sillas

    public void llegaCliente(Cliente c) {
        try {
            semaforoSalaEspera.acquire();
            synchronized (cola) {
                cola.add(c);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Cliente tomarCliente() throws InterruptedException {
        while (true) {
            synchronized (cola) {
                if (!cola.isEmpty()) {
                    return cola.poll();
                }
            }
            Thread.sleep(100);
        }
    }

    public boolean noHayMasClientes() {
        synchronized (cola) {
            return cola.isEmpty();
        }
    }

    public void liberarEspacio() {
        semaforoSalaEspera.release();
    }
}
