package Veterinaria.Clases;

public class Veterinario extends Thread {
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

    @Override
    public void run() {
        try {
            while (true) {
                Cliente c = sala.tomarCliente();

                // Simular que el cliente espera antes de ser atendido
                Thread.sleep(2000 + (int)(Math.random() * 2000));

                if (listener != null) listener.veterinarioAtendiendo(nombre, c);

                Thread.sleep(2000); // Tiempo de atención

                if (Veterinaria.Interfaz.VeterinariaApp.todosLosClientesLlegaron() && sala.noHayMasClientes()) {
                    if (listener != null) {
                        listener.veterinarioOrdenando(nombre);
                        Thread.sleep(4000);
                        listener.veterinariaCerrada();
                    }
                    break;
                }
            }
        } catch (InterruptedException e) {
            System.out.println(nombre + " interrumpido");
        }
    }
}
