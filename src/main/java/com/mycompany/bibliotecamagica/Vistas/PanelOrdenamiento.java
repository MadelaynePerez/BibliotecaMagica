/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.bibliotecamagica.Vistas;

import com.mycompany.bibliotecamagica.Arboles.Libro;
import com.mycompany.bibliotecamagica.EstructurasBasicas.BusquedaOrdenamiento;
import com.mycompany.bibliotecamagica.EstructurasBasicas.ListaNoOrdenada;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author Ana
 */
public class PanelOrdenamiento extends javax.swing.JFrame {

    /**
     * Creates new form OrdenamientoVista
     */
    private JComboBox<String> comboAtributo, comboMetodo;
    private JButton btnOrdenar, btnComparar, btnCerrar;
    private JTextArea areaResultado;
    private JPanel panelGrafica;
    private ListaNoOrdenada listaNoOrdenada;

    private double[] tiempos = new double[5];
    private String[] metodos = {"Intercambio", "Selección", "Inserción", "Shell", "QuickSort"};

    public PanelOrdenamiento(ListaNoOrdenada lista) {
        this.listaNoOrdenada = lista;
        inicializar();
    }

    public PanelOrdenamiento() {
        this.listaNoOrdenada = new com.mycompany.bibliotecamagica.EstructurasBasicas.ListaNoOrdenada();
        inicializar();
    }

    private void inicializar() {
        setTitle("Ordenamiento de Catálogo");
        setSize(800, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(240, 235, 245));
        setLayout(new BorderLayout(10, 10));

        JPanel panelOpciones = new JPanel(new GridLayout(2, 2, 8, 8));
        panelOpciones.setBorder(BorderFactory.createTitledBorder("Opciones de Ordenamiento"));

        comboAtributo = new JComboBox<>(new String[]{"Título", "Autor", "ISBN", "Año"});
        comboMetodo = new JComboBox<>(new String[]{"Intercambio", "Selección", "Inserción", "Shell", "QuickSort"});

        panelOpciones.add(new JLabel("Atributo a ordenar:"));
        panelOpciones.add(comboAtributo);
        panelOpciones.add(new JLabel("Método de ordenamiento:"));
        panelOpciones.add(comboMetodo);

        JPanel panelBotones = new JPanel(new GridLayout(3, 1, 5, 5));
        btnOrdenar = new JButton("Ordenar Catálogo");
        btnComparar = new JButton("Comparar Tiempos");
        btnCerrar = new JButton("Cerrar");
        panelBotones.add(btnOrdenar);
        panelBotones.add(btnComparar);
        panelBotones.add(btnCerrar);

        areaResultado = new JTextArea();
        areaResultado.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaResultado);
        scroll.setBorder(BorderFactory.createTitledBorder("Resultados"));

