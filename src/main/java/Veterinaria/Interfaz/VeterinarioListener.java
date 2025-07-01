package Veterinaria.Interfaz;

import Veterinaria.Clases.Cliente;

public interface VeterinarioListener {
    void veterinarioAtendiendo(String nombreVet, Cliente cliente);
    void veterinarioOrdenando(String nombreVet);
}