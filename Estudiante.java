package modelo;

import java.io.Serializable;

public class Estudiante implements Serializable {
  private String legajo;
  private String nombre;
  //constructor
    public Estudiante(String legajo, String nombre) {
        this.legajo = legajo;
        this.nombre = nombre;
    }
    //nombre es privado de la clase estudiante,por lo cual para poder mostrarlo en Inscripcion usamos get(obtener)
    public String getNombre() {
        return nombre;
    }

    public String getLegajo() {
        return legajo;
    }
}
