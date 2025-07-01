package Veterinaria.Clases;

import Veterinaria.Interfaz.VeterinarioListener;
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
                Cliente c = sala.tomarCliente();

                Thread.sleep(2000 + (int)(Math.random() * 2000));

                if (listener != null) {
                    Platform.runLater(() -> listener.veterinarioAtendiendo(nombre, c));
                    String mensaje = nombre + " está atendiendo a la mascota de "+c.getNombre();
                    ManejadorArchivosGenerico.escribirArchivo("src/main/java/Veterinaria/veterinaria.txt",
                            new String[]{mensaje});
                }

                Thread.sleep(2000);

                if (Veterinaria.Interfaz.VeterinariaApp.todosLosClientesLlegaron() && sala.noHayMasClientes()) {
                    if (listener != null) {
                        Platform.runLater(() -> listener.veterinarioOrdenando(nombre));
                        String mensaje = nombre + " está ordenando";
                        ManejadorArchivosGenerico.escribirArchivo("src/main/java/Veterinaria/veterinaria.txt",
                                new String[]{mensaje});
                        Thread.sleep(4000);
                    }
                    break;
                }
            }
        } catch (InterruptedException e) {
            System.out.println("interrumpido");
        }
    }
}