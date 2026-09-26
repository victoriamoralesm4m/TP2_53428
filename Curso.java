package modelo.actividades;
import modelo.certificacion.Certificable;
import modelo.Estudiante;

public class Curso extends Actividad implements Certificable {
    private int nivel;
//constructor
    public Curso(int id, String titulo, int cupoMaximo, int nivel) {
        super(id, titulo, cupoMaximo);
        this.nivel = nivel;
    }
    public int getNivel() {
        return nivel;
    }
    @Override
    public double calcularCostoMateriales() {
        return 5000.0; // Valor representativo de materiales
    }

    @Override
    public String getTipo() {
        return "Curso";
    }
    @Override
    public String generarCertificado(Estudiante estudiante) {
        return "CERTIFICADO DE ASISTENCIA (" + ENTIDAD_EMISORA + "): Otorgado a "
                + estudiante.getNombre() + " por completar el Curso '"
                + getTitulo() + "' (Nivel " + nivel + ").";
    }
}
