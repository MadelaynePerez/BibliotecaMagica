/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.bibliotecamagica.Vistas;

import com.mycompany.bibliotecamagica.Arboles.Biblioteca;
import com.mycompany.bibliotecamagica.EstructurasBasicas.HashTableISBN;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 *
 * @author Ana
 */
public class PanelBibliotecas extends javax.swing.JFrame {

    private DefaultListModel<String> modeloLista;
    private JList<String> lista;
    private JTextField txtId, txtNombre, txtUbicacion;
    private JButton btnAgregar, btnModificar, btnEliminar, btnCerrar;
    private HashTableISBN hash;
    private Map<String, Biblioteca> bibliotecas;

    public PanelBibliotecas(HashTableISBN hash, Map<String, Biblioteca> bibliotecas) {
        this.hash = hash;
        this.bibliotecas = bibliotecas;
        inicializar();
    }

    public PanelBibliotecas() {

        this(new HashTableISBN(), new java.util.HashMap<>());
    }

    private void inicializar() {
        setTitle("Gestión de Bibliotecas");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(240, 230, 245));
        setLayout(new BorderLayout(10, 10));

        modeloLista = new DefaultListModel<>();
        lista = new JList<>(modeloLista);
        JScrollPane scroll = new JScrollPane(lista);

        JPanel datos = new JPanel(new GridLayout(3, 2, 5, 5));
        datos.add(new JLabel("ID:"));
        txtId = new JTextField();
        datos.add(txtId);

        datos.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        datos.add(txtNombre);

        datos.add(new JLabel("Ubicación:"));
        txtUbicacion = new JTextField();
        datos.add(txtUbicacion);

        JPanel botones = new JPanel(new GridLayout(4, 1, 5, 5));
        btnAgregar = new JButton("Agregar");
        btnModificar = new JButton("Modificar");
        btnEliminar = new JButton("Eliminar");
        btnCerrar = new JButton("Cerrar");
        botones.add(btnAgregar);
        botones.add(btnModificar);
        botones.add(btnEliminar);
        botones.add(btnCerrar);

        add(new JLabel(" Bibliotecas registradas", SwingConstants.CENTER), BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(datos, BorderLayout.WEST);
        add(botones, BorderLayout.EAST);

        // --- Eventos ---
        btnAgregar.addActionListener(e -> agregarBiblioteca());
        btnModificar.addActionListener(e -> modificarBiblioteca());
        btnEliminar.addActionListener(e -> eliminarBiblioteca());
        btnCerrar.addActionListener(e -> dispose());
    }

    private void agregarBiblioteca() {
        String id = txtId.getText().trim();
        String nombre = txtNombre.getText().trim();
        String ubicacion = txtUbicacion.getText().trim();

        if (id.isEmpty() || nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar al menos ID y Nombre.");
            return;
        }

        if (bibliotecas.containsKey(id)) {
            JOptionPane.showMessageDialog(this, "Ya existe una biblioteca con ese ID.");
            return;
        }

        Biblioteca b = new Biblioteca(id, nombre, ubicacion, 2, 2, 3);
        bibliotecas.put(id, b);
        hash.insertar(nombre, b);
        modeloLista.addElement(nombre);
        JOptionPane.showMessageDialog(this, "Biblioteca agregada correctamente.");

        txtId.setText("");
        txtNombre.setText("");
        txtUbicacion.setText("");
    }

    private void modificarBiblioteca() {
        String seleccion = lista.getSelectedValue();
        if (seleccion == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una biblioteca.");
            return;
        }

        for (Biblioteca b : bibliotecas.values()) {
            if (b.getNombre().equals(seleccion)) {
                String nuevoNombre = JOptionPane.showInputDialog(this, "Nuevo nombre:", b.getNombre());
                if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
                    b.getClass(); // referencia
                    b = new Biblioteca(b.getNombre(), nuevoNombre, "Ubicación modificada", 2, 2, 3);
                    modeloLista.setElementAt(nuevoNombre, lista.getSelectedIndex());
                    JOptionPane.showMessageDialog(this, "Nombre actualizado.");
                }
                return;
            }
        }
    }

    private void eliminarBiblioteca() {
        String seleccion = lista.getSelectedValue();
        if (seleccion == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una biblioteca para eliminar.");
            return;
        }

        bibliotecas.values().removeIf(b -> b.getNombre().equals(seleccion));
        modeloLista.removeElement(seleccion);
        JOptionPane.showMessageDialog(this, "Biblioteca eliminada correctamente.");
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
