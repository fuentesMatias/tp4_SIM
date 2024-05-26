import lombok.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Data
public class Simulacion {
    // Parametros
    private double tiempoSimulacion;
    private List<Integer> iteracionesAMostrar;
    private double frecuenciaLlegada;
    private List<Double> probabilidadesCasos;
    private List<Double> tiemposAtencionConsulta;
    private double probabilidadLectura;
    private double tiempoPromedioLectura;

    // Variables
    private List<Cliente> capacidadTotal;
    private List<Bibliotecario> bibliotecarios;
    private Cola cola;
    private String evento;
    private int nroIteracion;
    private double reloj;
    private int llegadasTotales;
    private int llegadasFallidas;

    // Constructor con valores por defecto
    public Simulacion(Optional<Double> frecuenciaLlegada,
                      Optional<Double> tiempoSimulacion,
                      Optional<List<Integer>> iteracionesAMostrar,
                      Optional<List<Double>> probabilidadesCasos,
                      Optional<List<Double>> tiemposAtencionConsulta,
                      Optional<Double> probabilidadLectura,
                      Optional<Double> tiempoPromedioLectura) {
        this.frecuenciaLlegada = frecuenciaLlegada.orElse(4.0);
        Cola.setMediaLlegada(frecuenciaLlegada.orElse(4.0));

        this.iteracionesAMostrar = iteracionesAMostrar.orElse(getDefaultIteracionesAMostrar());
        this.tiempoSimulacion = tiempoSimulacion.orElse(100.0);

        this.probabilidadesCasos = probabilidadesCasos.orElse(getDefaultProbabilidadesCasos());
        Bibliotecario.setProbabilidadesCasos(probabilidadesCasos.orElse(getDefaultProbabilidadesCasos()));

        this.tiemposAtencionConsulta = tiemposAtencionConsulta.orElse(getDefaultTiemposAtencionConsulta());
        Bibliotecario.setTiemposAtencionConsulta(tiemposAtencionConsulta.orElse(getDefaultTiemposAtencionConsulta()));

        this.probabilidadLectura = probabilidadLectura.orElse(0.4);
        Cliente.setProbabilidadPermanecerLeyendo(probabilidadLectura.orElse(0.4));

        this.tiempoPromedioLectura = tiempoPromedioLectura.orElse(30.0);
        Cliente.setTiempoPromedioLectura(tiempoPromedioLectura.orElse(30.0));
        inicializarVariables();
    }

    // Método para inicializar las variables comunes
    private void inicializarVariables() {
        capacidadTotal = new ArrayList<Cliente>();
        bibliotecarios = new ArrayList<Bibliotecario>();
        Bibliotecario s1 = new Bibliotecario();
        Bibliotecario s2 = new Bibliotecario();
        bibliotecarios.add(s1);
        bibliotecarios.add(s2);
        cola = new Cola();
        evento = "";
        reloj = 0;
        llegadasTotales = 0;
        llegadasFallidas = 0;
    }

    private static List<Double> getDefaultProbabilidadesCasos() {
        List<Double> defaultProbabilidades = new ArrayList<>();
        defaultProbabilidades.add(0.45);
        defaultProbabilidades.add(0.45);
        defaultProbabilidades.add(0.1);
        return defaultProbabilidades;
    }

    private static List<Double> getDefaultTiemposAtencionConsulta() {
        List<Double> defaultTiempos = new ArrayList<>();
        defaultTiempos.add(2.0);
        defaultTiempos.add(5.0);
        return defaultTiempos;
    }

    private static List<Integer> getDefaultIteracionesAMostrar() {
        List<Integer> defaultIteraciones = new ArrayList<>();
        defaultIteraciones.add(1);
        defaultIteraciones.add(100);
        return defaultIteraciones;
    }
    // Inicializar
    public void inicializar() {
        cola.setProximaLlegada(1.6);
        imprimirResultados();
        nroIteracion++;
        while (reloj < tiempoSimulacion && nroIteracion < 100000) {
            switch (evento) {
                case "llegadaCliente":
                    llegadaCliente();
                case "finAtencionS1":
                    finAtencionS1();
                case "finAtencionS2":
                    finAtencionS2();
                case "finLectura":
                    finLectura();
            }
            imprimirResultados();
            calcularProximoEvento();
            nroIteracion++;
        }
        System.out.println(nroIteracion);

    }

    private void imprimirResultados() {
    }

    // Calcular próximo evento
    public void calcularProximoEvento() {
        double minimo = cola.getProximaLlegada();
        evento = "llegadaCliente";
        if (bibliotecarios.get(0).getTiempoFinAtencion() < minimo) {
            minimo = bibliotecarios.get(0).getTiempoFinAtencion();
            evento = "finAtencionS1";
        }
        if (bibliotecarios.get(1).getTiempoFinAtencion() < minimo) {
            minimo = bibliotecarios.get(1).getTiempoFinAtencion();
            evento = "finAtencionS2";
        }
        //para cada cliente que este en estado leyendo se fija si es el proximo en terminar
        for (Cliente cliente : capacidadTotal) {
            if (cliente.getTiempoSalida() < minimo && Objects.equals(cliente.getEstado(), "leyendo")) {
                minimo = cliente.getTiempoSalida();
                evento = "finLectura";
            }
        }

        reloj = minimo;

    }

    // Eventos
    public void llegadaCliente() {
        if (capacidadTotal.size() < 20) {

            Cliente cliente = new Cliente(reloj);
            cliente.setTiempoEntrada(reloj);
            capacidadTotal.add(cliente);
            llegadasTotales++;
            // Si hay bibliotecarios libres se atiende al cliente en ese bibliotecario, sino lo pone en la cola
            if (bibliotecarios.get(0).getEstado().equals("Libre")) {
                bibliotecarios.get(0).setEstado("Ocupado");
                bibliotecarios.get(0).setCliente(cliente);
                bibliotecarios.get(0).calcularTiempoFinAtencion(reloj);
                cliente.setEstado("Atendido");
            } else if (bibliotecarios.get(1).getEstado().equals("Libre")) {
                bibliotecarios.get(1).setEstado("Ocupado");
                bibliotecarios.get(1).setCliente(cliente);
                bibliotecarios.get(1).calcularTiempoFinAtencion(reloj);
                cliente.setEstado("Atendido");
            } else {
                cola.getColaAtencion().add(cliente);
                cliente.setEstado("Esperando");
            }
            cola.calcularProximaLlegada(reloj);
        } else {
            llegadasFallidas++;
            llegadasTotales++;
            cola.calcularProximaLlegada(reloj);
        }
    }

    public void finAtencionS1() {

    }

    public void finAtencionS2() {
    }

    public void finLectura() {
    }
}
