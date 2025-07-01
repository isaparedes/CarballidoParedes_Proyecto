package Veterinaria;

import Veterinaria.Clases.Cliente;
import Veterinaria.Clases.SalaDeEspera;
import Veterinaria.Clases.Veterinario;

import java.util.concurrent.Semaphore;

public class Main {
    private static final Semaphore semaforoCierre = new Semaphore(1); // Semáforo de mutua exclusión para cerrar la veterinaria

    public static void main(String[] args) {
        SalaDeEspera sala = new SalaDeEspera();

        // Crear los veterinarios
        Veterinario vet1 = new Veterinario("Dra. Vanesa", sala);
        Veterinario vet2 = new Veterinario("Dra. Isabela", sala);

        // Iniciar la atención de los veterinarios usando el método 'atender' en un nuevo hilo
        new Thread(() -> vet1.atender()).start();
        new Thread(() -> vet2.atender()).start();

        synchronized (System.out) {
            System.out.println("VETERINARIA ABIERTA");
        }

        // Crear clientes aleatorios
        for (int i = 1; i <= 13; i++) {
            new Thread(() -> {
                try {
                    Thread.sleep((int) (Math.random() * 1000)); // Tiempo aleatorio de espera
                    // Generamos un cliente con el nombre, raza y una imagen aleatoria
                    Cliente c = new Cliente(nombreRandom(), razaRandom(), Math.random() > 0.5 ? "/Imagenes/cliente1.png" : "/Imagenes/cliente2.png");
                    sala.llegaCliente(c);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private static String razaRandom() {
        String[] razas = {"Pastor Alemán", "Golden Retriever", "Caniche", "Doberman", "Salchicha",
                "Gran Danés", "Yorkshire", "Siamés", "Corgi", "Labrador", "Persa", "Carey", "cruza"};
        return razas[(int) (Math.random() * razas.length)];
    }

    private static String nombreRandom() {
        String[] nombres = {"Juan", "Felipe", "Ana", "Florencia", "Dolores",
                "Andrea", "Marcos", "Agustín", "Constanza"};
        return nombres[(int) (Math.random() * nombres.length)];
    }

    public static void cerrarVeterinaria() throws InterruptedException {
        try {
            // Intentamos adquirir el semáforo
            if (!semaforoCierre.tryAcquire()) {
                return; // Si otro hilo ya está cerrando, no hace nada
            }

            // Lógica de cierre de la veterinaria
            System.out.println("La veterinaria ha cerrado.");
            // Aquí puedes agregar el código para hacer que la interfaz cierre o que se realicen más acciones

        } finally {
            // Liberamos el semáforo de cierre
            semaforoCierre.release();
        }
    }



}

