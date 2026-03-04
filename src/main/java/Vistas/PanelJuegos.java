/*
 * GameVerse Digital - Panel CRUD de Juegos
 * Agregar como tab en jTabbedPane1 o jTabbedPane2 de paginaPrincipal
 */
package Vistas;

import Clases.Juego;
import Clases.JuegoService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * Panel con CRUD completo de Juegos.
 *
 * Cómo usarlo en paginaPrincipal:
 *   PanelJuegos pj = new PanelJuegos(juegoService);
 *   jTabbedPane1.addTab("Admin Juegos", pj);
 */
public class PanelJuegos extends JPanel {

    private static final Color VERDE       = new Color(0, 102, 102);
    private static final Color VERDE_CLARO = new Color(0, 153, 153);
    private static final Color NEGRO       = new Color(0, 0, 0);
    private static final Color BLANCO      = Color.WHITE;
    private static final Color GRIS_CAMPO  = new Color(30, 30, 30);
    private static final Font  FUENTE      = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font  FUENTE_B    = new Font("Segoe UI", Font.BOLD,  13);

    private JuegoService service;

    private JTable            tabla;
    private DefaultTableModel modeloTabla;

    private JTextField txtTitulo, txtPrecio, txtStock, txtBuscar;
    private JComboBox<String> cmbGenero;

    private int idEditando = -1;

    public PanelJuegos(JuegoService service) {
        this.service = service;
        setLayout(new BorderLayout(10, 10));
        setBackground(NEGRO);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        add(panelBusqueda(),   BorderLayout.NORTH);
        add(panelTabla(),      BorderLayout.CENTER);
        add(panelFormulario(), BorderLayout.SOUTH);

        cargarTabla(service.getLista());
    }

    private JPanel panelBusqueda() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        p.setBackground(VERDE);
        p.setBorder(new EmptyBorder(6, 6, 6, 6));

        JLabel lbl = new JLabel("Buscar juego:");
        lbl.setForeground(BLANCO); lbl.setFont(FUENTE_B);

        txtBuscar = campo(160);

        JComboBox<String> cmbFiltroGenero = new JComboBox<>(
            new String[]{"-- Todos --", "Terror", "Accion", "RPG", "Multijugador", "Aventura", "Deportes", "Sandbox"});
        cmbFiltroGenero.setBackground(GRIS_CAMPO);
        cmbFiltroGenero.setForeground(BLANCO);
        cmbFiltroGenero.setFont(FUENTE);

        JButton btnBuscar = boton("Buscar", VERDE_CLARO);
        JButton btnTodos  = boton("Todos",  NEGRO);

        btnBuscar.addActionListener(e -> {
            String q = txtBuscar.getText().trim();
            String g = (String) cmbFiltroGenero.getSelectedItem();
            if (!q.isEmpty()) {
                cargarTabla(service.buscarPorTitulo(q));
            } else if (!"-- Todos --".equals(g)) {
                cargarTabla(service.buscarPorGenero(g));
            } else {
                cargarTabla(service.getLista());
            }
        });
        btnTodos.addActionListener(e -> {
            txtBuscar.setText("");
            cmbFiltroGenero.setSelectedIndex(0);
            cargarTabla(service.getLista());
        });

