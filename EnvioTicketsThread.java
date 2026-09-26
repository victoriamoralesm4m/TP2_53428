package hilos;

import modelo.EventoUniversitario;
import modelo.Inscripcion;
import modelo.actividades.Actividad;

public class EnvioTicketsThread extends Thread {
    private EventoUniversitario evento;

    public EnvioTicketsThread(EventoUniversitario evento) {
        this.evento = evento;
    }

    @Override
    public void run() {
        System.out.println("\n>>> [HILO SECUNDARIO INICIADO] Procesando envío masivo de tickets de acceso...");

        for (Actividad actividad : evento.getActividades()) {
            for (Inscripcion inscripcion : actividad.getInscripciones()) {
                if (inscripcion.getTicket() != null) {
                    inscripcion.getTicket().enviarTicket();

                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        System.err.println("[HILO SECUNDARIO] Interrumpido: " + e.getMessage());
                    }
                }
            }
        }

        System.out.println(">>> [HILO SECUNDARIO FINALIZADO] Todos los tickets han sido enviados con éxito.\n");
    }
}
