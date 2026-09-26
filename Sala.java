package modelo;

import java.io.Serializable;

    public class Sala implements Serializable {
    private int id;
    private String nombre;

    public Sala(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

}