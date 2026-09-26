package modelo;

import modelo.actividades.Actividad;
import java.io.Serializable;
import java.time.LocalDate;

public class Inscripcion implements Serializable {

    private Estudiante estudiante;
    private Actividad actividad;
    private LocalDate fecha;
    private String estado;
    private TicketDeAcceso ticket; // Atributo de la clase anidada

    public Inscripcion(Estudiante estudiante, Actividad actividad) {
        this.estudiante = estudiante;
        this.actividad = actividad;
        this.fecha = LocalDate.now();
        this.estado = "Confirmada";
        this.ticket = null;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public Actividad getActividad() {
        return actividad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // OBLIGATORIO: Debe ser public para que EnvioTicketsThread lo vea
    public TicketDeAcceso getTicket() {
        return ticket;
    }

    public TicketDeAcceso generarTicket() {
        if ("Confirmada".equalsIgnoreCase(this.estado)) {
            String idTicket = "TKT-" + estudiante.getLegajo() + "-" + actividad.getId();
            this.ticket = new TicketDeAcceso(idTicket);
            return this.ticket;
        } else {
            System.out.println("No se puede generar ticket: La inscripción de " + estudiante.getNombre() + " no está confirmada.");
            return null;
        }
    }

    public void mostrarDatos() {
        System.out.println("  - " + estudiante.getNombre() + " | Fecha: " + fecha + " | Estado: " + estado);
    }

    public class TicketDeAcceso implements Serializable {
        private static final long serialVersionUID = 1L;

        private String idTicket;
        private LocalDate fechaEmision;

        public TicketDeAcceso(String idTicket) {
            this.idTicket = idTicket;
            this.fechaEmision = LocalDate.now();
        }

        public String getIdTicket() {
            return idTicket;
        }

        public LocalDate getFechaEmision() {
            return fechaEmision;
        }

        public void enviarTicket() {
            System.out.println("  [HILO DE ENVÍO] >>> Ticket " + idTicket + " enviado a "
                    + estudiante.getNombre() + " (" + estudiante.getLegajo() + ") para la actividad '"
                    + actividad.getTitulo() + "'");
        }
    }
}