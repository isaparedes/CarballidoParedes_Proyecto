package Veterinaria.Clases;

import javafx.scene.image.ImageView;

public class Cliente {
    private String nombre;
    private String raza;
    private transient ImageView imagenView;
    private transient int sillaIndex;

    private String imagenCliente;

    public Cliente(String nombre, String raza, String imagenCliente) {
        this.nombre = nombre;
        this.raza = raza;
        this.imagenCliente = imagenCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public String getRaza() {
        return raza;
    }

    public ImageView getImagenView() {
        return imagenView;
    }

    public void setImagenView(ImageView imagenView) {
        this.imagenView = imagenView;
    }

    public int getSillaIndex() {
        return sillaIndex;
    }

    public void setSillaIndex(int sillaIndex) {
        this.sillaIndex = sillaIndex;
    }

    public String getImagenCliente() {
        return imagenCliente;
    }
}
