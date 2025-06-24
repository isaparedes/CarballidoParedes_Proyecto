package Veterinaria;

public class Cliente {
    private final String nombre;
    private final String raza;
    private int numero;

    public Cliente(String nombre,String raza) {
        this.nombre = nombre;
        this.raza = raza;
    }

    public String getNombre() {
        return this.nombre;
    }

    public String getRaza() {
        return this.raza;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getNumero() {
        return this.numero;
    }
}
