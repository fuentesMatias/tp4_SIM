import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Optional;

public class Simulador extends JFrame {
    private JPanel Main;
    private JButton simularButton;
    private JTextField probSocio;
    private JTextField probDevolver;
    private JTextField probPedir;
    private JTextField MinM;
    private JTextField frecLlegada;
    private JTextField MaxM;
    private JTextField probLectura;
    private JTable table1;
    private JTextField promLectura;
    private JTextField tiempoDesde;
    private JTextField tiempo;
    private JScrollPane scrollPanel;
    private JTextField cantFilas;
    private JTextField textPromedio;
    private JTextField porcentajeFallido;
    private JTextField h;
    private JTextField p1;
    private JTextField p2;

    public Simulador() {
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        add(Main);
        setTitle("Simulador");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Inicializar JTextFields con valores predeterminados
        frecLlegada.setText("4");
        probSocio.setText("0.1");
        probDevolver.setText("0.45");
        probPedir.setText("0.45");
        MinM.setText("6");
        MaxM.setText("21");
        probLectura.setText("0.4");
        promLectura.setText("30");
        tiempoDesde.setText("0");
        tiempo.setText("60");
        cantFilas.setText("100");
        h.setText("0.1");
        p1.setText("0.01");
        p2.setText("3.0");

        // Agregar DocumentListener a todos los JTextField
        addDocumentListener(probSocio);
        addDocumentListener(probDevolver);
        addDocumentListener(probPedir);
        addDocumentListener(MinM);
        addDocumentListener(frecLlegada);
        addDocumentListener(MaxM);
        addDocumentListener(probLectura);
        addDocumentListener(promLectura);
        addDocumentListener(tiempoDesde);
        addDocumentListener(tiempo);
        addDocumentListener(cantFilas);
        addDocumentListener(h);
        addDocumentListener(p1);
        addDocumentListener(p2);

        // Aplicar filtro de entrada a los JTextFields
        applyNumberFilter(probSocio);
        applyNumberFilter(probDevolver);
        applyNumberFilter(probPedir);
        applyNumberFilter(MinM);
        applyNumberFilter(frecLlegada);
        applyNumberFilter(MaxM);
        applyNumberFilter(probLectura);
        applyNumberFilter(promLectura);
        applyNumberFilter(tiempoDesde);
        applyNumberFilter(tiempo);
        applyNumberFilter(cantFilas);
        applyNumberFilter(h);

        // Configurar el tamaño del botón y agregar ActionListener
        simularButton.setPreferredSize(new Dimension(200, 100));
        simularButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSimularButtonClick();
            }
        });

        // Crear la tabla con el encabezado doble
        String[] columnNames = {
                "evento", "reloj", "Llegada_rnd1", "tiempo", "proxLlegada",
                "Cola Atención", "Ocupacion Actual","llegadasTotales","llegadasFallidas","Tiempos Permanencia", "S1_estado", "S1_cliente",
                "S1_rndCaso", "S1_caso","S1_rndM","S1_M", "S1_rndTiempo", "S1_duracionA",
                "S1_finAtencion", "S2_estado", "S2_cliente", "S2_rndCaso",
                "S2_caso","S2_rndM","S2_M", "S2_rndTiempo", "S2_duracionA", "S2_finAtencion", "Acum. Tiempos Permanencia", "Contador clientes at. finalizada",
                "ID","C1_estado", "C1_TiempoEntrada", "C1_rndDesicion", "C1_rndTiempoLectura","C1_TiempoLectura","C1_TiempoFinLectura",
                "ID","C2_estado", "C2_TiempoEntrada", "C2_rndDesicion", "C2_rndTiempoLectura","C2_TiempoLectura","C2_TiempoFinLectura",
                "ID","C3_estado", "C3_TiempoEntrada", "C3_rndDesicion", "C3_rndTiempoLectura","C3_TiempoLectura","C3_TiempoFinLectura",
                "ID","C4_estado", "C4_TiempoEntrada", "C4_rndDesicion", "C4_rndTiempoLectura","C4_TiempoLectura","C4_TiempoFinLectura",
                "ID","C5_estado", "C5_TiempoEntrada", "C5_rndDesicion", "C5_rndTiempoLectura","C5_TiempoLectura","C5_TiempoFinLectura",
                "ID","C6_estado", "C6_TiempoEntrada", "C6_rndDesicion", "C6_rndTiempoLectura","C6_TiempoLectura","C6_TiempoFinLectura",
                "ID","C7_estado", "C7_TiempoEntrada", "C7_rndDesicion", "C7_rndTiempoLectura","C7_TiempoLectura","C7_TiempoFinLectura",
                "ID","C8_estado", "C8_TiempoEntrada", "C8_rndDesicion", "C8_rndTiempoLectura","C8_TiempoLectura","C8_TiempoFinLectura",
                "ID","C9_estado", "C9_TiempoEntrada", "C9_rndDesicion", "C9_rndTiempoLectura","C9_TiempoLectura","C9_TiempoFinLectura",
                "ID","C10_estado", "C10_TiempoEntrada", "C10_rndDesicion", "C10_rndTiempoLectura","C10_TiempoLectura","C10_TiempoFinLectura",
                "ID","C11_estado", "C11_TiempoEntrada", "C11_rndDesicion", "C11_rndTiempoLectura","C11_TiempoLectura","C11_TiempoFinLectura",
                "ID","C12_estado", "C12_TiempoEntrada", "C12_rndDesicion", "C12_rndTiempoLectura","C12_TiempoLectura","C12_TiempoFinLectura",
                "ID","C13_estado", "C13_TiempoEntrada", "C13_rndDesicion", "C13_rndTiempoLectura","C13_TiempoLectura","C13_TiempoFinLectura",
                "ID","C14_estado", "C14_TiempoEntrada", "C14_rndDesicion", "C14_rndTiempoLectura","C14_TiempoLectura","C14_TiempoFinLectura",
                "ID","C15_estado", "C15_TiempoEntrada", "C15_rndDesicion", "C15_rndTiempoLectura","C15_TiempoLectura","C15_TiempoFinLectura",
                "ID","C16_estado", "C16_TiempoEntrada", "C16_rndDesicion", "C16_rndTiempoLectura","C16_TiempoLectura","C16_TiempoFinLectura",
                "ID","C17_estado", "C17_TiempoEntrada", "C17_rndDesicion", "C17_rndTiempoLectura","C17_TiempoLectura","C17_TiempoFinLectura",
                "ID","C18_estado", "C18_TiempoEntrada", "C18_rndDesicion", "C18_rndTiempoLectura","C18_TiempoLectura","C18_TiempoFinLectura",
                "ID","C19_estado", "C19_TiempoEntrada", "C19_rndDesicion", "C19_rndTiempoLectura","C19_TiempoLectura","C19_TiempoFinLectura",
                "ID","C20_estado", "C20_TiempoEntrada", "C20_rndDesicion", "C20_rndTiempoLectura","C20_TiempoLectura","C20_TiempoFinLectura"
        };

        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Todas las celdas no son editables
            }
        };
        table1.setModel(model);

        // Aplicar el renderizador personalizado a las columnas
        DefaultTableCellRenderer customRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Centrando el texto en las celdas
                setHorizontalAlignment(SwingConstants.CENTER);

                // Aplicar color de fondo basado en el índice de la columna
                if (column >= 0 && column <= 1) {
                    c.setBackground(Color.DARK_GRAY);
                } else if (column >= 2 && column <= 4) {
                    c.setBackground(new Color(29, 118, 52));
                } else if (column >= 5 && column <= 9) {
                    c.setBackground(Color.DARK_GRAY);
                } else if (column >= 10 && column <= 18) {
                    c.setBackground(new Color(29, 34, 131));
                } else if (column >= 19 && column <= 27) {
                    c.setBackground(new Color(109, 32, 125));
                } else {
                    // Alternar colores cada 7 columnas a partir de la columna 21
                    if ((column - 28) / 7 % 2 == 0) {
                        c.setBackground(new Color(109, 93, 37));
                    } else {
                        c.setBackground(new Color(38, 131, 98));
                    }
                }
                //pintar de rojo las letras de la fila seleccionada
                if (isSelected) {
                    c.setForeground(Color.RED);
                } else {
                    c.setForeground(Color.WHITE);
                }
                return c;
            }
        };

        for (int i = 0; i < table1.getColumnCount(); i++) {
            table1.getColumnModel().getColumn(i).setCellRenderer(customRenderer);
        }

        table1.setIntercellSpacing(new Dimension(1, 1));
        table1.setShowGrid(true);
        table1.setGridColor(Color.BLACK);

        scrollPanel.setViewportView(table1);
        scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        setVisible(true);
    }

    private void applyNumberFilter(JTextField textField) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter() {
            private final String regex = "\\d*\\.?\\d*";

            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string.isEmpty() || string.matches(regex)) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text.isEmpty() || text.matches(regex)) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            @Override
            public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
                super.remove(fb, offset, length);
            }
        });
    }

    private void addDocumentListener(JTextField textField) {
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                onTextChange();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                onTextChange();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                onTextChange();
            }
        });
    }

    private void onTextChange() {
        try {
            double probSocioValue = Double.parseDouble(probSocio.getText());
            double probDevolverValue = Double.parseDouble(probDevolver.getText());
            double probPedirValue = Double.parseDouble(probPedir.getText());
            double tiempoMinValue = Double.parseDouble(MinM.getText());
            double tiempoMaxValue = Double.parseDouble(MaxM.getText());
            double probLecturaValue = Double.parseDouble(probLectura.getText());
            double promLecturaValue = Double.parseDouble(promLectura.getText());
            double frecLlegadaValue = Double.parseDouble(frecLlegada.getText());
            double tiempoDesdeValue = Double.parseDouble(tiempoDesde.getText());
            double tiempoValue = Double.parseDouble(tiempo.getText());
            int cantFilasValue = Integer.parseInt(cantFilas.getText());
            double hValue = Double.parseDouble(h.getText());
            double p1Value = Double.parseDouble(p1.getText());
            double p2Value = Double.parseDouble(p2.getText());


            if (hValue <= 0 ) {
                simularButton.setEnabled(false);
                return;
            }
            if (probSocioValue + probDevolverValue + probPedirValue != 1) {
                simularButton.setEnabled(false);
                return;
            }

            if (tiempoMinValue < 0 || tiempoMaxValue < 0 || tiempoMinValue >= tiempoMaxValue) {
                simularButton.setEnabled(false);
                return;
            }

            if (frecLlegadaValue <= 0 || probLecturaValue <= 0 || probLecturaValue >= 1 || promLecturaValue <= 0) {
                simularButton.setEnabled(false);
                return;
            }

            if (tiempoDesdeValue < 0 || tiempoValue <= 0 || cantFilasValue <= 0) {
                simularButton.setEnabled(false);
                return;
            }

            if (tiempoDesdeValue > tiempoValue) { // Permitir 0 como valor válido para tiempoDesde
                simularButton.setEnabled(false);
                return;
            }

            simularButton.setEnabled(true);
        } catch (NumberFormatException e) {
            simularButton.setEnabled(false);
        }
    }

    private void onSimularButtonClick() {
        // crea un rungekutta con los valores de los JTextField y luego lo integra

        // OBTIENE LOS PARAMETROS DE LA SIMULACION
        double alpha = Double.parseDouble(p1.getText());
        double beta = Double.parseDouble(p2.getText());
        double H = Double.parseDouble(h.getText());
        double M_max = Double.parseDouble(MaxM.getText());
        RungeKuttaIntegration rkIntegration = new RungeKuttaIntegration(alpha, beta, H, M_max);



        // Crear una simulación con los valores de los JTextField y luego llenar la tabla con la matriz
        Simulacion simulacion = new Simulacion(
                Optional.of(Double.parseDouble(frecLlegada.getText())),
                Optional.of(Double.parseDouble(tiempo.getText())),
                Optional.of(Double.parseDouble(tiempoDesde.getText())),
                Optional.of(Integer.parseInt(cantFilas.getText())),
                Optional.of(List.of(Double.parseDouble(probPedir.getText()),Double.parseDouble(probDevolver.getText()),Double.parseDouble(probSocio.getText()))),
                Optional.of(List.of(Double.parseDouble(MinM.getText()), Double.parseDouble(MaxM.getText()))),
                Optional.of(Double.parseDouble(probLectura.getText())),
                Optional.of(Double.parseDouble(promLectura.getText())),
                rkIntegration,
                Optional.of(List.of(6.0,21.0))
        );

        // Asumimos que la simulación devuelve una matriz de resultados de tipo List<List<Object>>
        List<List<Object>> resultados = simulacion.simular();

        // Limpiar la tabla actual
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setRowCount(0);

        // Rellenar la tabla con los nuevos datos
        for (List<Object> fila : resultados) {
            model.addRow(fila.toArray());
        }
        //calcular el porcentaje de llegadas fallidas
        int llegadasTotales = simulacion.getLlegadasTotales();
        int llegadasFallidas = simulacion.getLlegadasFallidas();
        double porcentajeLlegadasFallidas = (double) llegadasFallidas / llegadasTotales * 100;
        porcentajeFallido.setText(String.format("%.2f", porcentajeLlegadasFallidas) + "%");

        //calcular el tiempo de permanencia promedio
        double tiempoPromedioPermanencia = simulacion.getTiemposDePermanencia().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        textPromedio.setText(String.format("%.2f", tiempoPromedioPermanencia));


        // Mostrar una nueva ventana con el resultado de la integración
        Integracion integracionWindow = new Integracion(rkIntegration.getResults());
        integracionWindow.setVisible(true); // Mostrar la ventana integracionWindow
    }

    public static void main(String[] args) {
        FlatDarculaLaf.setup();
        new Simulador();
    }
}
