/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.bibliotecamagica.Vistas;

import com.mycompany.bibliotecamagica.Arboles.Biblioteca;
import com.mycompany.bibliotecamagica.EstructurasBasicas.Grafo;
import com.mycompany.bibliotecamagica.EstructurasBasicas.HashTableISBN;
import com.mycompany.bibliotecamagica.Vistas.Graficador.Graficador;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

/**
 *
 * @author Ana
 */
public class PanelGraficador extends javax.swing.JFrame {

    /**
     * Creates new form PanelGraficador
     */
    public PanelGraficador() {
        initComponents();
    }
    private Map<String, Biblioteca> bibliotecas;
    private Grafo grafo;
    private HashTableISBN hash;
    private JLabel lblImagen;
    private JComboBox<String> comboBiblio;
    private JButton btnAVL, btnB, btnBmas, btnHash, btnRed, btnColas, btnTodasColas, btnCerrar;

    private static final String RUTA = "C:\\Users\\Ana\\Documents\\NetBeansProjects\\BibliotecaMagica\\graficos\\";

    public PanelGraficador(Map<String, Biblioteca> bibliotecas, Grafo grafo, HashTableISBN hash) {
        this.bibliotecas = bibliotecas;
        this.grafo = grafo;
        this.hash = hash;
        init();
    }

    private void init() {
        setTitle("Visualizador de Gráficos (Graphviz)");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        comboBiblio = new JComboBox<>();
        for (String id : bibliotecas.keySet()) {
            comboBiblio.addItem(id);
        }

        btnAVL = new JButton("AVL");
        btnB = new JButton("Árbol B");
        btnBmas = new JButton("Árbol B+");
        btnHash = new JButton("Tabla Hash");
        btnRed = new JButton("Red Bibliotecas");
        btnColas = new JButton("Colas (una)");
        btnTodasColas = new JButton("Colas (todas)");
        btnCerrar = new JButton("Cerrar");

        panelTop.add(new JLabel("Biblioteca:"));
        panelTop.add(comboBiblio);
        panelTop.add(btnAVL);
        panelTop.add(btnB);
        panelTop.add(btnBmas);
        panelTop.add(btnHash);
        panelTop.add(btnRed);
        panelTop.add(btnColas);
        panelTop.add(btnTodasColas);
        panelTop.add(btnCerrar);

        lblImagen = new JLabel("Selecciona un gráfico para visualizar", SwingConstants.CENTER);
        lblImagen.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JScrollPane scroll = new JScrollPane(lblImagen);
        add(panelTop, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        btnAVL.addActionListener(e -> graficarAVL());
        btnB.addActionListener(e -> graficarB());
        btnBmas.addActionListener(e -> graficarBmas());
        btnHash.addActionListener(e -> graficarHash());
        btnRed.addActionListener(e -> graficarRed());
        btnColas.addActionListener(e -> graficarColas());
        btnTodasColas.addActionListener(e -> graficarTodas());
        btnCerrar.addActionListener(e -> dispose());
    }

    private Biblioteca getSeleccionada() {
        String key = (String) comboBiblio.getSelectedItem();
        return bibliotecas.get(key);
    }

    private void graficarAVL() {
        try {
            Biblioteca b = getSeleccionada();
            if (b == null) {
                msg("Seleccione una biblioteca válida.");
                return;
            }
            Graficador.graficarAVL(b.getArbolAvl());
            mostrarImagen("AVL.png");
        } catch (Exception ex) {
            msg("Error: " + ex.getMessage());
        }
    }

    private void graficarB() {
        try {
            Biblioteca b = getSeleccionada();
            if (b == null) {
                msg("Seleccione una biblioteca válida.");
                return;
            }
            Graficador.graficarArbolB(b.getArbolB());
            mostrarImagen("ArbolB.png");
        } catch (Exception ex) {
            msg("Error: " + ex.getMessage());
        }
    }

    private void graficarBmas() {
        try {
            Biblioteca b = getSeleccionada();
            if (b == null) {
                msg("Seleccione una biblioteca válida.");
                return;
            }
            Graficador.graficarArbolBMas(b.getArbolBMas());
            mostrarImagen("ArbolBMas.png");
        } catch (Exception ex) {
            msg("Error: " + ex.getMessage());
        }
    }

    private void graficarHash() {
        try {
            Graficador.graficarHash(hash);
            mostrarImagen("TablaHash.png");
        } catch (Exception ex) {
            msg("Error: " + ex.getMessage());
        }
    }

    private void graficarRed() {
        try {
            Graficador.graficarRed(grafo);
            mostrarImagen("RedBibliotecas.png");
        } catch (Exception ex) {
            msg("Error: " + ex.getMessage());
        }
    }

    private void graficarColas() {
        try {
            Biblioteca b = getSeleccionada();
            if (b == null) {
                msg("Seleccione una biblioteca válida.");
                return;
            }
            Graficador.graficarColas(b);
            mostrarImagen("Colas_" + b.getNombre() + ".png");
        } catch (Exception ex) {
            msg("Error: " + ex.getMessage());
        }
    }

    private void graficarTodas() {
        try {
            Graficador.graficarTodasLasColas(bibliotecas);
            mostrarImagen("Colas_TodasBibliotecas.png");
        } catch (Exception ex) {
            msg("Error: " + ex.getMessage());
        }
    }

    private void mostrarImagen(String nombreArchivo) {
        try {
            File imgFile = new File(RUTA + nombreArchivo);
            if (!imgFile.exists()) {
                msg("No se encontró la imagen: " + imgFile.getName());
                return;
            }
            BufferedImage img = ImageIO.read(imgFile);
            lblImagen.setIcon(new ImageIcon(img.getScaledInstance(850, 550, Image.SCALE_SMOOTH)));
            lblImagen.setText("");
        } catch (Exception e) {
            msg("Error al mostrar imagen: " + e.getMessage());
        }
    }

    private void msg(String s) {
        JOptionPane.showMessageDialog(this, s);
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
            java.util.logging.Logger.getLogger(PanelGraficador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PanelGraficador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PanelGraficador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PanelGraficador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PanelGraficador().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
