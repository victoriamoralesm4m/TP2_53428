package modelo;

import modelo.actividades.Actividad;
import modelo.actividades.Charla;
import modelo.actividades.Taller;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EventoUniversitario implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String id;
    private String titulo;
    private double costoBase;
    private boolean gratuito;
    private Sala sala;
    private static int cantidadEventos;
    private List<Actividad> actividades = new ArrayList<>();

    // Constructor
    public EventoUniversitario(String id, String titulo, double costoBase, boolean gratuito) {
        this.id = id;
        this.titulo = titulo;
        this.costoBase = costoBase;
        this.gratuito = gratuito;
        cantidadEventos++;
    }

    // Constructor copia
    public EventoUniversitario(EventoUniversitario otro) {
        this.id = otro.id;
        this.titulo = otro.titulo;
        this.costoBase = otro.costoBase;
        this.gratuito = otro.gratuito;
        cantidadEventos++;
    }

    public double calcularCostoEstimado() {
        if (gratuito) {
            return 0.0;
        } else {
            double costoActividades = 0;
            for (Actividad a : actividades) {
                costoActividades += a.calcularCostoMateriales();
            }
            return (costoBase + costoActividades) * 1.21;
        }
    }


    public void asignarSala(Sala sala1) {
        this.sala = sala1;
    }

    public Actividad crearActividad(int id, String titulo, int cupo, String tipo, String explicador, boolean requiereNotebook) {
        Actividad nuevaActividad;

        if (tipo.equals("Charla")) {
            nuevaActividad = new Charla(id, titulo, cupo, explicador);
        } else if (tipo.equals("Taller")) {
            nuevaActividad = new Taller(id, titulo, cupo, requiereNotebook);
        } else {
            System.out.println("Tipo de actividad no reconocido: " + tipo);
            return null;
        }

        actividades.add(nuevaActividad);
        return nuevaActividad;
    }


    public void mostrarDatos() {
        if (gratuito) {
            System.out.println("ID:" + id + " Titulo:" + titulo + " Evento gratuito:" + gratuito);
        } else {
            System.out.println("ID:" + id + ", Titulo:" + titulo + " Costo Base:" + costoBase);
        }
        System.out.println("Costo estimado total: " + calcularCostoEstimado());
        System.out.println("Sala: " + (sala != null ? sala.getNombre() : "sin asignar"));
        for (Actividad a : actividades) {
            System.out.println("  - Actividad: " + a.getTitulo() + " (" + a.getTipo() + ")");
        }
    }

    public static int getCantidadEventos() {
        return cantidadEventos;
    }

    public void persistirEvento() throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream("evento_" + this.id + ".ser");
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(this);
        }
    }
    public EventoUniversitario recuperarEvento(String id) throws IOException, ClassNotFoundException {
        try (FileInputStream fileIn = new FileInputStream("evento_" + id + ".ser");
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            return (EventoUniversitario) in.readObject();
        }
    }

    public String getId() {
        return this.id;
    }

    public List<Actividad> getActividades() {
        return this.actividades;
    }

    public <T extends Actividad> List<T> filtrarActividadesPorTipo(Class<T> tipo) {
        List<T> resultado = new ArrayList<>();
        for (Actividad actividad : actividades) {
            if (tipo.isInstance(actividad)) {
                resultado.add(tipo.cast(actividad));
            }
        }
        return resultado;
    }

    public double calcularCostoMateriales(List<? extends Actividad> actividades) {
        double costoTotal = 0.0;
        for (Actividad act : actividades) {
            costoTotal += act.calcularCostoMateriales();
        }
        return costoTotal;
    }
}