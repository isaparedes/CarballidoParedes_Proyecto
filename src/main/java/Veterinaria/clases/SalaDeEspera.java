package Veterinaria.clases;

import java.util.Queue;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class SalaDeEspera {
    private int sillasLibres;

    private final Semaphore mutex = new Semaphore(1);
    private final Semaphore clienteListo = new Semaphore(0);
    private final Queue<Cliente> colaClientes = new LinkedList<>();

    public SalaDeEspera(int capacidad) {
        this.sillasLibres = capacidad;
    }

    public void llegaCliente(Cliente cliente) throws InterruptedException {
        mutex.acquire();
        if (sillasLibres > 0) {
            sillasLibres--;
            colaClientes.add(cliente);
            synchronized (System.out) {
                System.out.println(cliente.getNombre() + " llega con un " +
                        cliente.getRaza() + ". Sillas libres: " + sillasLibres);
            }
            clienteListo.release();
        } else {
            synchronized (System.out) {
                System.out.println(cliente.getNombre() + " se va, no hay lugar");
            }
        }
        mutex.release();
    }

    public Cliente atenderCliente(String nombreVet) throws InterruptedException {
        clienteListo.acquire();

        mutex.acquire();
        Cliente cliente = colaClientes.poll();
        sillasLibres++;
        mutex.release();

        synchronized (System.out) {
            System.out.println("🩺 " + nombreVet + " atiende a " + cliente.getNombre() +
                    " con mascota " + cliente.getRaza() +
                    ". Sillas libres: " + sillasLibres);
        }

        Thread.sleep(3000);
        return cliente;
    }

    public boolean intentarEsperarCliente() throws InterruptedException {
        return clienteListo.tryAcquire(1, java.util.concurrent.TimeUnit.SECONDS);
    }
}
