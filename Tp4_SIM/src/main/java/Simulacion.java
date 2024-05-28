import lombok.Data;

import java.util.*;

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
    private List<Double> tiemposDePermanencia;

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
        tiemposDePermanencia = new ArrayList<>();
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
    public void simular() {
        cola.setProximaLlegada(1.6);
        imprimirResultados();
        calcularProximoEvento();
        nroIteracion++;
        while (reloj < tiempoSimulacion && nroIteracion < 100000) {
            switch (evento) {
                case "llegadaCliente":
                    llegadaCliente();
                    break;
                case "finAtencionS1":
                    finAtencionS1();
                    break;
                case "finAtencionS2":
                    finAtencionS2();
                    break;
                case "finLectura":
                    finLectura();
                    break;
            }
            imprimirResultados();
            calcularProximoEvento();
            nroIteracion++;
        }

    }

    private void imprimirResultados() {
            // Ensure locale is set to US to use dot as decimal separator
        Locale.setDefault(Locale.US);

        System.out.println(capacidadTotal.size());
        }

    // Calcular próximo evento
    public void calcularProximoEvento() {
        double minimo = cola.getProximaLlegada();
        evento = "llegadaCliente";
        if (bibliotecarios.get(0).getTiempoFinAtencion() < minimo && bibliotecarios.get(0).getTiempoFinAtencion() != -1){
            minimo = bibliotecarios.get(0).getTiempoFinAtencion();
            evento = "finAtencionS1";
        }
        if (bibliotecarios.get(1).getTiempoFinAtencion() < minimo && bibliotecarios.get(1).getTiempoFinAtencion() != -1){
            minimo = bibliotecarios.get(1).getTiempoFinAtencion();
            evento = "finAtencionS2";
        }
        //para cada cliente que este en estado leyendo se fija si es el proximo en terminar
        for (Cliente cliente : capacidadTotal) {
            if (cliente.getTiempoSalida() < minimo && Objects.equals(cliente.getEstado(), "leyendo") && cliente.getTiempoSalida() != -1) {
                minimo = cliente.getTiempoSalida();
                evento = "finLectura";
            }
        }

        reloj = minimo;

    }

    // Eventos
    public void llegadaCliente() {
        if (capacidadTotal.size() < 20) {
            //si la capacidad es menor a 20 se agrega un cliente
            Cliente cliente = new Cliente(reloj);
            //se agrega el cliente a la biblioteca, dsp se decide a donde va
            capacidadTotal.add(cliente);
            llegadasTotales++;

            // Si hay bibliotecarios libres se atiende al cliente en ese bibliotecario, sino lo pone en la cola
            if (bibliotecarios.get(0).getEstado().equals("Libre")) {
                bibliotecarios.get(0).ocuparBibliotecario(cliente, reloj); // setea y calcula el tiempo de fin de atencion
                cliente.setEstado("Atendido");
            } else if (bibliotecarios.get(1).getEstado().equals("Libre")) {
                bibliotecarios.get(1).ocuparBibliotecario(cliente, reloj); // setea y calcula el tiempo de fin de atencion
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
        Cliente cliente = bibliotecarios.get(0).getCliente();
        //cuando termina de antender a un cliente, primero se verifica si ese cliente pidio un libro
        if(Objects.equals(bibliotecarios.get(0).getTipoConsulta(), "Pedir")){
            //si pidio un libro, se calcula si se queda o se va
            if (cliente.calcularTiempoSalidaSiPidio(reloj)){ // En este metodo se setea el tiempo ya sea si se va o se queda
                //si se queda, se cambia el estado a leyendo, y se libera al bibliotecario
                cliente.setEstado("leyendo");
                bibliotecarios.get(0).liberarBibliotecario();
            }
            else{
                //si se va, se libera al bibliotecario y se elimina al cliente de la biblioteca y se guarda el tiempo de permanencia
                tiemposDePermanencia.add(cliente.calcularTiempoPermancencia());
                capacidadTotal.remove(cliente);
                bibliotecarios.get(0).liberarBibliotecario();
            }
        }
        else{
            //si no pidio un libro, se libera al bibliotecario, y se saca al cliente de la biblioteca y se guarda el tiempo de permanencia
            cliente.setTiempoSalida(reloj); //Seteo el tiempo de salida en el actual
            tiemposDePermanencia.add(cliente.calcularTiempoPermancencia()); //Guardo el tiempo de permanencia
            capacidadTotal.remove(cliente);//Saco al cliente de la biblioteca
            bibliotecarios.get(0).liberarBibliotecario();
        }

        //Ahora si hay gente en la cola, se atiende al proximo
        if (!cola.getColaAtencion().isEmpty()) { //Si hay gente en la cola
            Cliente next = cola.getColaAtencion().get(0);
            cola.getColaAtencion().remove(0);
            bibliotecarios.get(0).ocuparBibliotecario(next, reloj);
            next.setEstado("Atendido");
        }

    }

    public void finAtencionS2() {
        Cliente cliente = bibliotecarios.get(1).getCliente();
        //cuando termina de antender a un cliente, primero se verifica si ese cliente pidio un libro
        if(Objects.equals(bibliotecarios.get(1).getTipoConsulta(), "Pedir")){
            //si pidio un libro, se calcula si se queda o se va
            if (cliente.calcularTiempoSalidaSiPidio(reloj)){ // En este metodo se setea el tiempo ya sea si se va o se queda
                //si se queda, se cambia el estado a leyendo, y se libera al bibliotecario
                cliente.setEstado("leyendo");
                bibliotecarios.get(1).liberarBibliotecario();
            }
            else{
                //si se va, se libera al bibliotecario y se elimina al cliente de la biblioteca y se guarda el tiempo de permanencia
                tiemposDePermanencia.add(cliente.calcularTiempoPermancencia());
                capacidadTotal.remove(cliente);
                bibliotecarios.get(1).liberarBibliotecario();
            }
        }
        else{
            //si no pidio un libro, se libera al bibliotecario, y se saca al cliente de la biblioteca y se guarda el tiempo de permanencia
            cliente.setTiempoSalida(reloj); //Seteo el tiempo de salida en el actual
            tiemposDePermanencia.add(cliente.calcularTiempoPermancencia()); //Guardo el tiempo de permanencia
            capacidadTotal.remove(cliente);//Saco al cliente de la biblioteca
            bibliotecarios.get(1).liberarBibliotecario();
        }

        //Ahora si hay gente en la cola, se atiende al proximo
        if (!cola.getColaAtencion().isEmpty()) { //Si hay gente en la cola
            Cliente next = cola.getColaAtencion().get(0);
            cola.getColaAtencion().remove(0);
            bibliotecarios.get(1).ocuparBibliotecario(next, reloj);
            next.setEstado("Atendido");
        }
    }

    public void finLectura() {
        //Busca al cliente que termino de leer por el tiempo del reloj actual
        Cliente cliente = null;
        for (Cliente c : capacidadTotal) {
            if (c.getTiempoSalida() == reloj && Objects.equals(c.getEstado(), "leyendo")) {
                cliente = c;
                break;
            }
        }
        // Si termino de leer lo guarda el tiempo de permanencia y lo saca de la biblioteca
        if (cliente != null) {
            tiemposDePermanencia.add(cliente.calcularTiempoPermancencia());
            capacidadTotal.remove(cliente);
        }
    }
}
