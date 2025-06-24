package Veterinaria;
import java.util.concurrent.TimeUnit;

public class Veterinario extends Thread {
    private final String nombre;
    private final SalaDeEspera sala;

    public Veterinario(String nombre, SalaDeEspera sala) {
        this.nombre = nombre;
        this.sala = sala;
    }

    public String getNombre() {
        return this.nombre;
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println(this.nombre + " esperando cliente...");
               boolean hayCliente = sala.intentarEsperarCliente();
                if (hayCliente) {
                    sala.atenderCliente(this.nombre);
                } else {
                    this.ordenar();
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void ordenar() {
        System.out.println(this.nombre+" esta ordenando...");
        try {
            Thread.sleep(3000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
