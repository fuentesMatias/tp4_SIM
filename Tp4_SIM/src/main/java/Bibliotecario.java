import lombok.Data;

import java.util.List;

@Data
public class Bibliotecario {
    // Parametros
    private static List<Double> tiemposAtencionConsulta;
    private static List<Double> probabilidadesCasos;

    // atributos
    private String estado; // Libre, Ocupado
    private Cliente cliente;
    private String tipoConsulta; // pedir, devolver, socio
    private double rndCaso;
    private double rndTiempoAtencion;
    private double duracionAtencion;
    private double tiempoFinAtencion;

    public Bibliotecario() {
        estado = "Libre";
        cliente = null;
        tipoConsulta = "";
        rndCaso = 0.0;
        rndTiempoAtencion = 0.0;
        duracionAtencion = 0;
        tiempoFinAtencion = -1;
    }

    public static void setProbabilidadesCasos(List<Double> doubles) {
        probabilidadesCasos = doubles;
    }

    public static void setTiemposAtencionConsulta(List<Double> doubles) {
        tiemposAtencionConsulta = doubles;
    }

    public void calcularTiempoFinAtencion(double reloj) {
        //generar un rnd con 4 decimales
        rndTiempoAtencion = Math.random();
        rndCaso = Math.random();
        if (rndCaso < probabilidadesCasos.get(0)) {// Pedir
            tipoConsulta = "Pedir";
            duracionAtencion = -6.0 * Math.log(1 - rndTiempoAtencion);
            tiempoFinAtencion = reloj + duracionAtencion;
        }
        else if (rndCaso >= probabilidadesCasos.get(0) && rndCaso < probabilidadesCasos.get(0)+probabilidadesCasos.get(1)) { // Devolver
            tipoConsulta = "Devolver";
            duracionAtencion = 1.5 + (1.0 * rndTiempoAtencion);
            tiempoFinAtencion = reloj + duracionAtencion;
        }
        else{
            tipoConsulta = "Socio";
            // duracion es una dist uniforme con min=tiemposAtencionConsulta.get(0) y max=tiemposAtencionConsulta.get(1)
            duracionAtencion = tiemposAtencionConsulta.get(0) + (tiemposAtencionConsulta.get(1) - tiemposAtencionConsulta.get(0)) * rndTiempoAtencion;
            tiempoFinAtencion = reloj + duracionAtencion;
        }
    }

    public void liberarBibliotecario() {
        estado = "Libre";
        cliente = null;
        tipoConsulta = "";
        rndCaso = 0.0;
        rndTiempoAtencion = 0.0;
        duracionAtencion = 0;
        tiempoFinAtencion = -1;
    }

    public void ocuparBibliotecario(Cliente cliente,double reloj) {
        estado = "Ocupado";
        this.cliente = cliente;
        calcularTiempoFinAtencion(reloj);
    }
}
