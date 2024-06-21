import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class Integracion extends JFrame {
    private JTable table1;
    private JPanel main;

    // Constructor que recibe una matriz de doubles y los muestra en la tabla
    public Integracion(List<Double[]> data) {
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        add(main);
        setTitle("Tabla de integracion numerica");
        //setea el tamaño de la ventana
        setSize(800, 600);

        // Limpiar la tabla y agregar las columnas
        table1.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                        "t", "M", "M'", "k1", "k2", "k3", "k4", "M_next"
                }
        ));

        // Agregar los datos a la tabla
        for (Double[] row : data) {
            ((javax.swing.table.DefaultTableModel) table1.getModel()).addRow(row);
        }

        // Centrar el contenido de las celdas de la tabla
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER); // Alineación centrada

        // Aplicar el renderer centrado a cada columna
        for (int i = 0; i < table1.getColumnCount(); i++) {
            table1.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Configurar selección de filas
        table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Selección de una sola fila
    }

    // Renderer personalizado para centrar el contenido de las celdas
    private static class DefaultTableCellRenderer extends JLabel implements javax.swing.table.TableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }
            setFont(table.getFont());
            setText(value != null ? value.toString() : "");
            setHorizontalAlignment(JLabel.CENTER); // Alineación centrada
            return this;
        }
    }
}
