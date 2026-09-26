package modelo.certificacion;
import modelo.Estudiante;

public interface Certificable {
    String ENTIDAD_EMISORA="UTN";
    String generarCertificado(Estudiante estudiante);
}
