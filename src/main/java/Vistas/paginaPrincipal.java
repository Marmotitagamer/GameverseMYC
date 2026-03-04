/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vistas;
import java.util.LinkedList;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
/**
 *
 * @author Mflas
 */
public class paginaPrincipal extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(paginaPrincipal.class.getName());
    private Clases.JuegoService   juegoService   = new Clases.JuegoService();
    private Clases.UsuarioService usuarioService = new Clases.UsuarioService();
    private Clases.CompraService  compraService  = new Clases.CompraService();
    /**
     * Creates new form paginaPrincipal
     */

    
    public paginaPrincipal() {
        initComponents();
        // Paneles CRUD
        Vistas.PanelJuegos   panelJuegos   = new Vistas.PanelJuegos(juegoService);
        Vistas.PanelUsuarios panelUsuarios = new Vistas.PanelUsuarios(usuarioService);
        Vistas.PanelCompras  panelCompras  = new Vistas.PanelCompras(compraService, usuarioService, juegoService);

        // Agregar al jTabbedPane2 (tab "Usuario" que ya existe en tu código)
        jTabbedPane2.addTab("Clientes",  panelUsuarios);
        jTabbedPane2.addTab("Juegos",    panelJuegos);
        jTabbedPane2.addTab("Compras",   panelCompras);
        
        cargarTablaInicio();
        configurarBiblioteca();
    }
    private void cargarTablaInicio() {
        javax.swing.table.DefaultTableModel modelo =
            (javax.swing.table.DefaultTableModel) tablaJuegos.getModel();
        modelo.setRowCount(0);
        for (Clases.Juego j : juegoService.getLista())
            modelo.addRow(new Object[]{ j.getTitulo(), j.getGenero(), j.getPrecio() });
    }
    private void configurarBiblioteca() {
    // Panel de búsqueda que se agrega encima de la tabla
    javax.swing.JPanel panelBuscarCliente = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 4));
    panelBuscarCliente.setBackground(new java.awt.Color(0, 102, 102));

    javax.swing.JLabel lblPedirCliente = new javax.swing.JLabel("Código cliente:");
    lblPedirCliente.setForeground(java.awt.Color.WHITE);
    lblPedirCliente.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));

    javax.swing.JTextField txtCodigoCliente = new javax.swing.JTextField(12);
    txtCodigoCliente.setBackground(new java.awt.Color(30, 30, 30));
    txtCodigoCliente.setForeground(java.awt.Color.WHITE);
    txtCodigoCliente.setCaretColor(java.awt.Color.WHITE);

    javax.swing.JButton btnVerJuegos = new javax.swing.JButton("Ver mis juegos");
    btnVerJuegos.setBackground(new java.awt.Color(0, 153, 153));
    btnVerJuegos.setForeground(java.awt.Color.WHITE);
    btnVerJuegos.setFocusPainted(false);
    btnVerJuegos.setBorderPainted(false);

    javax.swing.JLabel lblResultado = new javax.swing.JLabel("");
    lblResultado.setForeground(new java.awt.Color(255, 180, 50));
    lblResultado.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));

    panelBuscarCliente.add(lblPedirCliente);
    panelBuscarCliente.add(txtCodigoCliente);
    panelBuscarCliente.add(btnVerJuegos);
    panelBuscarCliente.add(lblResultado);

    // Cambiar modelo de la tabla mostrarJuegos para mostrar más info
    javax.swing.table.DefaultTableModel modeloBiblioteca =
        new javax.swing.table.DefaultTableModel(
            new String[]{"Juego", "Género", "Precio ($)", "Fecha compra"}, 0) {
        public boolean isCellEditable(int r, int c) { return false; }
    };
    mostrarJuegos.setModel(modeloBiblioteca);
    mostrarJuegos.setForeground(java.awt.Color.WHITE);
    mostrarJuegos.setBackground(new java.awt.Color(0, 102, 102));
    mostrarJuegos.setSelectionBackground(new java.awt.Color(0, 153, 153));
    mostrarJuegos.setRowHeight(26);
    mostrarJuegos.getTableHeader().setBackground(new java.awt.Color(0, 153, 153));
    mostrarJuegos.getTableHeader().setForeground(java.awt.Color.WHITE);
    mostrarJuegos.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));

    // Acción del botón
    btnVerJuegos.addActionListener(e -> {
        String codigo = txtCodigoCliente.getText().trim();
        modeloBiblioteca.setRowCount(0);

        if (codigo.isEmpty()) {
            lblResultado.setText("⚠ Ingresa un código de cliente.");
            return;
        }

        Clases.Usuario usuario = usuarioService.buscarPorCodigo(codigo);
        if (usuario == null) {
            lblResultado.setText("✖ Cliente no encontrado.");
            return;
        }

        java.util.ArrayList<Clases.Compra> compras =
            compraService.buscarPorUsuario(codigo);

        if (compras.isEmpty()) {
            lblResultado.setText("📭 " + usuario.getNombre() + " no tiene compras.");
            return;
        }

        for (Clases.Compra c : compras) {
            modeloBiblioteca.addRow(new Object[]{
                c.getJuego().getTitulo(),
                c.getJuego().getGenero(),
                String.format("%.2f", c.getJuego().getPrecio()),
                c.getFecha()
            });
        }
        lblResultado.setText("✔ Biblioteca de: " + usuario.getNombre()
            + " (" + compras.size() + " juego(s))");
    });

    // Insertar el panel de búsqueda encima de la tabla en panelBiblioteca
    panelBiblioteca.setLayout(new java.awt.BorderLayout(0, 6));
    panelBiblioteca.add(panelBuscarCliente, java.awt.BorderLayout.NORTH);
    panelBiblioteca.add(jLabel3,            java.awt.BorderLayout.CENTER);
    panelBiblioteca.add(MostrarJuegos,      java.awt.BorderLayout.SOUTH);
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fondoVerde = new javax.swing.JPanel();
        Titulo = new javax.swing.JLabel();
        descripcion = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        panelInicio = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaJuegos = new javax.swing.JTable();
        buscador = new javax.swing.JLabel();
        ingresoJuego = new javax.swing.JTextField();
        ingresoGenero = new javax.swing.JComboBox<>();
        buscar = new javax.swing.JButton();
        comprar = new javax.swing.JButton();
        panelBiblioteca = new javax.swing.JPanel();
        MostrarJuegos = new javax.swing.JScrollPane();
        mostrarJuegos = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jTabbedPane2 = new javax.swing.JTabbedPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 0));

        fondoVerde.setBackground(new java.awt.Color(0, 102, 102));
        fondoVerde.setAlignmentX(40.0F);

        Titulo.setFont(new java.awt.Font("NSimSun", 1, 24)); // NOI18N
        Titulo.setForeground(new java.awt.Color(255, 255, 255));
        Titulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Titulo.setText("Gameverse digital");

        descripcion.setForeground(new java.awt.Color(255, 255, 255));
        descripcion.setText("Tienda Super Original de Videojuegos (Steam nos copió a nosotros )");

        jTabbedPane1.setBackground(new java.awt.Color(0, 102, 102));
        jTabbedPane1.setForeground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTabbedPane1.setOpaque(true);

        panelInicio.setBackground(new java.awt.Color(0, 0, 0));
        panelInicio.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 4, 2, 4, new java.awt.Color(0, 0, 0)));

        tablaJuegos.setBackground(new java.awt.Color(0, 102, 102));
        tablaJuegos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Juego", "Genero", "Precio"
            }
        ));
        tablaJuegos.setGridColor(new java.awt.Color(204, 204, 204));
        jScrollPane1.setViewportView(tablaJuegos);

        buscador.setForeground(new java.awt.Color(255, 255, 255));
        buscador.setText("Buscador");

        ingresoJuego.setText("ingrese el juego");
        ingresoJuego.setOpaque(true);

        ingresoGenero.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Terror", "Accion", "RPG", "Multijugador", "Aventura" }));

        buscar.setText("buscar");
        buscar.addActionListener(this::buscarActionPerformed);

        comprar.setText("Comprar");
        comprar.addActionListener(this::DDComprar);

        javax.swing.GroupLayout panelInicioLayout = new javax.swing.GroupLayout(panelInicio);
        panelInicio.setLayout(panelInicioLayout);
        panelInicioLayout.setHorizontalGroup(
            panelInicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInicioLayout.createSequentialGroup()
                .addGroup(panelInicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelInicioLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(panelInicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(buscador, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelInicioLayout.createSequentialGroup()
                                .addComponent(ingresoJuego, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ingresoGenero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buscar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(comprar))))
                    .addGroup(panelInicioLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 525, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        panelInicioLayout.setVerticalGroup(
            panelInicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInicioLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(buscador)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelInicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ingresoGenero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ingresoJuego, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comprar)
                    .addComponent(buscar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Inicio", panelInicio);

        panelBiblioteca.setBackground(new java.awt.Color(0, 0, 0));
        panelBiblioteca.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 4, 2, 4, new java.awt.Color(0, 0, 0)));

        mostrarJuegos.setBackground(new java.awt.Color(0, 102, 102));
        mostrarJuegos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Juego", "Genero"
            }
        ));
        mostrarJuegos.setGridColor(new java.awt.Color(204, 204, 204));
        MostrarJuegos.setViewportView(mostrarJuegos);

        jLabel3.setBackground(new java.awt.Color(0, 153, 153));
        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Tus juegos");
        jLabel3.setOpaque(true);

        javax.swing.GroupLayout panelBibliotecaLayout = new javax.swing.GroupLayout(panelBiblioteca);
        panelBiblioteca.setLayout(panelBibliotecaLayout);
        panelBibliotecaLayout.setHorizontalGroup(
            panelBibliotecaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBibliotecaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBibliotecaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(MostrarJuegos, javax.swing.GroupLayout.DEFAULT_SIZE, 533, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelBibliotecaLayout.setVerticalGroup(
            panelBibliotecaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBibliotecaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(MostrarJuegos, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Biblioteca", panelBiblioteca);
        jTabbedPane1.addTab("Usuario", jTabbedPane2);

        javax.swing.GroupLayout fondoVerdeLayout = new javax.swing.GroupLayout(fondoVerde);
        fondoVerde.setLayout(fondoVerdeLayout);
        fondoVerdeLayout.setHorizontalGroup(
            fondoVerdeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fondoVerdeLayout.createSequentialGroup()
                .addGroup(fondoVerdeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(fondoVerdeLayout.createSequentialGroup()
                        .addGap(160, 160, 160)
                        .addComponent(Titulo, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(fondoVerdeLayout.createSequentialGroup()
                        .addGap(88, 88, 88)
                        .addComponent(descripcion)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        fondoVerdeLayout.setVerticalGroup(
            fondoVerdeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fondoVerdeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Titulo, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(descripcion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(fondoVerde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(fondoVerde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void DDComprar(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DDComprar
        
    }//GEN-LAST:event_DDComprar

    private void buscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buscarActionPerformed
        String textoBuscar = ingresoJuego.getText().trim();
        String generoBuscar = (String) ingresoGenero.getSelectedItem();

        javax.swing.table.DefaultTableModel modelo =
        (javax.swing.table.DefaultTableModel) tablaJuegos.getModel();
        modelo.setRowCount(0);

        for (Clases.Juego j : juegoService.getLista()) {
            boolean coincideTitulo = textoBuscar.isEmpty() ||
            textoBuscar.equalsIgnoreCase("ingrese el juego") ||
            j.getTitulo().toLowerCase().contains(textoBuscar.toLowerCase());
            boolean coincideGenero = j.getGenero().equalsIgnoreCase(generoBuscar);

            if (coincideTitulo && coincideGenero)
            modelo.addRow(new Object[]{ j.getTitulo(), j.getGenero(), j.getPrecio() });
        }
    }//GEN-LAST:event_buscarActionPerformed

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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new paginaPrincipal().setVisible(true));
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane MostrarJuegos;
    private javax.swing.JLabel Titulo;
    private javax.swing.JLabel buscador;
    private javax.swing.JButton buscar;
    private javax.swing.JButton comprar;
    private javax.swing.JLabel descripcion;
    private javax.swing.JPanel fondoVerde;
    private javax.swing.JComboBox<String> ingresoGenero;
    private javax.swing.JTextField ingresoJuego;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTable mostrarJuegos;
    private javax.swing.JPanel panelBiblioteca;
    private javax.swing.JPanel panelInicio;
    private javax.swing.JTable tablaJuegos;
    // End of variables declaration//GEN-END:variables

}
