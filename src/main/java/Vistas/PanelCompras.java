/*
 * GameVerse Digital - Panel CRUD de Compras
 */
package Vistas;

import Clases.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * Panel con CRUD de Compras. Relaciona Usuario y Juego.
 *
 * Cómo usarlo:
 *   PanelCompras pc = new PanelCompras(compraService, usuarioService, juegoService);
 *   jTabbedPane1.addTab("Compras Admin", pc);
 */
public class PanelCompras extends JPanel {

    private static final Color VERDE       = new Color(0, 102, 102);
    private static final Color VERDE_CLARO = new Color(0, 153, 153);
    private static final Color NEGRO       = new Color(0, 0, 0);
    private static final Color BLANCO      = Color.WHITE;
    private static final Color GRIS_CAMPO  = new Color(30, 30, 30);
    private static final Font  FUENTE      = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font  FUENTE_B    = new Font("Segoe UI", Font.BOLD,  13);

    private CompraService  compraService;
    private UsuarioService usuarioService;
    private JuegoService   juegoService;

    private JTable            tabla;
    private DefaultTableModel modeloTabla;

    private JComboBox<String> cmbUsuario, cmbJuego;
    private JSpinner          spinCantidad;
    private JLabel            lblTotal;
    private int               idCompraSeleccionada = -1;

    public PanelCompras(CompraService cs, UsuarioService us, JuegoService js) {
        this.compraService  = cs;
        this.usuarioService = us;
        this.juegoService   = js;

        setLayout(new BorderLayout(10, 10));
        setBackground(NEGRO);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        add(panelFiltro(),     BorderLayout.NORTH);
        add(panelTabla(),      BorderLayout.CENTER);
        add(panelFormulario(), BorderLayout.SOUTH);

        cargarCombos();
        cargarTabla(compraService.getLista());
    }

    private JPanel panelFiltro() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        p.setBackground(VERDE);
        p.setBorder(new EmptyBorder(6, 6, 6, 6));

        JLabel lbl = new JLabel("Filtrar por cliente:");
        lbl.setForeground(BLANCO); lbl.setFont(FUENTE_B);

        JComboBox<String> cmbFiltro = new JComboBox<>();
        cmbFiltro.addItem("-- Todos --");
        for (Usuario u : usuarioService.getLista())
            cmbFiltro.addItem(u.getCodigoCliente() + " - " + u.getNombre());
        cmbFiltro.setBackground(GRIS_CAMPO); cmbFiltro.setForeground(BLANCO); cmbFiltro.setFont(FUENTE);

        JButton btnFiltrar = boton("Filtrar", VERDE_CLARO);
        JButton btnTodos   = boton("Todos",   NEGRO);

        btnFiltrar.addActionListener(e -> {
            int idx = cmbFiltro.getSelectedIndex();
            if (idx == 0) { cargarTabla(compraService.getLista()); return; }
            String cod = usuarioService.getLista().get(idx - 1).getCodigoCliente();
            cargarTabla(compraService.buscarPorUsuario(cod));
        });
        btnTodos.addActionListener(e -> { cmbFiltro.setSelectedIndex(0); cargarTabla(compraService.getLista()); });

