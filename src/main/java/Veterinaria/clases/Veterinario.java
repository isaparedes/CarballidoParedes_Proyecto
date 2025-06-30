package Veterinaria.clases;

import java.util.concurrent.Semaphore;

public class Veterinario {
    private final String nombre;
    private final SalaDeEspera sala;
    private int siCierra;

    // Semáforo para controlar los veterinarios disponibles (2 veterinarios)
    private static final Semaphore veterinariosDisponibles = new Semaphore(2);

    public Veterinario(String nombre, SalaDeEspera sala) {
        this.nombre = nombre;
        this.sala = sala;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void atender() {
        while (true) {
            try {
                // Intentamos adquirir un permiso del semáforo
                veterinariosDisponibles.acquire();
                System.out.println(this.nombre + " está esperando cliente...");

                // Verificamos si hay clientes para atender
                boolean hayCliente = sala.intentarEsperarCliente();
                if (hayCliente) {
                    sala.atenderCliente(this.nombre);
                } else {
                    this.ordenar();
                }

                // Liberamos el semáforo para que otro veterinario pueda atender
                veterinariosDisponibles.release();

                // Dormimos un poco para simular el trabajo
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void ordenar() {
        synchronized (System.out) {
            System.out.println(this.nombre + " está ordenando...");
        }
        siCierra++;
        if (siCierra == 6) {
            synchronized (System.out) {
                System.out.println("VETERINARIA CERRADA");
            }
            System.exit(0);
        }
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