        panelGrafica = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                dibujarGrafica((Graphics2D) g);
            }
        };
        panelGrafica.setPreferredSize(new Dimension(700, 200));
        panelGrafica.setBackground(Color.WHITE);
        panelGrafica.setBorder(BorderFactory.createTitledBorder("Comparación Visual de Tiempos (ms)"));

        add(panelOpciones, BorderLayout.NORTH);
        add(panelBotones, BorderLayout.EAST);
        add(scroll, BorderLayout.CENTER);
        add(panelGrafica, BorderLayout.SOUTH);

        btnOrdenar.addActionListener(e -> aplicarOrdenamiento());
        btnComparar.addActionListener(e -> compararOrdenamientos());
        btnCerrar.addActionListener(e -> dispose());
    }

    private void aplicarOrdenamiento() {
        if (listaNoOrdenada == null || listaNoOrdenada.getLibros().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El catálogo está vacío.");
            return;
        }

        List<Libro> copia = new ArrayList<>(listaNoOrdenada.getLibros());
        String metodo = comboMetodo.getSelectedItem().toString();
        String atributo = comboAtributo.getSelectedItem().toString();

        Comparator<Libro> cmp = switch (atributo) {
            case "Autor" ->
                BusquedaOrdenamiento.porAutor();
            case "ISBN" ->
                BusquedaOrdenamiento.porISBN();
            case "Año" ->
                BusquedaOrdenamiento.porAnio();
            default ->
                BusquedaOrdenamiento.porTitulo();
        };

        long inicio = System.nanoTime();

        switch (metodo) {
            case "Intercambio" ->
                BusquedaOrdenamiento.intercambio(copia, cmp);
            case "Selección" ->
                BusquedaOrdenamiento.seleccion(copia, cmp);
            case "Inserción" ->
                BusquedaOrdenamiento.insercion(copia, cmp);
            case "Shell" ->
                BusquedaOrdenamiento.shell(copia, cmp);
            case "QuickSort" ->
                BusquedaOrdenamiento.quicksort(copia, cmp);
        }

        long fin = System.nanoTime();
        double ms = (fin - inicio) / 1_000_000.0;

        areaResultado.setText("Catálogo ordenado por " + atributo + " usando " + metodo + ":\n");
        areaResultado.append("Tiempo: " + String.format("%.5f ms\n\n", ms));

        for (Libro l : copia) {
            areaResultado.append(l.getTitulo() + " | " + l.getAutor() + " | " + l.getAnio() + " | " + l.getIsbn() + "\n");
        }
    }

    private void compararOrdenamientos() {
        if (listaNoOrdenada == null || listaNoOrdenada.getLibros().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El catálogo está vacío.");
            return;
        }

        List<Libro> original = new ArrayList<>(listaNoOrdenada.getLibros());
        Comparator<Libro> cmp = BusquedaOrdenamiento.porTitulo();

        for (int i = 0; i < metodos.length; i++) {
            List<Libro> copia = new ArrayList<>(original);
            long inicio = System.nanoTime();

            switch (metodos[i]) {
                case "Intercambio" ->
                    BusquedaOrdenamiento.intercambio(copia, cmp);
                case "Selección" ->
                    BusquedaOrdenamiento.seleccion(copia, cmp);
                case "Inserción" ->
                    BusquedaOrdenamiento.insercion(copia, cmp);
                case "Shell" ->
                    BusquedaOrdenamiento.shell(copia, cmp);
                case "QuickSort" ->
                    BusquedaOrdenamiento.quicksort(copia, cmp);
            }

            long fin = System.nanoTime();
            tiempos[i] = (fin - inicio) / 1_000_000.0;
        }

        areaResultado.setText("Comparación de tiempos (ordenando por título):\n\n");
        for (int i = 0; i < metodos.length; i++) {
            areaResultado.append(String.format("%-12s : %.5f ms\n", metodos[i], tiempos[i]));
        }

        double min = tiempos[0];
        String mejor = metodos[0];
        for (int i = 1; i < tiempos.length; i++) {
            if (tiempos[i] < min) {
                min = tiempos[i];
                mejor = metodos[i];
            }
        }

        areaResultado.append("\nMétodo más eficiente: " + mejor + " (" + String.format("%.5f ms", min) + ")\n");
        panelGrafica.repaint(); // 
    }

    private void dibujarGrafica(Graphics2D g2d) {
        int ancho = getWidth() - 180;
        int alto = 160;
        int x = 50;
        int y = 20;

        double max = 0;
        for (double t : tiempos) {
            if (t > max) {
                max = t;
            }
        }
        if (max == 0) {
            return;
        }

        g2d.setColor(Color.DARK_GRAY);
        g2d.drawLine(x, alto + y, ancho + x, alto + y);

        int barWidth = 100;
        int espacio = 30;

        for (int i = 0; i < tiempos.length; i++) {
            int barHeight = (int) ((tiempos[i] / max) * alto);
            int xPos = x + i * (barWidth + espacio);
            int yPos = (alto + y) - barHeight;

            Color color = switch (i) {
                case 0 ->
                    new Color(135, 206, 250);
                case 1 ->
                    new Color(255, 182, 193);
                case 2 ->
                    new Color(144, 238, 144);
                case 3 ->
                    new Color(255, 255, 153);
                default ->
                    new Color(255, 204, 102);
            };

            g2d.setColor(color);
            g2d.fillRect(xPos, yPos, barWidth, barHeight);

            g2d.setColor(Color.BLACK);
            g2d.drawRect(xPos, yPos, barWidth, barHeight);
            g2d.drawString(metodos[i], xPos + 10, alto + y + 15);
            g2d.drawString(String.format("%.2f", tiempos[i]), xPos + 25, yPos - 5);
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
            java.util.logging.Logger.getLogger(PanelOrdenamiento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PanelOrdenamiento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PanelOrdenamiento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PanelOrdenamiento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PanelOrdenamiento().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
