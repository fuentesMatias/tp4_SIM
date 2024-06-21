import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class RungeKuttaIntegration {
    private double h; // Paso de integración
    private double M_max; // Límite superior de M
    private List<Double[]> results; // Lista para almacenar los resultados
    private double alpha; // Coeficiente para M^2
    private double beta;  // Término constante

    public RungeKuttaIntegration(double alpha, double beta, double h, double M_max) {
        this.alpha = alpha;
        this.beta = beta;
        this.h = h;
        this.M_max = M_max;
        results = new ArrayList<>();
        integrate(); // Integrar al instanciar la clase
    }

    // Método para la integración numérica usando Runge-Kutta de 4to orden
    public void integrate() {
        double t = 0.0;
        double M = 0.0; // Valor inicial de M

        while (M < M_max || results.isEmpty() || results.get(results.size() - 1)[1] < M_max) {
            double M_prime = dMdt(M);
            double k1 = h * M_prime;
            double k2 = h * dMdt(M + 0.5 * k1);
            double k3 = h * dMdt(M + 0.5 * k2);
            double k4 = h * dMdt(M + k3);
            double M_next = M + (k1 + 2 * k2 + 2 * k3 + k4) / 6.0;

            // Redondear los valores a 5 decimales
            t = Math.round(t * 1e5) / 1e5;
            M = Math.round(M * 1e5) / 1e5;
            M_prime = Math.round(M_prime * 1e5) / 1e5;
            k1 = Math.round(k1 * 1e5) / 1e5;
            k2 = Math.round(k2 * 1e5) / 1e5;
            k3 = Math.round(k3 * 1e5) / 1e5;
            k4 = Math.round(k4 * 1e5) / 1e5;
            M_next = Math.round(M_next * 1e5) / 1e5;

            Double[] row = new Double[]{t, M, M_prime, k1, k2, k3, k4, M_next};
            results.add(row);

            M = M_next;
            t += h;
        }
    }

    // Método para calcular la derivada dM/dt
    private double dMdt(double M) {
        return alpha * M * M + beta;
    }

    // Método para imprimir los resultados con 5 decimales
    public void printResults() {
        System.out.println("t\tM\tM'\tk1\tk2\tk3\tk4\tM_next");
        for (Double[] row : results) {
            System.out.printf("%.5f\t%.5f\t%.5f\t%.5f\t%.5f\t%.5f\t%.5f\t%.5f%n",
                    row[0], row[1], row[2], row[3], row[4], row[5], row[6], row[7]);
        }
    }

    // Método para obtener el valor de t correspondiente a un M dado
    public Double getResultForM(double M) {
        for (Double[] row : results) {
            if (row[1] >= M) {
                return row[0];
            }
        }
        return null;
    }

    public static void main(String[] args) {
        // Crear una instancia con valores parametrizables para alpha, beta, h y M_max
        double alpha = 0.01;
        double beta = 3;
        double h = 0.1;
        double M_max = 25.0;
        RungeKuttaIntegration rkIntegration = new RungeKuttaIntegration(alpha, beta, h, M_max);

        rkIntegration.integrate(); // Integrar con el valor inicial de M = 0
        rkIntegration.printResults();

        Double resultForM = rkIntegration.getResultForM(10.0);
        if (resultForM != null) {
            // Imprimir el valor de t correspondiente a M = 10
            System.out.printf("t para M >= 10: %.5f%n", resultForM);
        } else {
            System.out.println("No result found for M >= 10");
        }
    }
}
