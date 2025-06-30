package Veterinaria;

import Veterinaria.clases.Cliente;
import Veterinaria.clases.SalaDeEspera;
import Veterinaria.clases.Veterinario;

public class Main {
    public static void main(String[] args) {
        SalaDeEspera sala = new SalaDeEspera(6);

        // Crear veterinarios
        Veterinario vet1 = new Veterinario("Dra. Vanesa", sala);
        Veterinario vet2 = new Veterinario("Dra. Isabela", sala);

        // Iniciar la atención de los veterinarios usando semáforo
        new Thread(() -> vet1.atender()).start();
        new Thread(() -> vet2.atender()).start();

        synchronized (System.out) {
            System.out.println("VETERINARIA ABIERTA");
        }

        // Crear clientes aleatorios
        for (int i = 1; i <= 10; i++) {
            new Thread(() -> {
                try {
                    Thread.sleep((int) (Math.random() * 5000)); // Tiempo aleatorio de espera
                    Cliente c = new Cliente(nombreRandom(), razaRandom());
                    sala.llegaCliente(c);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    // Métodos para generar nombres y razas aleatorios
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
}
