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
    private JTextField tiempoMin;
    private JTextField frecLlegada;
    private JTextField tiempoMax;
    private JTextField probLectura;
    private JTable table1;
    private JTextField promLectura;
    private JTextField tiempoDesde;
    private JTextField tiempo;
    private JScrollPane scrollPanel;
    private JTextField cantFilas;
    private JTextField textPromedio;
    private JTextField porcentajeFallido;

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
        tiempoMin.setText("2");
        tiempoMax.setText("5");
        probLectura.setText("0.4");
        promLectura.setText("30");
        tiempoDesde.setText("0");
        tiempo.setText("60");
        cantFilas.setText("100");

        // Agregar DocumentListener a todos los JTextField
        addDocumentListener(probSocio);
        addDocumentListener(probDevolver);
        addDocumentListener(probPedir);
        addDocumentListener(tiempoMin);
        addDocumentListener(frecLlegada);
        addDocumentListener(tiempoMax);
        addDocumentListener(probLectura);
        addDocumentListener(promLectura);
        addDocumentListener(tiempoDesde);
        addDocumentListener(tiempo);
        addDocumentListener(cantFilas);

        // Aplicar filtro de entrada a los JTextFields
        applyNumberFilter(probSocio);
        applyNumberFilter(probDevolver);
        applyNumberFilter(probPedir);
        applyNumberFilter(tiempoMin);
        applyNumberFilter(frecLlegada);
        applyNumberFilter(tiempoMax);
        applyNumberFilter(probLectura);
        applyNumberFilter(promLectura);
        applyNumberFilter(tiempoDesde);
        applyNumberFilter(tiempo);
        applyNumberFilter(cantFilas);

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
                "Cola Atención", "Ocupacion Actual","llegadasTotales","llegadasFallidas", "S1_estado", "S1_cliente",
                "S1_rndCaso", "S1_caso", "S1_rndTiempo", "S1_duracionA",
                "S1_finAtencion", "S2_estado", "S2_cliente", "S2_rndCaso",
                "S2_caso", "S2_rndTiempo", "S2_duracionA", "S2_finAtencion",
                "ID","C1_estado", "C1_TiempoEntrada", "C1_rndDesicion", "C1_rndTiempoLectura","C1_TiempoLectura","C1_TiempoSalida",
                "ID","C2_estado", "C2_TiempoEntrada", "C2_rndDesicion", "C2_rndTiempoLectura","C2_TiempoLectura","C2_TiempoSalida",
                "ID","C3_estado", "C3_TiempoEntrada", "C3_rndDesicion", "C3_rndTiempoLectura","C3_TiempoLectura","C3_TiempoSalida",
                "ID","C4_estado", "C4_TiempoEntrada", "C4_rndDesicion", "C4_rndTiempoLectura","C4_TiempoLectura","C4_TiempoSalida",
                "ID","C5_estado", "C5_TiempoEntrada", "C5_rndDesicion", "C5_rndTiempoLectura","C5_TiempoLectura","C5_TiempoSalida",
                "ID","C6_estado", "C6_TiempoEntrada", "C6_rndDesicion", "C6_rndTiempoLectura","C6_TiempoLectura","C6_TiempoSalida",
                "ID","C7_estado", "C7_TiempoEntrada", "C7_rndDesicion", "C7_rndTiempoLectura","C7_TiempoLectura","C7_TiempoSalida",
                "ID","C8_estado", "C8_TiempoEntrada", "C8_rndDesicion", "C8_rndTiempoLectura","C8_TiempoLectura","C8_TiempoSalida",
                "ID","C9_estado", "C9_TiempoEntrada", "C9_rndDesicion", "C9_rndTiempoLectura","C9_TiempoLectura","C9_TiempoSalida",
                "ID","C10_estado", "C10_TiempoEntrada", "C10_rndDesicion", "C10_rndTiempoLectura","C10_TiempoLectura","C10_TiempoSalida",
                "ID","C11_estado", "C11_TiempoEntrada", "C11_rndDesicion", "C11_rndTiempoLectura","C11_TiempoLectura","C11_TiempoSalida",
                "ID","C12_estado", "C12_TiempoEntrada", "C12_rndDesicion", "C12_rndTiempoLectura","C12_TiempoLectura","C12_TiempoSalida",
                "ID","C13_estado", "C13_TiempoEntrada", "C13_rndDesicion", "C13_rndTiempoLectura","C13_TiempoLectura","C13_TiempoSalida",
                "ID","C14_estado", "C14_TiempoEntrada", "C14_rndDesicion", "C14_rndTiempoLectura","C14_TiempoLectura","C14_TiempoSalida",
                "ID","C15_estado", "C15_TiempoEntrada", "C15_rndDesicion", "C15_rndTiempoLectura","C15_TiempoLectura","C15_TiempoSalida",
                "ID","C16_estado", "C16_TiempoEntrada", "C16_rndDesicion", "C16_rndTiempoLectura","C16_TiempoLectura","C16_TiempoSalida",
                "ID","C17_estado", "C17_TiempoEntrada", "C17_rndDesicion", "C17_rndTiempoLectura","C17_TiempoLectura","C17_TiempoSalida",
                "ID","C18_estado", "C18_TiempoEntrada", "C18_rndDesicion", "C18_rndTiempoLectura","C18_TiempoLectura","C18_TiempoSalida",
                "ID","C19_estado", "C19_TiempoEntrada", "C19_rndDesicion", "C19_rndTiempoLectura","C19_TiempoLectura","C19_TiempoSalida",
                "ID","C20_estado", "C20_TiempoEntrada", "C20_rndDesicion", "C20_rndTiempoLectura","C20_TiempoLectura","C20_TiempoSalida"
        };
        //impirmir la longitud del array
        System.out.println(columnNames.length);
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
                } else if (column >= 5 && column <= 8) {
                    c.setBackground(Color.DARK_GRAY);
                } else if (column >= 9 && column <= 15) {
                    c.setBackground(new Color(29, 34, 131));
                } else if (column >= 16 && column <= 22) {
                    c.setBackground(new Color(109, 32, 125));
                } else {
                    // Alternar colores cada 7 columnas a partir de la columna 21
                    if ((column - 23) / 7 % 2 == 0) {
                        c.setBackground(new Color(109, 93, 37));
                    } else {
                        c.setBackground(new Color(38, 131, 98));
                    }
                }

                return c;
            }
        };

        for (int i = 0; i < table1.getColumnCount(); i++) {
            table1.getColumnModel().getColumn(i).setCellRenderer(customRenderer);
        }

        //hacer focus en un fila de la tabla cuando elijo una fila
        table1.setCellSelectionEnabled(true);
        table1.setRowSelectionAllowed(true);
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
            double tiempoMinValue = Double.parseDouble(tiempoMin.getText());
            double tiempoMaxValue = Double.parseDouble(tiempoMax.getText());
            double probLecturaValue = Double.parseDouble(probLectura.getText());
            double promLecturaValue = Double.parseDouble(promLectura.getText());
            double frecLlegadaValue = Double.parseDouble(frecLlegada.getText());
            double tiempoDesdeValue = Double.parseDouble(tiempoDesde.getText());
            double tiempoValue = Double.parseDouble(tiempo.getText());
            int cantFilasValue = Integer.parseInt(cantFilas.getText());

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
        // Crear una simulación con los valores de los JTextField y luego llenar la tabla con la matriz
        Simulacion simulacion = new Simulacion(
                Optional.of(Double.parseDouble(frecLlegada.getText())),
                Optional.of(Double.parseDouble(tiempo.getText())),
                Optional.of(Double.parseDouble(tiempoDesde.getText())),
                Optional.of(Integer.parseInt(cantFilas.getText())),
                Optional.of(List.of(Double.parseDouble(probPedir.getText()),Double.parseDouble(probDevolver.getText()),Double.parseDouble(probSocio.getText()))),
                Optional.of(List.of(Double.parseDouble(tiempoMin.getText()), Double.parseDouble(tiempoMax.getText()))),
                Optional.of(Double.parseDouble(probLectura.getText())),
                Optional.of(Double.parseDouble(promLectura.getText()))
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

    }

    public static void main(String[] args) {
        FlatDarculaLaf.setup();
        new Simulador();
    }
}
