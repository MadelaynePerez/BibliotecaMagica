/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.bibliotecamagica.Vistas;

import com.mycompany.bibliotecamagica.Arboles.Biblioteca;
import com.mycompany.bibliotecamagica.Arboles.Libro;
import com.mycompany.bibliotecamagica.EstructurasBasicas.Grafo;
import com.mycompany.bibliotecamagica.EstructurasBasicas.HashTableISBN;
import com.mycompany.bibliotecamagica.SimuladorCompleto;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;
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
public class PanelDespacho extends javax.swing.JFrame {

    /**
     * Creates new form PanelDespacho
     */
    public PanelDespacho() {
        initComponents();
    }

    private JComboBox<String> comboOrigen, comboDestino;
    private JTextField txtTitulo, txtAutor, txtIsbn, txtAnio, txtGenero;
    private JTextArea areaLog;
    private JButton btnTiempo, btnCosto, btnVerGrafo, btnVerColas, btnCerrar;

    private Grafo grafo;
    private Map<String, Biblioteca> bibliotecas;
    private SimuladorCompleto simulador;

    public PanelDespacho(Grafo grafo, Map<String, Biblioteca> bibliotecas) {
        this.grafo = grafo;
        this.bibliotecas = bibliotecas;
        this.simulador = new SimuladorCompleto(grafo, new HashTableISBN(), bibliotecas);
        this.simulador.iniciarHilos();
        inicializar();
    }

    public PanelDespacho() {
        this(new Grafo(), new java.util.HashMap<>());
    }

    private void inicializar() {
        setTitle("Sistema de despacho de libros");
        setSize(850, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        
        JPanel panelSup = new JPanel(new GridLayout(4, 4, 5, 5));
        panelSup.setBorder(BorderFactory.createTitledBorder("Datos de transferencia"));

        comboOrigen = new JComboBox<>();
        comboDestino = new JComboBox<>();

       
        for (Map.Entry<String, Biblioteca> entry : this.bibliotecas.entrySet()) {
            comboOrigen.addItem(entry.getKey());
            comboDestino.addItem(entry.getKey());
        }

        txtTitulo = new JTextField();
        txtAutor = new JTextField();
        txtIsbn = new JTextField();
        txtAnio = new JTextField();
        txtGenero = new JTextField();

        panelSup.add(new JLabel("Origen:"));
        panelSup.add(comboOrigen);
        panelSup.add(new JLabel("Destino:"));
        panelSup.add(comboDestino);

        panelSup.add(new JLabel("Título:"));
        panelSup.add(txtTitulo);
        panelSup.add(new JLabel("Autor:"));
        panelSup.add(txtAutor);

        panelSup.add(new JLabel("ISBN:"));
        panelSup.add(txtIsbn);
        panelSup.add(new JLabel("Año:"));
        panelSup.add(txtAnio);
        panelSup.add(new JLabel("Género:"));
        panelSup.add(txtGenero);

        // ---------- botones ----------
        JPanel panelBtns = new JPanel(new GridLayout(5, 1, 5, 5));
        btnTiempo = new JButton("Transferir por TIEMPO");
        btnCosto = new JButton("Transferir por COSTO");
        btnVerGrafo = new JButton("Generar Grafo");
        btnVerColas = new JButton("Ver Estado de Colas");
        btnCerrar = new JButton("Cerrar");

        panelBtns.add(btnTiempo);
        panelBtns.add(btnCosto);
        panelBtns.add(btnVerGrafo);
        panelBtns.add(btnVerColas);
        panelBtns.add(btnCerrar);

        // ---------- área de log ----------
        areaLog = new JTextArea();
        areaLog.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaLog);
        scroll.setBorder(BorderFactory.createTitledBorder("Eventos del sistema"));

        add(panelSup, BorderLayout.NORTH);
        add(panelBtns, BorderLayout.EAST);
        add(scroll, BorderLayout.CENTER);

        btnTiempo.addActionListener(e -> transferir(true));
        btnCosto.addActionListener(e -> transferir(false));
        btnVerGrafo.addActionListener(e -> generarImagenGrafo());
        btnVerColas.addActionListener(e -> mostrarEstadoColas());
        btnCerrar.addActionListener(e -> dispose());
    }

   
    private void transferir(boolean porTiempo) {
        try {
            String origenNombre = comboOrigen.getSelectedItem().toString();
            String destinoNombre = comboDestino.getSelectedItem().toString();

            if (origenNombre.equals(destinoNombre)) {
                JOptionPane.showMessageDialog(this, "Seleccione bibliotecas distintas.");
                return;
            }

            Biblioteca origen = bibliotecas.get(origenNombre);
            Biblioteca destino = bibliotecas.get(destinoNombre);

            if (origen == null || destino == null) {
                JOptionPane.showMessageDialog(this, "Alguna de las bibliotecas no existe en el mapa.");
                return;
            }

            String titulo = txtTitulo.getText().trim();
            String autor = txtAutor.getText().trim();
            String isbn = txtIsbn.getText().trim();
            String genero = txtGenero.getText().trim();
            int anio = Integer.parseInt(txtAnio.getText().trim());

            Libro libro = new Libro(titulo, autor, isbn, anio, genero);

            areaLog.append(" Iniciando transferencia de \"" + titulo + "\"\n");
            areaLog.append("Desde " + origenNombre + " hasta " + destinoNombre + "\n");

            List<String> ruta = grafo.encontrarRuta(origenNombre, destinoNombre, porTiempo);
            areaLog.append("Ruta: " + ruta + "\n\n");

           
            Thread hilo = new Thread(() -> {
                simulador.transferirLibro(origenNombre, destinoNombre, libro, porTiempo);
            });
            hilo.start();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error en transferencia: " + ex.getMessage());
        }
    }

   
    private void generarImagenGrafo() {
        try {
            File dot = new File("grafo.dot");
            FileWriter fw = new FileWriter(dot);
            fw.write("digraph G {\n");
            for (var e : grafo.getAdyacencia().entrySet()) {
                for (var a : e.getValue()) {
                    fw.write("  \"" + e.getKey() + "\" -> \"" + a.destino
                            + "\" [label=\"t:" + a.tiempo + ", c:" + a.costo + "\"];\n");
                }
            }
            fw.write("}\n");
            fw.close();
            Runtime.getRuntime().exec("dot -Tpng grafo.dot -o grafo.png");
            JOptionPane.showMessageDialog(this, " Se generó 'grafo.png' en la carpeta del proyecto.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al generar el grafo: " + ex.getMessage());
        }
    }

    private void mostrarEstadoColas() {
        StringBuilder sb = new StringBuilder();
        for (Biblioteca b : bibliotecas.values()) {
            sb.append("\n=== ").append(b.getNombre()).append(" ===\n");
            sb.append("📥 Ingreso: ").append(b.getColaIngreso().size()).append("\n");
            sb.append("⚙️ Traspaso: ").append(b.getColaTraspaso().size()).append("\n");
            sb.append("📤 Salida: ").append(b.getColaSalida().size()).append("\n");
            sb.append("🧱 Pila: ").append(b.getPilaSalida().getNumeroDeElementos()).append("\n");
        }
        areaLog.setText(sb.toString());
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
            java.util.logging.Logger.getLogger(PanelDespacho.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PanelDespacho.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PanelDespacho.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PanelDespacho.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PanelDespacho().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
