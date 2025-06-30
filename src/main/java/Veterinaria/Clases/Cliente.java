package Veterinaria.Clases;

import javafx.scene.image.ImageView;

public class Cliente {

    private String nombre;
    private String raza;

    // Campos extra para la interfaz
    private transient ImageView imagenView;
    private transient int sillaIndex;

    public Cliente(String nombre, String raza) {
        this.nombre = nombre;
        this.raza = raza;
    }

    // Getters y setters normales
    public String getNombre() {
        return nombre;
    }

    public String getRaza() {
        return raza;
    }

    // Getters y setters para la animación
    public void setImagenView(ImageView imagenView) {
        this.imagenView = imagenView;
    }

    public ImageView getImagenView() {
        return imagenView;
    }

    public void setSillaIndex(int sillaIndex) {
        this.sillaIndex = sillaIndex;
    }

    public int getSillaIndex() {
        return sillaIndex;
    }
}
