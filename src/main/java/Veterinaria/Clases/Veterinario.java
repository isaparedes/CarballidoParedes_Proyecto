package Veterinaria.Clases;

import java.util.List;
import java.util.ArrayList;

public class Veterinario extends Thread {
    private final String nombre;
    private final SalaDeEspera sala;
    private int siCierra = 0;

    private final List<VeterinarioListener> listeners = new ArrayList<>();

    public Veterinario(String nombre, SalaDeEspera sala) {
        this.nombre = nombre;
        this.sala = sala;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void addVeterinarioListener(VeterinarioListener listener) {
        listeners.add(listener);
    }

    private void notificarAtendiendo(Cliente cliente) {
        for (VeterinarioListener l : listeners) {
            l.veterinarioAtendiendo(nombre, cliente);
        }
    }

    private void notificarOrdenando() {
        for (VeterinarioListener l : listeners) {
            l.veterinarioOrdenando(nombre);
        }
    }

    private void notificarCerrada() {
        for (VeterinarioListener l : listeners) {
            l.veterinariaCerrada();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println(this.nombre + " esperando cliente...");
                boolean hayCliente = sala.intentarEsperarCliente();
                if (hayCliente) {
                    Cliente cliente = sala.atenderCliente(this.nombre);
                    notificarAtendiendo(cliente);
                } else {
                    this.ordenar();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void ordenar() {
        synchronized (System.out) {
            System.out.println(this.nombre + " está ordenando...");
        }
        notificarOrdenando();

        siCierra++;
        if (siCierra == 6) {
            synchronized (System.out) {
                System.out.println("VETERINARIA CERRADA");
            }
            notificarCerrada();
            System.exit(0);
        }
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
