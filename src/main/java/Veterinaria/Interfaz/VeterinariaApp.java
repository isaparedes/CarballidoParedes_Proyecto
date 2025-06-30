package Veterinaria.Interfaz;

import Veterinaria.Clases.Cliente;
import Veterinaria.Clases.SalaDeEspera;
import Veterinaria.Clases.Veterinario;
import Veterinaria.Clases.VeterinarioListener;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

public class VeterinariaApp extends Application {
    private Group root;
    private SalaDeEspera sala;

    private static int totalClientes = 0;
    private static final int MAX_CLIENTES = 30;

    private boolean cerrado = false;  // Bandera para controlar cierre único

    private double[][] sillas = {
            {100,140}, {200,140}, {307,140}, {407,140}, {509,140},
            {100,260}, {200,260}, {300,260}, {400,260}
    };
    private boolean[] sillasOcupadas = new boolean[sillas.length];

    private double[][] consultorios = {{200, 40}, {509, 40}};

    @Override
    public void start(Stage stage) {
        root = new Group();
        sala = new SalaDeEspera();

        Image fondo = new Image(getClass().getResourceAsStream("/Imagenes/vaciaConsAbiertos.png"));
        ImageView fondoView = new ImageView(fondo);
        fondoView.setFitWidth(800);
        fondoView.setFitHeight(600);
        fondoView.setPreserveRatio(false);
        root.getChildren().add(fondoView);

        Veterinario v1 = new Veterinario("Dra. Vanesa", sala);
        Veterinario v2 = new Veterinario("Dra. Isabela", sala);

        VeterinarioListener listener = new VeterinarioListener() {
            @Override
            public void veterinarioAtendiendo(String nombre, Cliente c) {
                Platform.runLater(() -> atenderCliente(nombre, c));
            }
            @Override
            public void veterinarioOrdenando(String nombre) {
                if (!cerrado) {
                    cerrado = true;
                    Platform.runLater(() -> {
                        mostrarOrden(nombre);
                        // Esperar animación y cerrar veterinaria
                        new Thread(() -> {
                            try {
                                Thread.sleep(4000);
                            } catch (InterruptedException e) {}
                            Platform.runLater(() -> cerrarVeterinaria());
                        }).start();
                    });
                }
            }
        };

        v1.addVeterinarioListener(listener);
        v2.addVeterinarioListener(listener);

        new Thread(() -> v1.atender()).start();
        new Thread(() -> v2.atender()).start();

        stage.setScene(new Scene(root, 800, 600));
        stage.setTitle("CARBALLIDO, PAREDES - VETERINARIA");
        stage.show();

        generarClientes();
    }

    private void generarClientes() {
        new Thread(() -> {
            String[] nombres = {"Juan", "Felipe", "Marcos", "Agustín", "Julián", "Romeo", "Alberto", "Ángel",
                    "Luciano", "Ana", "María", "Martina", "Julieta", "Sofía", "Laura", "Claudia"};
            String[] razas = {"Pastor", "Golden", "Caniche", "Doberman", "Salchicha", "Danés", "Yorkshire", "Corgi"};
            for (int i = 0; i < MAX_CLIENTES; i++) {
                try {
                    Thread.sleep(500 + (int)(Math.random() * 1500));

                    String imagenCliente = Math.random() > 0.5 ? "/Imagenes/cliente1.png" : "/Imagenes/cliente2.png";

                    Cliente c = new Cliente(
                            nombres[(int)(Math.random() * nombres.length)],
                            razas[(int)(Math.random() * razas.length)],
                            imagenCliente
                    );

                    Platform.runLater(() -> {
                        boolean sentado = animarCliente(c);
                        if (sentado) {
                            new Thread(() -> {
                                try {
                                    sala.llegaCliente(c);
                                    sala.veterinario.acquire(); // espera ser llamado por veterinario
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }).start();
                        }
                        totalClientes++;
                    });

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private boolean animarCliente(Cliente c) {
        ImageView view = new ImageView(new Image(getClass().getResourceAsStream(c.getImagenCliente())));
        view.setFitWidth(159);
        view.setFitHeight(176);
        view.setX(10);
        view.setY(590);
        root.getChildren().add(view);

        int silla = -1;
        for (int i = 0; i < sillasOcupadas.length; i++) {
            if (!sillasOcupadas[i]) {
                sillasOcupadas[i] = true;
                silla = i;
                break;
            }
        }

        if (silla == -1) {
            TranslateTransition subir = new TranslateTransition(Duration.seconds(2), view);
            subir.setByY(-120);

            TranslateTransition bajar = new TranslateTransition(Duration.seconds(1), view);
            bajar.setByY(100);

            subir.setOnFinished(e -> bajar.play());
            bajar.setOnFinished(e -> root.getChildren().remove(view));

            subir.play();

            return false;
        }

        double destX = sillas[silla][0];
        double destY = sillas[silla][1];
        TranslateTransition tt = new TranslateTransition(Duration.seconds(2), view);
        tt.setToX(destX - view.getX());
        tt.setToY(destY - view.getY());
        tt.play();

        c.setImagenView(view);
        c.setSillaIndex(silla);
        return true;
    }

    private void atenderCliente(String nombre, Cliente c) {
        sillasOcupadas[c.getSillaIndex()] = false;
        ImageView view = c.getImagenView();
        if (view == null) return;
        double[] pos = nombre.contains("Vanesa") ? consultorios[0] : consultorios[1];
        TranslateTransition tt = new TranslateTransition(Duration.seconds(2), view);
        tt.setToX(pos[0] - view.getX());
        tt.setToY(pos[1] - view.getY());
        tt.setOnFinished(e -> root.getChildren().remove(view));
        tt.play();
    }

    private void mostrarOrden(String nombre) {
        Text t = new Text(nombre + " ordenando");
        t.setFill(Color.GREENYELLOW);
        t.setX(600);
        t.setY(100);
        root.getChildren().add(t);
        TranslateTransition tt = new TranslateTransition(Duration.seconds(4), t);
        tt.setByY(-100);
        tt.setOnFinished(e -> root.getChildren().remove(t));
        tt.play();
    }

    private void cerrarVeterinaria() {
        root.getChildren().clear();

        Image nuevoFondo = new Image(getClass().getResourceAsStream("/Imagenes/cerrado.png"));
        ImageView fondoCerrado = new ImageView(nuevoFondo);
        fondoCerrado.setFitWidth(800);
        fondoCerrado.setFitHeight(600);
        fondoCerrado.setPreserveRatio(false);
        root.getChildren().add(fondoCerrado);

        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {}
            Platform.runLater(() -> Platform.exit());
        }).start();
    }

    public static boolean todosLosClientesLlegaron() {
        return totalClientes >= MAX_CLIENTES;
    }

    public static void main(String[] args) {
        launch();
    }
}
