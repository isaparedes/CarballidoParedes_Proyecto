package Veterinaria.Clases;

public interface VeterinarioListener {
    void veterinarioAtendiendo(String nombreVet, Cliente cliente);
    void veterinarioOrdenando(String nombreVet);
    void veterinariaCerrada();
}
