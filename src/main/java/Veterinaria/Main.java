package Veterinaria;

import Veterinaria.Clases.Cliente;
import Veterinaria.Clases.SalaDeEspera;
import Veterinaria.Clases.Veterinario;

public class Main {
    public static void main(String[] args) {
        /*
        SalaDeEspera sala = new SalaDeEspera(8);
        Veterinario vet1 = new Veterinario("Dra. Vanesa", sala);
        Veterinario vet2 = new Veterinario("Dra. Isabela", sala);
        vet1.start();
        vet2.start();

        synchronized (System.out) {
            System.out.println("VETERINARIA ABIERTA");
        }
        for (int i = 1; i <= 10; i++) {
            new Thread(() -> {
                try {
                    Thread.sleep((int) (Math.random() * 5000));
                    Cliente c = new Cliente(nombreRandom(), razaRandom());
                    sala.llegaCliente(c);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

         */
    }

    /*private static String razaRandom() {
        String[] razas = {"Pastor Alemán", "Golden Retriever", "Caniche", "Doberman", "Salchicha",
                "Gran Danés", "Yorkshire", "Siamés", "Corgi", "Labrador", "Persa", "Carey", "cruza",};
        return razas[(int) (Math.random() * razas.length)];
    }

    private static String nombreRandom() {
        String[] nombres = {"Juan", "Felipe", "Ana", "Florencia", "Dolores",
        "Andrea", "Marcos", "Agustín", "Constanza"};
        return nombres[(int) (Math.random() * nombres.length)];
    }
     */
}