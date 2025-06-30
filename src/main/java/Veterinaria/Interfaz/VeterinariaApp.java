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
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

public class VeterinariaApp extends Application {

    private Group root;
    private SalaDeEspera sala;

    @Override
    public void start(Stage stage) {
        root = new Group();

        // Imagen de fondo
        Image fondo = new Image(getClass().getResourceAsStream("/Imagenes/vaciaConsAbiertos.png"));
        ImageView fondoView = new ImageView(fondo);
        fondoView.setFitWidth(800);
        fondoView.setFitHeight(600);
        fondoView.setPreserveRatio(false);

        // Agregamos el fondo primero para que quede detrás
        root.getChildren().add(fondoView);

        sala = new SalaDeEspera(9);

        // Suscribirse a llegada de clientes
        sala.addClienteListener(cliente -> {
            Platform.runLater(() -> animarCliente(cliente));
        });

        // Crear y arrancar veterinarios
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
        // Cargamos la imagen que deseas para el cliente
        Image imagenCliente = new Image(getClass().getResourceAsStream("/Imagenes/cliente.png"));
        ImageView clienteView = new ImageView(imagenCliente);

        // Ajustamos el tamaño de la imagen si quieres (opcional)
        clienteView.setFitWidth(159); // ajusta a tu gusto
        clienteView.setFitHeight(176); // ajusta a tu gusto

        // Posición inicial
        clienteView.setX(10);
        clienteView.setY(590);

        // Agregamos al root
        root.getChildren().add(clienteView);

        // Texto con el nombre del cliente
        Text texto = new Text(cliente.getNombre());
        texto.setX(50);
        texto.setY(480);
        root.getChildren().add(texto);

        // Animación
        TranslateTransition animacion = new TranslateTransition(Duration.seconds(3), clienteView);
        animacion.setByX(0);
        animacion.setByY(-200);
        animacion.setOnFinished(e -> {
            root.getChildren().remove(clienteView);
            root.getChildren().remove(texto);
        });
        animacion.play();
    }


    private void animarAtencion(String nombreVet, Cliente cliente) {
        Text atencion = new Text(nombreVet + " atiende a " + cliente.getNombre());
        atencion.setFill(Color.BLUE);
        atencion.setX(10);
        atencion.setY(50 + (int)(Math.random() * 100));
        root.getChildren().add(atencion);

        TranslateTransition tt = new TranslateTransition(Duration.seconds(3), atencion);
        tt.setByY(-50);
        tt.setOnFinished(e -> root.getChildren().remove(atencion));
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

            for (int i = 1; i <= 10; i++) {
                try {
                    Thread.sleep((int) (Math.random() * 5000));
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
