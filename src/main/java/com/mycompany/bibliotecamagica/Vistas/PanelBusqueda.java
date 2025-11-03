/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.bibliotecamagica.Vistas;

import com.mycompany.bibliotecamagica.Arboles.Biblioteca;
import com.mycompany.bibliotecamagica.Arboles.Libro;
import com.mycompany.bibliotecamagica.CatalogoGlobal;
import com.mycompany.bibliotecamagica.EstructurasBasicas.ListaNoOrdenada;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
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
public class PanelBusqueda extends javax.swing.JFrame {

    private JTextField txtTitulo, txtISBN, txtGenero, txtDesde, txtHasta;
    private JButton btnBuscarTitulo, btnBuscarISBN, btnBuscarGenero, btnComparar, btnCerrar;
    private JTextArea areaResultado;

    private CatalogoGlobal catalogo;
    private ListaNoOrdenada listaNoOrdenada;

    public PanelBusqueda(CatalogoGlobal catalogo, ListaNoOrdenada lista) {
        this.catalogo = catalogo;
        this.listaNoOrdenada = lista;
        inicializar();
    }

    public PanelBusqueda() {

        this.catalogo = new CatalogoGlobal(new java.util.HashMap<>(), new com.mycompany.bibliotecamagica.EstructurasBasicas.HashTableISBN());
        this.listaNoOrdenada = new com.mycompany.bibliotecamagica.EstructurasBasicas.ListaNoOrdenada();
        inicializar();
    }

    private void inicializar() {
        setTitle("Búsqueda de Libros");
        setSize(700, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(240, 235, 245));
        setLayout(new BorderLayout(10, 10));

        JPanel panelCampos = new JPanel(new GridLayout(5, 2, 8, 8));
        panelCampos.setBorder(BorderFactory.createTitledBorder("Opciones de Búsqueda"));

        txtTitulo = new JTextField();
        txtISBN = new JTextField();
        txtGenero = new JTextField();
        txtDesde = new JTextField();
        txtHasta = new JTextField();

        panelCampos.add(new JLabel("Título (AVL o Secuencial):"));
        panelCampos.add(txtTitulo);
        panelCampos.add(new JLabel("ISBN (Hash o Binaria):"));
        panelCampos.add(txtISBN);
        panelCampos.add(new JLabel("Género (B+):"));
        panelCampos.add(txtGenero);
        panelCampos.add(new JLabel("Rango de años (desde):"));
        panelCampos.add(txtDesde);
        panelCampos.add(new JLabel("Rango de años (hasta):"));
        panelCampos.add(txtHasta);

        JPanel panelBotones = new JPanel(new GridLayout(5, 1, 5, 5));
        btnBuscarTitulo = new JButton("Buscar por Título");
        btnBuscarISBN = new JButton("Buscar por ISBN");
        btnBuscarGenero = new JButton("Buscar por Género / Rango");
        btnComparar = new JButton("Comparar Tiempos");
        btnCerrar = new JButton("Cerrar");

        panelBotones.add(btnBuscarTitulo);
        panelBotones.add(btnBuscarISBN);
        panelBotones.add(btnBuscarGenero);
        panelBotones.add(btnComparar);
        panelBotones.add(btnCerrar);

        areaResultado = new JTextArea();
        areaResultado.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaResultado);
        scroll.setBorder(BorderFactory.createTitledBorder("Resultados"));

        add(panelCampos, BorderLayout.NORTH);
        add(panelBotones, BorderLayout.EAST);
        add(scroll, BorderLayout.CENTER);

