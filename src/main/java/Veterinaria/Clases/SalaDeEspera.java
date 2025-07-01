package Veterinaria.Clases;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class SalaDeEspera {
    private final Queue<Cliente> cola = new LinkedList<>();
    private final Semaphore sillasDisponibles = new Semaphore(9);
    public final Semaphore cliente = new Semaphore(0);
    public final Semaphore veterinario = new Semaphore(0);
    public final Semaphore mutex = new Semaphore(1);

    public boolean llegaCliente(Cliente c) throws InterruptedException {
        mutex.acquire();
        if (sillasDisponibles.tryAcquire()) {
            cola.add(c);
            cliente.release();
            String mensaje = c.getNombre() + " vino con un "+c.getRaza();
            ManejadorArchivosGenerico.escribirArchivo("src/main/java/Veterinaria/veterinaria.txt",
                    new String[]{mensaje});
            mutex.release();
            return true;
        } else {
            mutex.release();
            String mensaje = c.getNombre() + " se fue porque no había lugar en la sala";
            System.out.println(mensaje);
            ManejadorArchivosGenerico.escribirArchivo("src/main/java/Veterinaria/veterinaria.txt",
                    new String[]{mensaje});
            return false;
        }
    }

    public Cliente tomarCliente() throws InterruptedException {
        cliente.acquire();
        mutex.acquire();
        Cliente c = cola.poll();
        veterinario.release();
        sillasDisponibles.release();
        mutex.release();
        return c;
    }

    public boolean noHayMasClientes() {
        mutex.acquireUninterruptibly();
        boolean vacio = cola.isEmpty();
        mutex.release();
        return vacio;
    }
}