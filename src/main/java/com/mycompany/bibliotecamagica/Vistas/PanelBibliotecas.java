/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.bibliotecamagica.Vistas;

import com.mycompany.bibliotecamagica.Arboles.Biblioteca;
import com.mycompany.bibliotecamagica.EstructurasBasicas.Grafo;
import com.mycompany.bibliotecamagica.EstructurasBasicas.HashTableISBN;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Ana
 */
public class PanelBibliotecas extends javax.swing.JFrame {

    private final Map<String, Biblioteca> bibliotecas;
    private final Grafo grafo;

    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtId, txtNombre, txtUbicacion, txtIngreso, txtTraspaso, txtIntervalo;
    private JButton btnAgregar, btnModificar, btnActualizar, btnCerrar;

    public PanelBibliotecas(Map<String, Biblioteca> bibliotecas, Grafo grafo) {
        this.bibliotecas = bibliotecas;
        this.grafo = grafo;
        inicializar();
        actualizarTabla();
    }

    public PanelBibliotecas() {
        this(new java.util.HashMap<>(), new com.mycompany.bibliotecamagica.EstructurasBasicas.Grafo());
    }

    private void inicializar() {
        setTitle("Gestión de Bibliotecas");
        setSize(850, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // 🧩 Tabla
        modelo = new DefaultTableModel(new Object[]{"ID", "Nombre", "Ubicación", "Ingreso", "Traspaso", "Despacho"}, 0);
        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(tabla);

        JPanel panelEdicion = new JPanel(new GridLayout(6, 2, 5, 5));
        panelEdicion.setBorder(BorderFactory.createTitledBorder("Datos Biblioteca"));
        txtId = new JTextField();
        txtNombre = new JTextField();
        txtUbicacion = new JTextField();
        txtIngreso = new JTextField();
        txtTraspaso = new JTextField();
        txtIntervalo = new JTextField();

        panelEdicion.add(new JLabel("ID:"));
        panelEdicion.add(txtId);
        panelEdicion.add(new JLabel("Nombre:"));
        panelEdicion.add(txtNombre);
        panelEdicion.add(new JLabel("Ubicación:"));
        panelEdicion.add(txtUbicacion);
        panelEdicion.add(new JLabel("Tiempo Ingreso (s):"));
        panelEdicion.add(txtIngreso);
        panelEdicion.add(new JLabel("Tiempo Traspaso (s):"));
        panelEdicion.add(txtTraspaso);
        panelEdicion.add(new JLabel("Intervalo Despacho (s):"));
        panelEdicion.add(txtIntervalo);

        JPanel panelBotones = new JPanel(new GridLayout(4, 1, 5, 5));
        btnAgregar = new JButton("Agregar");
        btnModificar = new JButton("Modificar");
        btnActualizar = new JButton("Actualizar tabla");
        btnCerrar = new JButton("Cerrar");
        panelBotones.add(btnAgregar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnCerrar);

        add(scroll, BorderLayout.CENTER);
        add(panelEdicion, BorderLayout.NORTH);
        add(panelBotones, BorderLayout.EAST);

        btnAgregar.addActionListener(e -> agregarBiblioteca());
        btnModificar.addActionListener(e -> modificarBiblioteca());
        btnActualizar.addActionListener(e -> actualizarTabla());
        btnCerrar.addActionListener(e -> dispose());

        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tabla.getSelectedRow();
                if (fila >= 0) {
                    txtId.setText(modelo.getValueAt(fila, 0).toString());
                    txtNombre.setText(modelo.getValueAt(fila, 1).toString());
                    txtUbicacion.setText(modelo.getValueAt(fila, 2).toString());
                    txtIngreso.setText(modelo.getValueAt(fila, 3).toString());
                    txtTraspaso.setText(modelo.getValueAt(fila, 4).toString());
                    txtIntervalo.setText(modelo.getValueAt(fila, 5).toString());
                }
            }
        });
    }

    private void agregarBiblioteca() {
        try {
            String id = txtId.getText().trim();
            if (id.isEmpty() || bibliotecas.containsKey(id)) {
                JOptionPane.showMessageDialog(this, "ID inválido o ya existe.");
                return;
            }

            String nombre = txtNombre.getText().trim();
            String ubicacion = txtUbicacion.getText().trim();
            int ingreso = Integer.parseInt(txtIngreso.getText().trim());
            int traspaso = Integer.parseInt(txtTraspaso.getText().trim());
            int intervalo = Integer.parseInt(txtIntervalo.getText().trim());

            Biblioteca b = new Biblioteca(id, nombre, ubicacion, ingreso, traspaso, intervalo);
            bibliotecas.put(id, b);
            grafo.registrarBiblioteca(b);
            actualizarTabla();
            JOptionPane.showMessageDialog(this, "Biblioteca agregada correctamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar: " + ex.getMessage());
        }
    }

    private void modificarBiblioteca() {
        try {
            String id = txtId.getText().trim();
            Biblioteca b = bibliotecas.get(id);
            if (b == null) {
                JOptionPane.showMessageDialog(this, "No existe una biblioteca con ese ID.");
                return;
            }

            b.setNombre(txtNombre.getText().trim());
            b.setUbicacion(txtUbicacion.getText().trim());
            b.setTiempos(
                    Integer.parseInt(txtIngreso.getText().trim()),
                    Integer.parseInt(txtTraspaso.getText().trim()),
                    Integer.parseInt(txtIntervalo.getText().trim())
            );
            actualizarTabla();
            JOptionPane.showMessageDialog(this, "Biblioteca modificada correctamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al modificar: " + ex.getMessage());
        }
    }

    private void actualizarTabla() {
        modelo.setRowCount(0);
        for (Biblioteca b : bibliotecas.values()) {
            modelo.addRow(new Object[]{
                b.getId(), b.getNombre(), b.getUbicacion(),
                b.getTiempoIngreso(), b.getTiempoTraspaso(), b.getIntervaloDespacho()
            });
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PanelBibliotecas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PanelBibliotecas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PanelBibliotecas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PanelBibliotecas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PanelBibliotecas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