        p.add(lbl); p.add(txtBuscar);
        p.add(new JLabel("Género:") {{ setForeground(BLANCO); setFont(FUENTE_B); }});
        p.add(cmbFiltroGenero);
        p.add(btnBuscar); p.add(btnTodos);
        return p;
    }

    private JScrollPane panelTabla() {
        String[] cols = {"ID", "Título", "Género", "Precio ($)", "Stock"};
        modeloTabla = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setBackground(VERDE);
        tabla.setForeground(BLANCO);
        tabla.setSelectionBackground(VERDE_CLARO);
        tabla.setSelectionForeground(BLANCO);
        tabla.setGridColor(new Color(0, 80, 80));
        tabla.setFont(FUENTE);
        tabla.setRowHeight(26);
        tabla.getColumnModel().getColumn(0).setMaxWidth(45);
        tabla.getTableHeader().setBackground(VERDE_CLARO);
        tabla.getTableHeader().setForeground(BLANCO);
        tabla.getTableHeader().setFont(FUENTE_B);

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() != -1) {
                int r = tabla.getSelectedRow();
                idEditando = (int) modeloTabla.getValueAt(r, 0);
                txtTitulo.setText((String) modeloTabla.getValueAt(r, 1));
                cmbGenero.setSelectedItem(modeloTabla.getValueAt(r, 2));
                txtPrecio.setText(String.valueOf(modeloTabla.getValueAt(r, 3)));
                txtStock.setText(String.valueOf(modeloTabla.getValueAt(r, 4)));
            }
        });

        JScrollPane sp = new JScrollPane(tabla);
        sp.setPreferredSize(new Dimension(0, 150));
        sp.getViewport().setBackground(VERDE);
        return sp;
    }

    private JPanel panelFormulario() {
        JPanel contenedor = new JPanel(new BorderLayout(0, 6));
        contenedor.setBackground(NEGRO);

        JLabel titulo = new JLabel("  🎮  Datos del Juego");
        titulo.setFont(FUENTE_B); titulo.setForeground(BLANCO);
        titulo.setOpaque(true); titulo.setBackground(VERDE_CLARO);
        titulo.setBorder(new EmptyBorder(6, 8, 6, 8));

        JPanel campos = new JPanel(new GridLayout(2, 4, 8, 8));
        campos.setBackground(NEGRO);
        campos.setBorder(new EmptyBorder(8, 4, 8, 4));

        txtTitulo = campo(0);
        txtPrecio = campo(0);
        txtStock  = campo(0);
        cmbGenero = new JComboBox<>(new String[]{"Terror","Accion","RPG","Multijugador","Aventura","Deportes","Sandbox"});
        cmbGenero.setBackground(GRIS_CAMPO); cmbGenero.setForeground(BLANCO); cmbGenero.setFont(FUENTE);

        campos.add(etiqueta("Título:"));    campos.add(txtTitulo);
        campos.add(etiqueta("Género:"));    campos.add(cmbGenero);
        campos.add(etiqueta("Precio ($):")); campos.add(txtPrecio);
        campos.add(etiqueta("Stock:"));     campos.add(txtStock);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        botones.setBackground(NEGRO);

        JButton btnAgregar    = boton("+ Agregar",    VERDE_CLARO);
        JButton btnActualizar = boton("✎ Actualizar", VERDE);
        JButton btnEliminar   = boton("✕ Eliminar",   new Color(160, 30, 30));
        JButton btnLimpiar    = boton("Limpiar",       new Color(60, 60, 60));

        btnAgregar.addActionListener(e    -> accionAgregar());
        btnActualizar.addActionListener(e -> accionActualizar());
        btnEliminar.addActionListener(e   -> accionEliminar());
        btnLimpiar.addActionListener(e    -> limpiar());

        botones.add(btnLimpiar); botones.add(btnEliminar);
        botones.add(btnActualizar); botones.add(btnAgregar);

        contenedor.add(titulo,  BorderLayout.NORTH);
        contenedor.add(campos,  BorderLayout.CENTER);
        contenedor.add(botones, BorderLayout.SOUTH);
        return contenedor;
    }

    private void accionAgregar() {
        try {
            String titulo = txtTitulo.getText().trim();
            String genero = (String) cmbGenero.getSelectedItem();
            if (titulo.isEmpty()) { error("El título es obligatorio."); return; }
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            int    stock  = Integer.parseInt(txtStock.getText().trim());
            service.agregar(titulo, genero, precio, stock);
            cargarTabla(service.getLista());
            limpiar();
            exito("Juego agregado correctamente.");
        } catch (NumberFormatException ex) { error("Precio y Stock deben ser numéricos."); }
    }

    private void accionActualizar() {
        if (idEditando == -1) { error("Selecciona un juego de la tabla."); return; }
        try {
            String titulo = txtTitulo.getText().trim();
            String genero = (String) cmbGenero.getSelectedItem();
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            int    stock  = Integer.parseInt(txtStock.getText().trim());
            service.actualizar(idEditando, titulo, genero, precio, stock);
            cargarTabla(service.getLista());
            limpiar();
            exito("Juego actualizado correctamente.");
        } catch (NumberFormatException ex) { error("Precio y Stock deben ser numéricos."); }
    }

    private void accionEliminar() {
        if (idEditando == -1) { error("Selecciona un juego de la tabla."); return; }
        int c = JOptionPane.showConfirmDialog(this, "¿Eliminar el juego seleccionado?",
            "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (c == JOptionPane.YES_OPTION) {
            service.eliminar(idEditando);
            cargarTabla(service.getLista());
            limpiar();
            exito("Juego eliminado.");
        }
    }

    private void cargarTabla(ArrayList<Juego> lista) {
        modeloTabla.setRowCount(0);
        for (Juego j : lista)
            modeloTabla.addRow(new Object[]{j.getId(), j.getTitulo(), j.getGenero(), j.getPrecio(), j.getStock()});
    }

    private void limpiar() {
        txtTitulo.setText(""); txtPrecio.setText(""); txtStock.setText("");
        cmbGenero.setSelectedIndex(0); idEditando = -1; tabla.clearSelection();
    }

    private JTextField campo(int ancho) {
        JTextField tf = new JTextField();
        tf.setFont(FUENTE); tf.setBackground(GRIS_CAMPO);
        tf.setForeground(BLANCO); tf.setCaretColor(BLANCO);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(VERDE, 1),
            new EmptyBorder(4, 6, 4, 6)));
        if (ancho > 0) tf.setPreferredSize(new Dimension(ancho, 30));
        return tf;
    }

    private JLabel etiqueta(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(FUENTE_B); l.setForeground(BLANCO);
        return l;
    }

    private JButton boton(String texto, Color bg) {
        JButton b = new JButton(texto);
        b.setFont(FUENTE_B); b.setBackground(bg);
        b.setForeground(BLANCO); b.setFocusPainted(false);
        b.setBorderPainted(false); b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(7, 14, 7, 14));
        return b;
    }

    private void error(String msg) { JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE); }
    private void exito(String msg) { JOptionPane.showMessageDialog(this, msg, "Éxito", JOptionPane.INFORMATION_MESSAGE); }

    public void refrescar() { cargarTabla(service.getLista()); }
}
