import modelo.certificacion.Certificable;
import excepciones.CupoExcedidoException;
import hilos.EnvioTicketsThread;
import modelo.Estudiante;
import modelo.EventoUniversitario;
import modelo.Inscripcion;
import modelo.Sala;
import modelo.actividades.Actividad;
import modelo.actividades.Charla;
import modelo.actividades.Taller;
import modelo.actividades.Curso;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {

        System.out.println("INICIALIZANDO DATOS DEL SISTEMA");

        // a
        List<Estudiante> estudiantes = new ArrayList<>();
        Estudiante e1 = new Estudiante("50001", "Juan Pedriel");
        Estudiante e2 = new Estudiante("50002", "María López");
        Estudiante e3 = new Estudiante("50003", "Pablo Gómez");
        estudiantes.add(e1);
        estudiantes.add(e2);
        estudiantes.add(e3);

        // b
        EventoUniversitario evento1 = new EventoUniversitario("EV001", "Jornada de Programación", 10000, false);
        EventoUniversitario evento2 = new EventoUniversitario("EV002", "Jornada Principal UTN", 0, true);

        // c
        Sala sala1 = new Sala(1, "Aula Lab 3");
        Sala sala2 = new Sala(2, "Aula Magna");
        evento1.asignarSala(sala1);
        evento2.asignarSala(sala2);

        // d
        Charla charla1 = new Charla(1, "Charla de Java Moderno", 1, "Prof. García");
        Charla charla2 = new Charla(2, "Charla de IA Generativa", 20, "Dr. Pérez");
        Taller taller1 = new Taller(3, "Taller de Testing Unitario", 10, true);
        Curso curso1 = new Curso(4, "Curso de Patrones de Diseño", 15, 2); // Nivel 2
        Curso curso2 = new Curso(5, "Curso de Arquitectura Software", 12, 3); // Nivel 3

        evento1.getActividades().add(charla1);
        evento1.getActividades().add(charla2);
        evento1.getActividades().add(taller1);
        evento1.getActividades().add(curso1);
        evento1.getActividades().add(curso2);


        System.out.println("CASO EXITOSO (INSCRIPCIÓN Y PERSISTENCIA)");
        try {
            System.out.println("Inscribiendo alumno en Charla (Ocupa cupo 1/1)...");
            Inscripcion i1 = charla1.inscribir(e1);

            System.out.println("Inscribiendo alumnos en Taller y Curso...");
            Inscripcion i2 = taller1.inscribir(e2);
            Inscripcion i3 = taller1.inscribir(e3);
            Inscripcion i4 = curso1.inscribir(e1);
            curso1.inscribir(e2);

            // Generación de tickets (Clase Anidada TicketDeAcceso)
            System.out.println("GENERANDO TICKETS DE ACCESO");
            i1.generarTicket();
            i2.generarTicket();
            i3.generarTicket();
            i4.generarTicket();

            System.out.println("Guardando evento1 en disco vía serialización...");
            evento1.persistirEvento();
            System.out.println("Evento persistido con éxito.");

            System.out.println("Recuperando evento1 desde el archivo...");

            EventoUniversitario copiaDesdeArchivo = evento1.recuperarEvento(evento1.getId());
            System.out.println("Evento recuperado correctamente desde archivo.");

            System.out.println("Datos del evento recuperado");
            copiaDesdeArchivo.mostrarDatos();

        } catch (CupoExcedidoException e) {
            System.err.println("No hay cupo disponible: " + e.getMessage());
        } catch (FileNotFoundException e) {
            System.err.println("No se encontró el archivo del evento: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("No se pudo reconstruir el objeto almacenado: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Falla de entrada/salida durante la persistencia: " + e.getMessage());
        } finally {
            System.out.println("Finalizó el bloque de ejecución de la Prueba 1");
        }

        System.out.println("EXCEPCIÓN DE CUPO EXCEDIDO");
        try {
            System.out.println("Intentando inscribir a un segundo estudiante en 'Charla de Java Moderno,Cupo max:1...");
            charla1.inscribir(e2);
            evento1.persistirEvento();

        } catch (CupoExcedidoException e) {
            System.out.println("Excepción de cupo: " + e.getMessage());
        } catch (FileNotFoundException e) {
            System.out.println("No se encontró el archivo: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error de entrada/salida: " + e.getMessage());
        } finally {
            System.out.println("Finalizó el bloque de ejecución de la Prueba 2 ");
        }


        System.out.println("EJECUCIÓN CONCURRENTE");

        EnvioTicketsThread hiloEnvio = new EnvioTicketsThread(evento1);
        hiloEnvio.start();

        System.out.println("¿El programa principal sigue ejecutándose mientras se envían los tickets...");
        for (int i = 1; i <= 3; i++) {
            System.out.println("Consultando datos en segundo plano... paso " + i);
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            hiloEnvio.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        System.out.println("CERTIFICADOS DE ASISTENCIA EMITIDOS");

        for (Actividad actividad : evento1.getActividades()) {
            if (actividad instanceof Certificable) {
                Certificable actividadCertificable = (Certificable) actividad;

                for (Inscripcion inscripcion : actividad.getInscripciones()) {
                    Estudiante alumno = inscripcion.getEstudiante();
                    String certificado = actividadCertificable.generarCertificado(alumno);
                    System.out.println("CERTIFICADO " + certificado);
                }
            } else {
                System.out.println("La actividad '" + actividad.getTitulo() + "' (" + actividad.getTipo() + ") NO emite certificados.");
            }
        }

        System.out.println("FILTRADO CON GENÉRICOS Y COSTO ");

        List<Charla> listaCharlas = evento1.filtrarActividadesPorTipo(Charla.class);
        List<Taller> listaTalleres = evento1.filtrarActividadesPorTipo(Taller.class);
        List<Curso> listaCursos = evento1.filtrarActividadesPorTipo(Curso.class);

        System.out.println("CANTIDAD DE ACTIVIDADES CREADAS POR TIPO");
        System.out.println("Charlas creadas: " + listaCharlas.size());
        System.out.println("Talleres creados: " + listaTalleres.size());
        System.out.println("Cursos creados: " + listaCursos.size());

        System.out.println("COSTO DE MATERIALES POR TIPO DE ACTIVIDAD");
        double costoCharlas = evento1.calcularCostoMateriales(listaCharlas);
        double costoTalleres = evento1.calcularCostoMateriales(listaTalleres);
        double costoCursos = evento1.calcularCostoMateriales(listaCursos);
        double costoTotalEvento = evento1.calcularCostoMateriales(evento1.getActividades());

        System.out.println("Costo materiales (Charlas): $" + costoCharlas);
        System.out.println("Costo materiales (Talleres): $" + costoTalleres);
        System.out.println("Costo materiales (Cursos): $" + costoCursos);
        System.out.println("Costo materiales total del evento: $" + costoTotalEvento);

        System.out.println("EVIDENCIA DE LISTAS CORRECTAMENTE TIPADAS");
        for (Curso c : listaCursos) {
            System.out.println("Curso: " + c.getTitulo() + " | Nivel: " + c.getNivel());
        }

        System.out.println("RESUMEN DE EVENTOS CREADOS");
        evento1.mostrarDatos();
        System.out.println("...");
        evento2.mostrarDatos();

        System.out.println("cantidad total de eventos universitarios registrados: " + EventoUniversitario.getCantidadEventos());
    }
}