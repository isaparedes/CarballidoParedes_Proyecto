package Veterinaria;

public class Main {
    public static void main(String[] args) {
       SalaDeEspera sala = new SalaDeEspera(3);
       Veterinario veterinario = new Veterinario("Vanisa", sala);
       veterinario.start();
       for (int i = 1; i <= 6; i++) {
           final int id = i;
           new Thread(() -> {
               try {
                   Thread.sleep((int) (Math.random() * 3000));
                   Cliente cliente = new Cliente(nombreRandom()+" con número: "+id, razaRandom());
                   cliente.setNumero(id);
                   sala.nuevoCliente(cliente);
               }
               catch (InterruptedException e) {
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
        String[] nombres = {"Vanesa", "Isabela", "Juan", "Felipe", "Ana", "Florencia", "Dolores",
        "Andrea", "Marcos", "Agustín", "Constanza"};
        return nombres[(int) (Math.random() * nombres.length)];
    }
}