/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.bibliotecamagica.Vistas;

import com.mycompany.bibliotecamagica.Arboles.Biblioteca;
import com.mycompany.bibliotecamagica.Arboles.Libro;
import com.mycompany.bibliotecamagica.CatalogoGlobal;
import com.mycompany.bibliotecamagica.EstructurasBasicas.HashTableISBN;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
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
public class PanelLibros extends javax.swing.JFrame {

    /**
     * Creates new form PanelLibros
     */
    private final Map<String, Biblioteca> bibliotecas;
    private final HashTableISBN hash;

    private JTextField txtTitulo, txtAutor, txtIsbn, txtAnio, txtGenero, txtEstado;
    private JComboBox<String> comboOrigen, comboDestino, comboPrioridad;
    private JTextArea areaLog;

    public PanelLibros(Map<String, Biblioteca> bibliotecas, HashTableISBN hash) {
        this.bibliotecas = bibliotecas;
        this.hash = hash;
        initUI();
        llenarCombosBibliotecas();
    }

    private void initUI() {
        setTitle("Gestión de Libros");
        setSize(700, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel panelCampos = new JPanel(new GridLayout(8, 2, 10, 10));
        panelCampos.setBorder(BorderFactory.createTitledBorder("Datos del Libro"));

        txtTitulo = new JTextField();
        txtAutor = new JTextField();
        txtIsbn = new JTextField();
        txtAnio = new JTextField();
        txtGenero = new JTextField();
        txtEstado = new JTextField();

        comboOrigen = new JComboBox<>();
        comboDestino = new JComboBox<>();
        comboPrioridad = new JComboBox<>(new String[]{"Alta", "Media", "Baja"});

        panelCampos.add(new JLabel("Título:"));
        panelCampos.add(txtTitulo);
        panelCampos.add(new JLabel("Autor:"));
        panelCampos.add(txtAutor);
        panelCampos.add(new JLabel("ISBN:"));
        panelCampos.add(txtIsbn);
        panelCampos.add(new JLabel("Año:"));
        panelCampos.add(txtAnio);
        panelCampos.add(new JLabel("Género:"));
        panelCampos.add(txtGenero);
        panelCampos.add(new JLabel("Estado:"));
        panelCampos.add(txtEstado);
        panelCampos.add(new JLabel("Origen:"));
        panelCampos.add(comboOrigen);
        panelCampos.add(new JLabel("Destino:"));
        panelCampos.add(comboDestino);

        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnAgregar = new JButton("Agregar Libro");
        JButton btnBuscar = new JButton("Buscar Libro");
        JButton btnEliminar = new JButton("Eliminar Libro");
        JButton btnCerrar = new JButton("Cerrar");

        panelBotones.add(btnAgregar);
        panelBotones.add(btnBuscar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnCerrar);

        areaLog = new JTextArea();
        areaLog.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaLog);
        scroll.setBorder(BorderFactory.createTitledBorder("Resultado"));

        add(panelCampos, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        btnAgregar.addActionListener(this::agregarLibro);
        btnBuscar.addActionListener(this::buscarLibro);
        btnEliminar.addActionListener(this::eliminarLibro);
        btnCerrar.addActionListener(e -> dispose());
    }

    private void llenarCombosBibliotecas() {
        comboOrigen.removeAllItems();
        comboDestino.removeAllItems();

        if (bibliotecas == null || bibliotecas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay bibliotecas cargadas aún.");
            return;
        }

        for (Biblioteca b : bibliotecas.values()) {
            comboOrigen.addItem(b.getId() + " - " + b.getNombre());
            comboDestino.addItem(b.getId() + " - " + b.getNombre());
        }
    }

    private void agregarLibro(ActionEvent e) {
        try {
            String titulo = txtTitulo.getText().trim();
            String autor = txtAutor.getText().trim();
            String isbn = txtIsbn.getText().trim();
            String genero = txtGenero.getText().trim();
            String estado = txtEstado.getText().trim();
            int anio = Integer.parseInt(txtAnio.getText().trim());

            if (titulo.isEmpty() || autor.isEmpty() || isbn.isEmpty() || genero.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos.");
                return;
            }

            Libro libro = new Libro(titulo, autor, isbn, anio, genero);
            libro.setEstado(estado);

            hash.insertar(isbn, libro);

            String seleccionado = (String) comboOrigen.getSelectedItem();
            if (seleccionado == null || seleccionado.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Seleccione una biblioteca de origen.");
                return;
            }

            String idBiblioteca = seleccionado.split(" - ")[0].trim();

            Biblioteca b = bibliotecas.get(idBiblioteca);
            if (b == null) {
                JOptionPane.showMessageDialog(this, "Biblioteca no encontrada: " + idBiblioteca);
                return;
            }

            b.insertarLibro(libro);

            areaLog.append("Libro agregado a " + b.getNombre() + ": " + titulo + " (" + isbn + ")\n");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar libro: " + ex.getMessage());
        }
    }

    private void buscarLibro(ActionEvent e) {
        String dato = JOptionPane.showInputDialog(this, "Ingrese Título, ISBN, Género o Año:");
        if (dato == null || dato.trim().isEmpty()) {
            return;
        }
        dato = dato.trim();

        Libro libro = (Libro) hash.buscar(dato);
        if (libro != null) {
            mostrarLibro(libro, "HashTable ISBN");
            return;
        }

        for (Biblioteca b : bibliotecas.values()) {
            Libro encontradoAVL = b.getArbolAvl().buscar(dato);
            if (encontradoAVL != null) {
                mostrarLibro(encontradoAVL, "Árbol AVL (" + b.getNombre() + ")");
                return;
            }

            try {
                int anio = Integer.parseInt(dato);
                Libro encontradoB = b.getArbolB().buscarPorAnio(anio);
                if (encontradoB != null) {
                    mostrarLibro(encontradoB, "Árbol B (" + b.getNombre() + ")");
                    return;
                }
            } catch (NumberFormatException ex) {

            }

            var listaGenero = b.getArbolBMas().buscarPorGenero(dato);
            if (!listaGenero.isEmpty()) {
                for (Libro l : listaGenero) {
                    mostrarLibro(l, "Árbol B+ (" + b.getNombre() + ")");
                }
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "No se encontró ningún libro con ese dato.");
    }

    private void eliminarLibro(java.awt.event.ActionEvent e) {
        String dato = JOptionPane.showInputDialog(this, "Ingrese Título, ISBN, Género o Año para eliminar:");
        if (dato == null || dato.trim().isEmpty()) {
            return;
        }

        dato = dato.trim();
        Integer anioBuscado = null;
        try {
            anioBuscado = Integer.parseInt(dato.replaceAll("\\D", ""));
        } catch (Exception ignore) {
        }

        HashTableISBN.Nodo[] tabla = hash.getTabla();
        java.util.List<Libro> aEliminar = new java.util.ArrayList<>();

        for (int i = 0; i < hash.getTamaño(); i++) {
            HashTableISBN.Nodo nodo = tabla[i];
            while (nodo != null) {
                Object val = nodo.valor;
                if (val instanceof com.mycompany.bibliotecamagica.Arboles.Libro) {
                    Libro l = (Libro) val;

                    boolean match = false;
                    if (l.getIsbn() != null && l.getIsbn().equalsIgnoreCase(dato)) {
                        match = true;
                    } else if (l.getTitulo() != null && l.getTitulo().equalsIgnoreCase(dato)) {
                        match = true;
                    } else if (l.getGenero() != null && l.getGenero().equalsIgnoreCase(dato)) {
                        match = true;
                    } else if (anioBuscado != null && l.getAnio() == anioBuscado) {
                        match = true;
                    }

                    if (match) {
                        aEliminar.add(l);
                    }
                }
                nodo = nodo.siguiente;
            }
        }

        if (aEliminar.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontró el libro para eliminar.");
            return;
        }

        int eliminados = 0;
        for (Libro l : aEliminar) {
            boolean eliminadoAlguno = false;

            try {
                if (l.getIsbn() != null) {
                    boolean ok = hash.eliminar(l.getIsbn());
                    eliminadoAlguno = ok || eliminadoAlguno;
                }
            } catch (Exception ignore) {
            }

            for (Biblioteca b : bibliotecas.values()) {
                try {
                    b.getArbolAvl().eliminar(l.getTitulo());
                    eliminadoAlguno = true;
                } catch (Exception ignore) {
                }

                try {
                    b.getArbolB().eliminarPorAnio(l.getAnio(), l.getTitulo());
                    eliminadoAlguno = true;
                } catch (Exception ignore) {
                }

                try {

                } catch (Exception ignore) {
                }
            }

            if (eliminadoAlguno) {
                eliminados++;
                areaLog.append("Libro eliminado: " + l.getTitulo() + " (" + l.getIsbn() + ")\n");
            }
        }

        if (eliminados == 0) {
            JOptionPane.showMessageDialog(this, "No se pudo eliminar el libro (no se encontró en las estructuras).");
        } else {
            areaLog.append("--------------------------------------\n");
        }
    }

    private void mostrarLibro(Libro l, String tipo) {
        areaLog.append("Encontrado por " + tipo + ":\n");
        areaLog.append("Título: " + l.getTitulo() + "\n");
        areaLog.append("Autor: " + l.getAutor() + "\n");
        areaLog.append("ISBN: " + l.getIsbn() + "\n");
        areaLog.append("Género: " + l.getGenero() + "\n");
        areaLog.append("Año: " + l.getAnio() + "\n");
        areaLog.append("--------------------------------------\n");
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
