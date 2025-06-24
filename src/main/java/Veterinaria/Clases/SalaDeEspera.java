package Veterinaria.Clases;

import java.util.Queue;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import java.util.List;
import java.util.ArrayList;

public class SalaDeEspera {
    private int sillasLibres;

    private final Semaphore mutex = new Semaphore(1);
    private final Semaphore clienteListo = new Semaphore(0);
    private final Queue<Cliente> colaClientes = new LinkedList<>();

    // Lista de listeners para notificar llegada de clientes
    private final List<ClienteListener> listeners = new ArrayList<>();

    public SalaDeEspera(int capacidad) {
        this.sillasLibres = capacidad;
    }

    // Método para agregar listeners
    public void addClienteListener(ClienteListener listener) {
        listeners.add(listener);
    }

    // Notificar listeners de un cliente que llegó y se sentó
    private void notificarClienteLlegado(Cliente cliente) {
        for (ClienteListener listener : listeners) {
            listener.clienteLlego(cliente);
        }
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

            // Notificar a los listeners (la UI, por ejemplo)
            notificarClienteLlegado(cliente);

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
