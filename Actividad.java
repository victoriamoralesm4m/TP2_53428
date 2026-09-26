package modelo.actividades;
import excepciones.CupoExcedidoException;
import modelo.Estudiante;
import modelo.Inscripcion;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract  class Actividad  implements Serializable {
    private int id;
    private String titulo;
    private int cupoMaximo;
    public static final int CUPO_MINIMO = 8;

    private List<Inscripcion> inscripciones = new ArrayList<>();

    //constructor actividad
    public Actividad(int id, String titulo, int cupoMaximo) {
        this.id = id;
        this.titulo = titulo;
        this.cupoMaximo = cupoMaximo;
    }
    //metodos punto 2
    public Inscripcion inscribir(Estudiante estudiante) throws CupoExcedidoException {
        if (inscripciones.size() >= cupoMaximo) {
            throw new CupoExcedidoException("No se puede inscribir: la actividad " + titulo + " está completa.");
        } else {
            Inscripcion nuevaInscripcion = new Inscripcion(estudiante, this);
            inscripciones.add(nuevaInscripcion);
            return nuevaInscripcion;
        }
    }


    public void mostrarInscripciones() {
        System.out.println("Inscripciones en: " + titulo);
        for (Inscripcion i : inscripciones) {
            i.mostrarDatos();
        }
    }

    public final void mostrarIdentificacion() {
        System.out.println(getTipo() + ": " + titulo);
    }

    public abstract double calcularCostoMateriales();
    public abstract String getTipo();

    public String getTitulo() {
        return titulo;
    }
    public List<Inscripcion> getInscripciones() {
        return this.inscripciones; // Retorna la lista privada de inscripciones
    }
    public int getId() {
        return this.id;
    }
}

