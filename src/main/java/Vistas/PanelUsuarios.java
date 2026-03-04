/*
 * GameVerse Digital - Panel CRUD de Usuarios
 * Insertar este panel dentro del jTabbedPane2 (tab "Usuario") de paginaPrincipal
 */
package Vistas;

import Clases.TipoCliente;
import Clases.Usuario;
import Clases.UsuarioService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * Panel reutilizable con CRUD completo de Usuarios.
 * Usa la misma paleta de color del proyecto (verde #006666 / negro).
 *
 * Cómo usarlo en paginaPrincipal:
 *   PanelUsuarios pu = new PanelUsuarios(usuarioService);
 *   jTabbedPane2.addTab("Gestión Clientes", pu);
 */
public class PanelUsuarios extends JPanel {

    // ── Paleta del proyecto ──────────────────────────────────────────
    private static final Color VERDE       = new Color(0, 102, 102);
    private static final Color VERDE_CLARO = new Color(0, 153, 153);
    private static final Color NEGRO       = new Color(0, 0, 0);
    private static final Color BLANCO      = Color.WHITE;
    private static final Color GRIS_CAMPO  = new Color(30, 30, 30);
    private static final Font  FUENTE      = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font  FUENTE_B    = new Font("Segoe UI", Font.BOLD,  13);

    private UsuarioService service;

    // Tabla
    private JTable             tabla;
    private DefaultTableModel  modeloTabla;

    // Campos formulario
    private JTextField txtIdentificacion, txtNombre, txtCiudad, txtEdad, txtCodigo;
    private JComboBox<TipoCliente> cmbTipo;

    // Búsqueda
    private JTextField txtBuscar;

    // Estado edición
    private String codigoEditando = null;

    // ────────────────────────────────────────────────────────────────
    public PanelUsuarios(UsuarioService service) {
        this.service = service;
        setLayout(new BorderLayout(10, 10));
        setBackground(NEGRO);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        add(panelBusqueda(),   BorderLayout.NORTH);
        add(panelTabla(),      BorderLayout.CENTER);
        add(panelFormulario(), BorderLayout.SOUTH);

        cargarTabla(service.getLista());
    }

    // ── Barra de búsqueda ────────────────────────────────────────────
    private JPanel panelBusqueda() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        p.setBackground(VERDE);
        p.setBorder(new EmptyBorder(6, 6, 6, 6));

        JLabel lbl = new JLabel("Buscar cliente:");
        lbl.setForeground(BLANCO); lbl.setFont(FUENTE_B);

        txtBuscar = campo(160);

        JButton btnBuscar = boton("Buscar", VERDE_CLARO);
        JButton btnTodos  = boton("Mostrar todos", NEGRO);

        btnBuscar.addActionListener(e -> {
            String q = txtBuscar.getText().trim();
            cargarTabla(q.isEmpty() ? service.getLista() : service.buscarPorNombre(q));
        });
        btnTodos.addActionListener(e -> {
            txtBuscar.setText("");
            cargarTabla(service.getLista());
        });

        p.add(lbl); p.add(txtBuscar); p.add(btnBuscar); p.add(btnTodos);
        return p;
    }

    // ── Tabla ────────────────────────────────────────────────────────
    private JScrollPane panelTabla() {
        String[] cols = {"Identificación", "Nombre", "Ciudad", "Edad", "Tipo", "Código"};
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
        tabla.getTableHeader().setBackground(VERDE_CLARO);
        tabla.getTableHeader().setForeground(BLANCO);
        tabla.getTableHeader().setFont(FUENTE_B);

        // Al seleccionar fila → cargar formulario
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() != -1) {
                int r = tabla.getSelectedRow();
                txtIdentificacion.setText((String) modeloTabla.getValueAt(r, 0));
                txtNombre.setText((String) modeloTabla.getValueAt(r, 1));
                txtCiudad.setText((String) modeloTabla.getValueAt(r, 2));
                txtEdad.setText(String.valueOf(modeloTabla.getValueAt(r, 3)));
                cmbTipo.setSelectedItem(modeloTabla.getValueAt(r, 4));
                txtCodigo.setText((String) modeloTabla.getValueAt(r, 5));
                codigoEditando = (String) modeloTabla.getValueAt(r, 5);
                txtCodigo.setEditable(false); // no editar código al actualizar
            }
        });

        JScrollPane sp = new JScrollPane(tabla);
        sp.setPreferredSize(new Dimension(0, 160));
        sp.getViewport().setBackground(VERDE);
        return sp;
    }

    // ── Formulario ───────────────────────────────────────────────────
    private JPanel panelFormulario() {
        JPanel contenedor = new JPanel(new BorderLayout(0, 6));
        contenedor.setBackground(NEGRO);

        // Título formulario
        JLabel titulo = new JLabel("  ✏  Datos del Cliente");
        titulo.setFont(FUENTE_B);
        titulo.setForeground(BLANCO);
        titulo.setOpaque(true);
        titulo.setBackground(VERDE_CLARO);
        titulo.setBorder(new EmptyBorder(6, 8, 6, 8));

        // Campos en grid
        JPanel campos = new JPanel(new GridLayout(3, 4, 8, 8));
        campos.setBackground(NEGRO);
        campos.setBorder(new EmptyBorder(8, 4, 8, 4));

        txtIdentificacion = campo(0);
        txtNombre         = campo(0);
        txtCiudad         = campo(0);
        txtEdad           = campo(0);
        txtCodigo         = campo(0);
        cmbTipo = new JComboBox<>(TipoCliente.values());
        cmbTipo.setBackground(GRIS_CAMPO);
        cmbTipo.setForeground(BLANCO);
        cmbTipo.setFont(FUENTE);

        campos.add(etiqueta("Identificación:")); campos.add(txtIdentificacion);
        campos.add(etiqueta("Nombre:"));         campos.add(txtNombre);
        campos.add(etiqueta("Ciudad origen:"));  campos.add(txtCiudad);
        campos.add(etiqueta("Edad:"));           campos.add(txtEdad);
        campos.add(etiqueta("Tipo cliente:"));   campos.add(cmbTipo);
        campos.add(etiqueta("Código cliente:")); campos.add(txtCodigo);

        // Botones acción
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

    // ── Acciones CRUD ────────────────────────────────────────────────
    private void accionAgregar() {
        try {
            String id    = txtIdentificacion.getText().trim();
            String nom   = txtNombre.getText().trim();
            String ciu   = txtCiudad.getText().trim();
            int    edad  = Integer.parseInt(txtEdad.getText().trim());
            TipoCliente tipo = (TipoCliente) cmbTipo.getSelectedItem();
            String cod   = txtCodigo.getText().trim();

            if (id.isEmpty() || nom.isEmpty() || cod.isEmpty()) {
                error("Identificación, Nombre y Código son obligatorios."); return;
            }
            if (service.buscarPorCodigo(cod) != null) {
                error("Ya existe un cliente con ese código."); return;
            }
            service.agregar(id, nom, ciu, edad, tipo, cod);
            cargarTabla(service.getLista());
            limpiar();
            exito("Cliente agregado correctamente.");
        } catch (NumberFormatException ex) { error("La edad debe ser un número."); }
    }

    private void accionActualizar() {
        if (codigoEditando == null) { error("Selecciona un cliente de la tabla."); return; }
        try {
            String nom  = txtNombre.getText().trim();
            String ciu  = txtCiudad.getText().trim();
            int    edad = Integer.parseInt(txtEdad.getText().trim());
            TipoCliente tipo = (TipoCliente) cmbTipo.getSelectedItem();
            service.actualizar(codigoEditando, nom, ciu, edad, tipo);
            cargarTabla(service.getLista());
            limpiar();
            exito("Cliente actualizado correctamente.");
        } catch (NumberFormatException ex) { error("La edad debe ser un número."); }
    }

    private void accionEliminar() {
        if (codigoEditando == null) { error("Selecciona un cliente de la tabla."); return; }
        int c = JOptionPane.showConfirmDialog(this,
            "¿Eliminar el cliente seleccionado?", "Confirmar",
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (c == JOptionPane.YES_OPTION) {
            service.eliminar(codigoEditando);
            cargarTabla(service.getLista());
            limpiar();
            exito("Cliente eliminado.");
        }
    }

    // ── Helpers ──────────────────────────────────────────────────────
    private void cargarTabla(ArrayList<Usuario> lista) {
        modeloTabla.setRowCount(0);
        for (Usuario u : lista)
            modeloTabla.addRow(new Object[]{
                u.getIdentificacion(), u.getNombre(), u.getCiudadOrigen(),
                u.getEdad(), u.getTipoCliente(), u.getCodigoCliente()
            });
    }

    private void limpiar() {
        txtIdentificacion.setText(""); txtNombre.setText("");
        txtCiudad.setText(""); txtEdad.setText(""); txtCodigo.setText("");
        cmbTipo.setSelectedIndex(0);
        txtCodigo.setEditable(true);
        codigoEditando = null;
        tabla.clearSelection();
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

    private void error(String msg)  { JOptionPane.showMessageDialog(this, msg, "Error",  JOptionPane.ERROR_MESSAGE); }
    private void exito(String msg)  { JOptionPane.showMessageDialog(this, msg, "Éxito",  JOptionPane.INFORMATION_MESSAGE); }

    /** Permite refrescar la tabla desde paginaPrincipal cuando sea necesario */
    public void refrescar() { cargarTabla(service.getLista()); }
}