        p.add(lbl); p.add(cmbFiltro); p.add(btnFiltrar); p.add(btnTodos);
        return p;
    }

    private JScrollPane panelTabla() {
        String[] cols = {"ID", "Cliente", "Juego", "Cantidad", "Total ($)", "Fecha"};
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
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() != -1)
                idCompraSeleccionada = (int) modeloTabla.getValueAt(tabla.getSelectedRow(), 0);
        });

        JScrollPane sp = new JScrollPane(tabla);
        sp.setPreferredSize(new Dimension(0, 140));
        sp.getViewport().setBackground(VERDE);
        return sp;
    }

    private JPanel panelFormulario() {
        JPanel contenedor = new JPanel(new BorderLayout(0, 6));
        contenedor.setBackground(NEGRO);

        JLabel titulo = new JLabel("  🛒  Nueva Compra");
        titulo.setFont(FUENTE_B); titulo.setForeground(BLANCO);
        titulo.setOpaque(true); titulo.setBackground(VERDE_CLARO);
        titulo.setBorder(new EmptyBorder(6, 8, 6, 8));

        JPanel campos = new JPanel(new GridLayout(2, 4, 8, 8));
        campos.setBackground(NEGRO);
        campos.setBorder(new EmptyBorder(8, 4, 8, 4));

        cmbUsuario   = new JComboBox<>();
        cmbJuego     = new JComboBox<>();
        spinCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
        lblTotal     = new JLabel("Total: $0.00");

        estilizarCombo(cmbUsuario); estilizarCombo(cmbJuego);
        spinCantidad.setFont(FUENTE);
        spinCantidad.setBackground(GRIS_CAMPO);
        lblTotal.setFont(FUENTE_B); lblTotal.setForeground(VERDE_CLARO);

        cmbJuego.addActionListener(e -> actualizarTotal());
        spinCantidad.addChangeListener(e -> actualizarTotal());

        campos.add(etiqueta("Cliente:"));   campos.add(cmbUsuario);
        campos.add(etiqueta("Juego:"));     campos.add(cmbJuego);
        campos.add(etiqueta("Cantidad:"));  campos.add(spinCantidad);
        campos.add(new JLabel());           campos.add(lblTotal);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        botones.setBackground(NEGRO);

        JButton btnRegistrar = boton("✔ Registrar Compra", VERDE_CLARO);
        JButton btnCancelar  = boton("✕ Cancelar Compra",  new Color(160, 30, 30));

        btnRegistrar.addActionListener(e -> accionRegistrar());
        btnCancelar.addActionListener(e  -> accionCancelar());

        botones.add(btnCancelar); botones.add(btnRegistrar);

        contenedor.add(titulo,  BorderLayout.NORTH);
        contenedor.add(campos,  BorderLayout.CENTER);
        contenedor.add(botones, BorderLayout.SOUTH);
        return contenedor;
    }

    private void accionRegistrar() {
        int idxU = cmbUsuario.getSelectedIndex();
        int idxJ = cmbJuego.getSelectedIndex();
        if (idxU < 0 || idxJ < 0) { error("Selecciona cliente y juego."); return; }

        String codCliente = usuarioService.getLista().get(idxU).getCodigoCliente();
        int    idJuego    = juegoService.getLista().get(idxJ).getId();
        int    cantidad   = (int) spinCantidad.getValue();

        String res = compraService.registrar(usuarioService, juegoService, codCliente, idJuego, cantidad);
        if ("OK".equals(res)) {
            cargarTabla(compraService.getLista());
            cargarCombos(); // refrescar stock en combo
            spinCantidad.setValue(1);
            exito("Compra registrada exitosamente.");
        } else { error(res); }
    }

    private void accionCancelar() {
        if (idCompraSeleccionada == -1) { error("Selecciona una compra de la tabla."); return; }
        int c = JOptionPane.showConfirmDialog(this,
            "¿Cancelar la compra #" + idCompraSeleccionada + "? Se restaurará el stock.",
            "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (c == JOptionPane.YES_OPTION) {
            compraService.cancelar(idCompraSeleccionada, juegoService);
            cargarTabla(compraService.getLista());
            cargarCombos();
            idCompraSeleccionada = -1;
            exito("Compra cancelada. Stock restaurado.");
        }
    }

    private void actualizarTotal() {
        int idx = cmbJuego.getSelectedIndex();
        if (idx >= 0 && idx < juegoService.getLista().size()) {
            double precio = juegoService.getLista().get(idx).getPrecio();
            lblTotal.setText(String.format("Total: $%.2f", precio * (int) spinCantidad.getValue()));
        }
    }

    private void cargarCombos() {
        cmbUsuario.removeAllItems();
        for (Usuario u : usuarioService.getLista())
            cmbUsuario.addItem(u.getCodigoCliente() + " - " + u.getNombre());
        cmbJuego.removeAllItems();
        for (Juego j : juegoService.getLista())
            cmbJuego.addItem(j.getTitulo() + "  ($" + j.getPrecio() + ")  Stock:" + j.getStock());
    }

    private void cargarTabla(ArrayList<Compra> lista) {
        modeloTabla.setRowCount(0);
        for (Compra c : lista)
            modeloTabla.addRow(new Object[]{
                c.getId(), c.getUsuario().getNombre(), c.getJuego().getTitulo(),
                c.getCantidad(), String.format("%.2f", c.getTotal()), c.getFecha()
            });
    }

    private void estilizarCombo(JComboBox<String> c) {
        c.setFont(FUENTE); c.setBackground(GRIS_CAMPO); c.setForeground(BLANCO);
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

    public void refrescar() { cargarCombos(); cargarTabla(compraService.getLista()); }
}
