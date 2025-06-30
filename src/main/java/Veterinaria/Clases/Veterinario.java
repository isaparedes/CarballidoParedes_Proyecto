package Veterinaria.Clases;

import javafx.application.Platform;

public class Veterinario {
    private final String nombre;
    private final SalaDeEspera sala;
    private VeterinarioListener listener;

    public Veterinario(String nombre, SalaDeEspera sala) {
        this.nombre = nombre;
        this.sala = sala;
    }

    public void addVeterinarioListener(VeterinarioListener l) {
        this.listener = l;
    }

    public void atender() {
        try {
            while (true) {
                Cliente c = sala.tomarCliente(); // Espera activa

                // Simula el tiempo que espera sentado antes de pasar
                Thread.sleep(2000 + (int)(Math.random() * 2000));

                if (listener != null) {
                    Platform.runLater(() -> listener.veterinarioAtendiendo(nombre, c));
                }

                Thread.sleep(2000); // Tiempo de atención

                // Libera espacio en la sala
                sala.liberarEspacio();

                // Verificar si es hora de cerrar
                if (Veterinaria.Interfaz.VeterinariaApp.todosLosClientesLlegaron() && sala.noHayMasClientes()) {
                    if (listener != null) {
                        Platform.runLater(() -> listener.veterinarioOrdenando(nombre));
                        Thread.sleep(4000);
                        Platform.runLater(() -> listener.veterinariaCerrada());
                    }
                    break;
                }
            }
        } catch (InterruptedException e) {
            System.out.println(nombre + " interrumpido");
        }
    }
}
