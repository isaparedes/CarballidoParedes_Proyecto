package Veterinaria.Interfaz;

import Veterinaria.Clases.Cliente;
import Veterinaria.Clases.SalaDeEspera;
import Veterinaria.Clases.Veterinario;
import Veterinaria.Clases.ClienteListener;
import Veterinaria.Clases.VeterinarioListener;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

public class VeterinariaApp extends Application {

    private Group root;
    private SalaDeEspera sala;

    // Posiciones de sillas y consultorios (ajusta según tu fondo)
    private double[][] posicionesSillas = {
            {100, 140}, {200, 140}, {307, 140}, {407, 140}, {509, 140},
            {100, 260}, {200, 260}, {300, 260}, {400, 260}
    };

    private double[][] posicionesConsultorios = { {200, 40}, {509, 40} };

    private boolean[] sillasOcupadas = new boolean[posicionesSillas.length];

    @Override
    public void start(Stage stage) {
        root = new Group();

        // Imagen de fondo
        Image fondo = new Image(getClass().getResourceAsStream("/Imagenes/vaciaConsAbiertos.png"));
        ImageView fondoView = new ImageView(fondo);
        fondoView.setFitWidth(800);
        fondoView.setFitHeight(600);
        fondoView.setPreserveRatio(false);
        root.getChildren().add(fondoView);

        sala = new SalaDeEspera(9);

        sala.addClienteListener(cliente -> {
            Platform.runLater(() -> animarCliente(cliente));
        });

        Veterinario vet1 = new Veterinario("Dra. Vanesa", sala);
        Veterinario vet2 = new Veterinario("Dra. Isabela", sala);

        VeterinarioListener vetListener = new VeterinarioListener() {
            @Override
            public void veterinarioAtendiendo(String nombreVet, Cliente cliente) {
                Platform.runLater(() -> animarAtencion(nombreVet, cliente));
            }

            @Override
            public void veterinarioOrdenando(String nombreVet) {
                Platform.runLater(() -> animarOrden(nombreVet));
            }

            @Override
            public void veterinariaCerrada() {
                Platform.runLater(() -> {
                    System.out.println("La veterinaria cerró.");
                    Platform.exit();
                });
            }
        };

        vet1.addVeterinarioListener(vetListener);
        vet2.addVeterinarioListener(vetListener);

        vet1.start();
        vet2.start();

        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Simulación Veterinaria");
        stage.setScene(scene);
        stage.show();

        iniciarSimulacion();
    }

    private void animarCliente(Cliente cliente) {
        Image imagenCliente = new Image(getClass().getResourceAsStream("/Imagenes/cliente.png"));
        ImageView clienteView = new ImageView(imagenCliente);
        clienteView.setFitWidth(159);
        clienteView.setFitHeight(176);
        clienteView.setX(10);
        clienteView.setY(590);
        root.getChildren().add(clienteView);

        // Buscar silla libre
        int sillaIndex = -1;
        for (int i = 0; i < sillasOcupadas.length; i++) {
            if (!sillasOcupadas[i]) {
                sillasOcupadas[i] = true;
                sillaIndex = i;
                break;
            }
        }

        if (sillaIndex == -1) {
            System.out.println(cliente.getNombre() + " no encontró silla y se fue.");
            // Animar salida: por ejemplo hacia la derecha fuera de la pantalla
            TranslateTransition salir = new TranslateTransition(Duration.seconds(2), clienteView);
            salir.setByX(400);
            salir.setByY(100);  // Sale por la derecha
            salir.setOnFinished(e -> root.getChildren().remove(clienteView));
            salir.play();

            return;
        }

        double destinoX = posicionesSillas[sillaIndex][0];
        double destinoY = posicionesSillas[sillaIndex][1];

        TranslateTransition animacion = new TranslateTransition(Duration.seconds(2), clienteView);
        animacion.setToX(destinoX - clienteView.getX());
        animacion.setToY(destinoY - clienteView.getY());
        animacion.play();

        cliente.setImagenView(clienteView);
        cliente.setSillaIndex(sillaIndex);
    }

    private void animarAtencion(String nombreVet, Cliente cliente) {
        ImageView clienteView = cliente.getImagenView();
        if (clienteView == null) return;

        // Liberar la silla
        sillasOcupadas[cliente.getSillaIndex()] = false;

        // Consultorio según veterinario
        double[] consultorio = nombreVet.contains("Vanesa") ? posicionesConsultorios[0] : posicionesConsultorios[1];

        TranslateTransition animacion = new TranslateTransition(Duration.seconds(2), clienteView);
        animacion.setToX(consultorio[0] - clienteView.getX());
        animacion.setToY(consultorio[1] - clienteView.getY());
        animacion.setOnFinished(e -> root.getChildren().remove(clienteView));
        animacion.play();

        Text atencion = new Text(nombreVet + " atiende a " + cliente.getNombre());
        atencion.setFill(Color.BLUE);
        atencion.setX(10);
        atencion.setY(50 + (int)(Math.random() * 100));
        root.getChildren().add(atencion);

        TranslateTransition tt = new TranslateTransition(Duration.seconds(3), atencion);
        tt.setByY(-50);
        tt.setOnFinished(e2 -> root.getChildren().remove(atencion));
        tt.play();
    }

    private void animarOrden(String nombreVet) {
        Text orden = new Text(nombreVet + " está ordenando...");
        orden.setFill(Color.GREEN);
        orden.setX(10);
        orden.setY(200 + (int)(Math.random() * 100));
        root.getChildren().add(orden);

        TranslateTransition tt = new TranslateTransition(Duration.seconds(2), orden);
        tt.setByY(-30);
        tt.setOnFinished(e -> root.getChildren().remove(orden));
        tt.play();
    }

    private void iniciarSimulacion() {
        new Thread(() -> {
            String[] razas = {"Pastor Alemán", "Golden Retriever", "Caniche", "Doberman", "Salchicha",
                    "Gran Danés", "Yorkshire", "Siamés", "Corgi", "Labrador", "Persa", "Carey", "cruza"};

            String[] nombres = {"Juan", "Felipe", "Marcos", "Agustín", "Julián", "Romeo", "Alberto",
                    "Ángel"};

            for (int i = 1; i <= 20; i++) {
                try {
                    Thread.sleep((int) (Math.random() * 1000));
                    Cliente c = new Cliente(
                            nombres[(int) (Math.random() * nombres.length)],
                            razas[(int) (Math.random() * razas.length)]
                    );
                    sala.llegaCliente(c);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void main(String[] args) {
        launch();
    }
}
