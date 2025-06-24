package Veterinaria.clases;

public class Veterinario extends Thread {
    private final String nombre;
    private final SalaDeEspera sala;
    private int siCierra=0;

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
        siCierra++;
        if (siCierra==6) {
            System.out.println("El turno de "+this.nombre+" ha finalizado.");
            System.exit(0);
        }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

    }

}
