package modelo.actividades;
import modelo.certificacion.Certificable;
import modelo.Estudiante;

public class Taller extends Actividad implements Certificable {
        private boolean requiereNotebook;

        public Taller(int id, String titulo, int cupoMaximo, boolean requiereNotebook) {
            super(id, titulo, cupoMaximo);
            this.requiereNotebook = requiereNotebook;
        }

        @Override
        public double calcularCostoMateriales() {
            if (requiereNotebook) {
                return 5000;
            } else {
                return 2000;
            }
        }

        @Override
        public String getTipo() {
            return "Taller";
        }

    @Override
    public String generarCertificado(Estudiante estudiante) {
        return ("Certificado de asistencia("+ENTIDAD_EMISORA+"):otorgado al estudiante"+ estudiante.getNombre() + "por participar en el Taller con nombre:"+getTitulo() +".");
    }
}