        btnBuscarTitulo.addActionListener(e -> buscarPorTitulo());
        btnBuscarISBN.addActionListener(e -> buscarPorISBN());
        btnBuscarGenero.addActionListener(e -> buscarPorGeneroORango());
        btnComparar.addActionListener(e -> compararTiempos());
        btnCerrar.addActionListener(e -> dispose());
    }

    private void buscarPorTitulo() {
        String titulo = txtTitulo.getText().trim();
        if (titulo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un título para buscar.");
            return;
        }

        areaResultado.setText("Resultado de búsqueda por título:\n");

        for (Biblioteca b : catalogo.getBibliotecas().values()) {
            Libro encontradoAVL = b.getArbolAvl().buscar(titulo);
            if (encontradoAVL != null) {
                areaResultado.append("[AVL] " + encontradoAVL + " | Biblioteca: " + b.getNombre() + "\n");
            }
        }

        Libro secuencial = listaNoOrdenada.buscarPorTituloSecuencial(titulo);
        if (secuencial != null) {
            areaResultado.append("[Secuencial] " + secuencial + "\n");
        } else {
            areaResultado.append("No se encontró el libro.\n");
        }
    }

    private void buscarPorISBN() {
        String isbn = txtISBN.getText().trim();
        if (isbn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un ISBN para buscar.");
            return;
        }

        areaResultado.setText("Resultado de búsqueda por ISBN:\n");

        Libro hash = (Libro) catalogo.getHash().buscar(isbn);
        if (hash != null) {
            areaResultado.append("[Hash] " + hash + "\n");
        }

        Libro binaria = listaNoOrdenada.buscarPorISBNBinaria(isbn);
        if (binaria != null) {
            areaResultado.append("[Binaria] " + binaria + "\n");
        } else {
            areaResultado.append("No se encontró el libro.\n");
        }
    }

    private void buscarPorGeneroORango() {
        String genero = txtGenero.getText().trim();
        String desdeTxt = txtDesde.getText().trim();
        String hastaTxt = txtHasta.getText().trim();

        areaResultado.setText("Resultado de búsqueda por género o rango:\n");

        for (Biblioteca b : catalogo.getBibliotecas().values()) {
            if (!genero.isEmpty()) {
                var lista = b.getArbolBMas().buscarPorGenero(genero);
                for (Libro l : lista) {
                    areaResultado.append("[B+] " + l + " | Biblioteca: " + b.getNombre() + "\n");
                }
            }

            if (!desdeTxt.isEmpty() && !hastaTxt.isEmpty()) {
                try {
                    int desde = Integer.parseInt(desdeTxt);
                    int hasta = Integer.parseInt(hastaTxt);
                    areaResultado.append("Libros entre " + desde + " y " + hasta + ":\n");
                    b.getArbolB().buscarPorRango(desde, hasta);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Los años deben ser numéricos.");
                }
            }
        }
    }

    private void compararTiempos() {
        String dato = JOptionPane.showInputDialog(this,
                "Ingrese el título o ISBN del libro a comparar:",
                "Comparación de búsquedas", JOptionPane.QUESTION_MESSAGE);

        if (dato == null || dato.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar un valor válido.");
            return;
        }

        dato = dato.trim();
        areaResultado.setText("Comparación de tiempos para: " + dato + "\n\n");

        Libro secuencial = null;
        Libro avl = null;
        Libro hash = null;

        long tSec = 0, tAvl = 0, tHash = 0;

        try {
            // Secuencial
            long start = System.nanoTime();
            secuencial = listaNoOrdenada.buscarPorTituloSecuencial(dato);
            if (secuencial == null) {
                secuencial = listaNoOrdenada.buscarPorISBNSecuencial(dato);
            }
            tSec = System.nanoTime() - start;

            // AVL
            start = System.nanoTime();
            for (Biblioteca b : catalogo.getBibliotecas().values()) {
                avl = b.getArbolAvl().buscar(dato);
                if (avl != null) {
                    break;
                }
            }
            tAvl = System.nanoTime() - start;

            // Hash
            start = System.nanoTime();
            hash = (Libro) catalogo.getHash().buscar(dato);
            tHash = System.nanoTime() - start;

        } catch (Exception ex) {
            areaResultado.append("Error en la comparación: " + ex.getMessage() + "\n");
            return;
        }

        areaResultado.append("Resultados encontrados:\n");
        if (secuencial != null) {
            areaResultado.append("Secuencial: " + secuencial + "\n");
        }
        if (avl != null) {
            areaResultado.append("AVL: " + avl + "\n");
        }
        if (hash != null) {
            areaResultado.append("Hash: " + hash + "\n");
        }
        if (secuencial == null && avl == null && hash == null) {
            areaResultado.append("Ninguna estructura contiene el libro.\n");
        }

        areaResultado.append("\nTiempos de ejecución:\n");
        areaResultado.append(String.format("Secuencial: %,d ns (%.5f ms)\n", tSec, tSec / 1_000_000.0));
        areaResultado.append(String.format("AVL: %,d ns (%.5f ms)\n", tAvl, tAvl / 1_000_000.0));
        areaResultado.append(String.format("Hash: %,d ns (%.5f ms)\n", tHash, tHash / 1_000_000.0));

        long min = Math.min(tSec, Math.min(tAvl, tHash));
        String rapido = (min == tSec) ? "Secuencial" : (min == tAvl) ? "AVL" : "Hash";

        areaResultado.append("\nEstructura más rápida: " + rapido + "\n");

        boolean consistentes = (secuencial != null && avl != null && hash != null);
        areaResultado.append("Consistencia de resultados: " + (consistentes ? "Coinciden" : "No coinciden o faltan datos") + "\n");
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
            java.util.logging.Logger.getLogger(PanelBusqueda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PanelBusqueda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PanelBusqueda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PanelBusqueda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PanelBusqueda().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
