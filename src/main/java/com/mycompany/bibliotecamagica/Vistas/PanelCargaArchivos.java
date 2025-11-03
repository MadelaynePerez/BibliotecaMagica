/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.bibliotecamagica.Vistas;

import com.mycompany.bibliotecamagica.Arboles.Biblioteca;
import com.mycompany.bibliotecamagica.CargaArchivo;
import com.mycompany.bibliotecamagica.EstructurasBasicas.Grafo;
import com.mycompany.bibliotecamagica.EstructurasBasicas.HashTableISBN;
import java.awt.BorderLayout;
import java.awt.GridLayout;

import java.io.File;
import java.util.*;
import javax.swing.*;

/**
 *
 * @author Ana
 */
public class PanelCargaArchivos extends javax.swing.JFrame {

    /**
     * Creates new form PanelCargaArchivos
     */
    private final Map<String, Biblioteca> bibliotecas;
    private final Grafo grafo;
    private final HashTableISBN hash;
    private final CargaArchivo cargador;
    private JTextField txtBibliotecas, txtLibros, txtConexiones;
    private JTextArea areaLog;
    private JButton btnCargarBibliotecas, btnCargarLibros, btnCargarConexiones, btnCerrar;

    public PanelCargaArchivos() {
        initComponents();
        this.bibliotecas = new HashMap<>();
        this.grafo = new Grafo();
        this.hash = new HashTableISBN();
        this.cargador = new CargaArchivo();
        init();
    }

    public PanelCargaArchivos(Map<String, Biblioteca> bibliotecas,
            Grafo grafo,
            HashTableISBN hash,
            CargaArchivo cargador) {
        initComponents();
        this.bibliotecas = bibliotecas;
        this.grafo = grafo;
        this.hash = hash;
        this.cargador = cargador;
        init();
    }

    private void init() {
        setTitle("Cargar Archivos CSV");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel panelTop = new JPanel(new GridLayout(4, 3, 10, 10));
        panelTop.setBorder(BorderFactory.createTitledBorder("Archivos a cargar"));

        txtBibliotecas = new JTextField();
        txtLibros = new JTextField();
        txtConexiones = new JTextField();

        JButton btnBuscarBiblio = new JButton("Buscar...");
        JButton btnBuscarLibros = new JButton("Buscar...");
        JButton btnBuscarConex = new JButton("Buscar...");

        btnBuscarBiblio.addActionListener(e -> seleccionarArchivo(txtBibliotecas));
        btnBuscarLibros.addActionListener(e -> seleccionarArchivo(txtLibros));
        btnBuscarConex.addActionListener(e -> seleccionarArchivo(txtConexiones));

        panelTop.add(new JLabel(" Bibliotecas:"));
        panelTop.add(txtBibliotecas);
        panelTop.add(btnBuscarBiblio);

        panelTop.add(new JLabel(" Libros:"));
        panelTop.add(txtLibros);
        panelTop.add(btnBuscarLibros);

        panelTop.add(new JLabel(" Conexiones:"));
        panelTop.add(txtConexiones);
        panelTop.add(btnBuscarConex);

        btnCargarBibliotecas = new JButton("Cargar Bibliotecas");
        btnCargarLibros = new JButton("Cargar Libros");
        btnCargarConexiones = new JButton("Cargar Conexiones");
        btnCerrar = new JButton("Cerrar");

        panelTop.add(btnCargarBibliotecas);
        panelTop.add(btnCargarLibros);
        panelTop.add(btnCargarConexiones);

        JPanel panelBtns = new JPanel();
        panelBtns.add(btnCerrar);

        areaLog = new JTextArea();
        areaLog.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaLog);
        scroll.setBorder(BorderFactory.createTitledBorder("Resultados y errores"));

        add(panelTop, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(panelBtns, BorderLayout.SOUTH);

        btnCargarBibliotecas.addActionListener(e -> cargarBibliotecas());
        btnCargarLibros.addActionListener(e -> cargarLibros());
        btnCargarConexiones.addActionListener(e -> cargarConexiones());
        btnCerrar.addActionListener(e -> dispose());
    }

    private void seleccionarArchivo(JTextField destino) {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setDialogTitle("Seleccionar archivo CSV");
        int sel = fc.showOpenDialog(this);
        if (sel == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            destino.setText(f.getAbsolutePath());
        }
    }

    private void cargarBibliotecas() {
        String ruta = txtBibliotecas.getText().trim();
        if (ruta.isEmpty()) {
            msg("Seleccione un archivo de bibliotecas.");
            return;
        }

        List<String> errores = new ArrayList<>();
        List<Biblioteca> lista = cargador.cargarBibliotecas(ruta, grafo, errores);

        for (Biblioteca b : lista) {
            bibliotecas.put(b.getId(), b);
        }

        mostrarResultado("Bibliotecas cargadas: " + lista.size(), errores);
    }

    private void cargarLibros() {
        if (bibliotecas.isEmpty()) {
            msg("Primero cargue las bibliotecas.");
            return;
        }

        String ruta = txtLibros.getText().trim();
        if (ruta.isEmpty()) {
            msg("Seleccione un archivo de libros.");
            return;
        }

        com.mycompany.bibliotecamagica.SimuladorCompleto simulador
                = new com.mycompany.bibliotecamagica.SimuladorCompleto(grafo, hash, bibliotecas);

        List<String> errores = new ArrayList<>();
        int total = cargador.cargarLibros(ruta, bibliotecas, grafo, simulador, errores);

        mostrarResultado("Libros cargados: " + total, errores);
    }

    private void cargarConexiones() {
        String ruta = txtConexiones.getText().trim();
        if (ruta.isEmpty()) {
            msg("Seleccione un archivo de conexiones.");
            return;
        }

        List<String> errores = new ArrayList<>();
        int total = cargador.cargarConexiones(ruta, grafo, errores);

        mostrarResultado("Conexiones cargadas: " + total, errores);
    }

    private void mostrarResultado(String titulo, List<String> errores) {
        areaLog.append("\n-----------------------------------------\n");
        areaLog.append(titulo + "\n");
        if (errores.isEmpty()) {
            areaLog.append(" Sin errores.\n");
        } else {
            areaLog.append("️ Errores encontrados:\n");
            for (String e : errores) {
                areaLog.append(" - " + e + "\n");
            }
        }
        areaLog.append("-----------------------------------------\n");
    }

    private void msg(String s) {
        JOptionPane.showMessageDialog(this, s);
    }

    public Map<String, Biblioteca> getBibliotecas() {
        return bibliotecas;
    }

    public Grafo getGrafo() {
        return grafo;
    }

    public HashTableISBN getHash() {
        return hash;
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
            java.util.logging.Logger.getLogger(PanelCargaArchivos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PanelCargaArchivos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PanelCargaArchivos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PanelCargaArchivos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PanelCargaArchivos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
