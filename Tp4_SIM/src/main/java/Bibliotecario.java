import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
public class Bibliotecario {
    // Parametros
    @Setter
    private static List<Double> tiemposAtencionConsulta;
    @Setter
    private static List<Double> probabilidadesCasos;
    @Setter
    private static List<Double> limitesM; // uniforme entra A y B
    private RungeKuttaIntegration rungeKuttaIntegration;

    // atributos
    private String estado; // Libre, Ocupado
    private Cliente cliente;
    private String tipoConsulta; // pedir, devolver, socio
    private double rndCaso;
    private double rndM;
    private double M;
    private double rndTiempoAtencion;
    private double duracionAtencion;
    private double tiempoFinAtencion;

    public Bibliotecario(RungeKuttaIntegration rungeKuttaIntegration) {
        estado = "Libre";
        cliente = null;
        tipoConsulta = "";
        rndCaso = 0.0;
        rndTiempoAtencion = 0.0;
        duracionAtencion = 0;
        tiempoFinAtencion = -1;
        this.rungeKuttaIntegration = rungeKuttaIntegration;
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
            //tira un rnd para M, que va de uniforme entre A y B de los limites y segun ese M calcula el tiempo de atencion con la tabla de RK
            rndM = Math.random();
            M = limitesM.get(0) + rndM * (limitesM.get(1) - limitesM.get(0));
            duracionAtencion = rungeKuttaIntegration.getResultForM(M);
            tiempoFinAtencion = reloj + rungeKuttaIntegration.getResultForM(M);


        }
    }

    public void atenderConDevolucion(double reloj) {
        tipoConsulta = "Devolver";
        rndTiempoAtencion = Math.random();
        rndCaso = 0.0;
        duracionAtencion = 1.5 + (1.0 * rndTiempoAtencion);
        tiempoFinAtencion = reloj + duracionAtencion;
    }

    public void liberarBibliotecario() {
        estado = "Libre";
        cliente = null;
        tipoConsulta = "";
        rndCaso = 0.0;
        rndM = 0.0;
        M = 0.0;
        rndTiempoAtencion = 0.0;
        duracionAtencion = 0;
        tiempoFinAtencion = -1;
    }

    public void ocuparBibliotecario(Cliente cliente,double reloj) {
        estado = "Ocupado";
        this.cliente = cliente;
        if (cliente.getEstado().equals("EsperandoParaDevolver")) {
            atenderConDevolucion(reloj);
        }
        else
            calcularTiempoFinAtencion(reloj);
    }
}
