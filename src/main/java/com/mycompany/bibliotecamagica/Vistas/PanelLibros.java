/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.bibliotecamagica.Vistas;

import com.mycompany.bibliotecamagica.Arboles.Biblioteca;
import com.mycompany.bibliotecamagica.Arboles.Libro;
import com.mycompany.bibliotecamagica.CatalogoGlobal;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author Ana
 */
public class PanelLibros extends javax.swing.JFrame {

    /**
     * Creates new form PanelLibros
     */
    private JComboBox<String> comboBibliotecas;
    private JTextField txtTitulo, txtAutor, txtIsbn, txtAnio, txtGenero, txtEliminar;
    private JButton btnAgregar, btnEliminar, btnBuscar, btnCerrar;
    private JTextArea areaResultado;

    private CatalogoGlobal catalogo;

    public PanelLibros(CatalogoGlobal catalogo) {
        this.catalogo = catalogo;
        inicializar();
    }

    public PanelLibros() {
        this(new CatalogoGlobal(new java.util.HashMap<>(), new com.mycompany.bibliotecamagica.EstructurasBasicas.HashTableISBN()));
    }

    private void inicializar() {
        setTitle("Gestión de Libros 📚");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(240, 235, 245));
        setLayout(new BorderLayout(10, 10));

        JPanel panelDatos = new JPanel(new GridLayout(6, 2, 5, 5));
        panelDatos.setBorder(BorderFactory.createTitledBorder("Agregar Libro"));
        txtTitulo = new JTextField();
        txtAutor = new JTextField();
        txtIsbn = new JTextField();
        txtAnio = new JTextField();
        txtGenero = new JTextField();
        comboBibliotecas = new JComboBox<>();

        for (String id : catalogo.getBibliotecas().keySet()) {
            comboBibliotecas.addItem(id);
        }

        panelDatos.add(new JLabel("Título:"));
        panelDatos.add(txtTitulo);
        panelDatos.add(new JLabel("Autor:"));
        panelDatos.add(txtAutor);
        panelDatos.add(new JLabel("ISBN:"));
        panelDatos.add(txtIsbn);
        panelDatos.add(new JLabel("Año:"));
        panelDatos.add(txtAnio);
        panelDatos.add(new JLabel("Género:"));
        panelDatos.add(txtGenero);
        panelDatos.add(new JLabel("ID Biblioteca:"));
        panelDatos.add(comboBibliotecas);

        JPanel panelBotones = new JPanel(new GridLayout(4, 1, 5, 5));
        btnAgregar = new JButton("Agregar Libro");
        btnEliminar = new JButton("Eliminar Libro");
        btnBuscar = new JButton("Buscar Libro");
        btnCerrar = new JButton("Cerrar");
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnBuscar);
        panelBotones.add(btnCerrar);

        areaResultado = new JTextArea();
        areaResultado.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaResultado);
        scroll.setBorder(BorderFactory.createTitledBorder("Resultados"));

        JPanel panelEliminar = new JPanel(new FlowLayout());
        txtEliminar = new JTextField(20);
        panelEliminar.add(new JLabel("Título / ISBN / Género / Año:"));
        panelEliminar.add(txtEliminar);

        add(panelDatos, BorderLayout.NORTH);
        add(panelBotones, BorderLayout.EAST);
        add(scroll, BorderLayout.CENTER);
        add(panelEliminar, BorderLayout.SOUTH);

        btnAgregar.addActionListener(e -> agregarLibro());
        btnEliminar.addActionListener(e -> eliminarLibro());
        btnBuscar.addActionListener(e -> buscarLibro());
        btnCerrar.addActionListener(e -> dispose());
    }

    private void agregarLibro() {
        try {
            String titulo = txtTitulo.getText().trim();
            String autor = txtAutor.getText().trim();
            String isbn = txtIsbn.getText().trim();
            String genero = txtGenero.getText().trim();
            int anio = Integer.parseInt(txtAnio.getText().trim());
            String idBiblio = comboBibliotecas.getSelectedItem().toString();

            if (titulo.isEmpty() || autor.isEmpty() || isbn.isEmpty() || genero.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe completar todos los campos.");
                return;
            }

            Libro libro = new Libro(titulo, autor, isbn, anio, genero);
            catalogo.insertarLibroEn(idBiblio, libro);
            areaResultado.append(" Libro agregado: " + titulo + " → " + idBiblio + "\n");

            txtTitulo.setText("");
            txtAutor.setText("");
            txtIsbn.setText("");
            txtAnio.setText("");
            txtGenero.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void buscarLibro() {
        String texto = txtEliminar.getText().trim().toLowerCase();
        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese título, género, año o ISBN para buscar.");
            return;
        }

        areaResultado.setText("");
        for (Biblioteca b : catalogo.getBibliotecas().values()) {
            b.getArbolAvl().mostrarCoincidencias(texto, areaResultado);

        }
    }

    private void eliminarLibro() {
        String texto = txtEliminar.getText().trim();
        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese título, género, año o ISBN para eliminar.");
            return;
        }

        try {
            if (texto.matches("\\d+")) {
                catalogo.eliminarLibro(texto);
            } else {
                Object obj = catalogo.getHash().buscar(texto);
                if (obj instanceof Libro libro) {
                    catalogo.eliminarLibro(libro.getIsbn());
                }
            }
            areaResultado.append("️ Libro eliminado: " + texto + "\n");
        } catch (Exception ex) {
            areaResultado.append("Error al eliminar: " + ex.getMessage() + "\n");
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
            java.util.logging.Logger.getLogger(PanelLibros.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PanelLibros.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PanelLibros.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PanelLibros.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PanelLibros().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
