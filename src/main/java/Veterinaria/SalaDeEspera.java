package Veterinaria;
import java.util.concurrent.Semaphore;

public class SalaDeEspera {
    private int lugaresLibres;
    private final Semaphore mutuaExclusion = new Semaphore(1);
    private final Semaphore mascotaEsperando = new Semaphore(0);
    private final Semaphore veterinarioListo = new Semaphore(0);

    public SalaDeEspera(int capacidad) {
        this.lugaresLibres = capacidad;
    }

    public void nuevoCliente(Cliente cliente) throws InterruptedException {
        mutuaExclusion.acquire();
        if (lugaresLibres > 0) {
            lugaresLibres--;
            System.out.println(cliente.getNombre()+" vino con un "+cliente.getRaza()
                    +". Lugares libres: "+this.lugaresLibres);
            mascotaEsperando.release();
            mutuaExclusion.release();
            veterinarioListo.acquire();
            System.out.println(cliente.getNombre()+" con el número "+cliente.getNumero()+" está siendo atendido");
        }
        else {
            System.out.println("No hay lugares libres, "+cliente.getNombre()+" se va");
            mutuaExclusion.release();
        }

    }

    public void esperarCliente() throws InterruptedException {
        mascotaEsperando.acquire();
    }

    public void atenderCliente(String veterinario) throws InterruptedException {
        mutuaExclusion.acquire();
        lugaresLibres++;
        System.out.println(veterinario+" atiende a un cliente. Lugares libres: "+this.lugaresLibres);
        mutuaExclusion.release();
        Thread.sleep(6000);
        veterinarioListo.release();
    }

    public boolean intentarEsperarCliente() throws InterruptedException {
        return mascotaEsperando.tryAcquire(1, java.util.concurrent.TimeUnit.SECONDS);
    }
}
