package servlets;

import Modelo.GestionarProductos;
import Modelo.Producto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

@WebServlet("/productos")
public class ServletProductos extends HttpServlet {

    private final GestionarProductos gestor = new GestionarProductos();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        String accion = request.getParameter("accion");
        PrintWriter out = response.getWriter();

        escribirCabecera(out, "Gestion de Productos", request);

        if ("editar".equals(accion)) {
            String codigo = request.getParameter("codigo");
            Producto p = gestor.buscarProducto(codigo);
            if (p != null) {
                mostrarFormularioEditar(out, p);
            } else {
                out.println("<p class='msg error'>Producto no encontrado.</p>");
                mostrarTablaYFormulario(out);
            }
        } else if ("buscar".equals(accion)) {
            String codigo = request.getParameter("codigo");
            Producto p = gestor.buscarProducto(codigo);
            mostrarResultadoBusqueda(out, p, codigo);
            mostrarTablaYFormulario(out);
        } else {
            mostrarTablaYFormulario(out);
        }

        escribirPie(out);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String accion = request.getParameter("accion");

        switch (accion) {
            case "editar" : {
                gestor.editarProducto(extraerProducto(request));
                redirigirConMensaje(response, "ok", "Producto+actualizado+correctamente.");
                return;
            }
            case "eliminar" : {
                gestor.eliminarProducto(request.getParameter("codigo"));
                redirigirConMensaje(response, "ok", "Producto+eliminado+correctamente.");
                return;
            }
            default: response.sendRedirect("productos");
        }
    }

    private Producto extraerProducto(HttpServletRequest req) {
        return new Producto(
            req.getParameter("codigo"),
            req.getParameter("nombre"),
            Integer.parseInt(req.getParameter("cantidad")),
            Double.parseDouble(req.getParameter("precio")),
            req.getParameter("imagen")
        );
    }

    private void redirigirConMensaje(HttpServletResponse response, String tipo, String msg)
            throws IOException {
        response.sendRedirect("productos?msg=" + msg + "&tipoMsg=" + tipo);
    }

    private void mostrarTablaYFormulario(PrintWriter out) {
        mostrarTablaProductos(out);
    }

    private void mostrarTablaProductos(PrintWriter out) {
        LinkedList<Producto> lista = gestor.listarProductos();
        out.println("<section>");
        out.println("<h2>Productos registrados</h2>");

        // Formulario búsqueda
        out.println("<form method='get' action='productos' class='form-buscar'>");
        out.println("<input type='hidden' name='accion' value='buscar'>");
        out.println("<input type='text' name='codigo' placeholder='Buscar por codigo...' required>");
        out.println("<button type='submit'>Buscar</button>");
        out.println("</form>");

        if (lista.isEmpty()) {
            out.println("<p class='msg'>No hay productos registrados.</p>");
        } else {
            out.println("<table><thead><tr>"
                + "<th>Codigo</th><th>Nombre</th><th>Cantidad</th><th>Precio</th><th>Imagen</th><th>Acciones</th>"
                + "</tr></thead><tbody>");
            for (Producto p : lista) {
                out.println("<tr>"
                    + "<td>" + p.getCodigo() + "</td>"
                    + "<td>" + p.getNombre() + "</td>"
                    + "<td>" + p.getCantidad() + "</td>"
                    + "<td>$" + String.format("%.2f", p.getPrecio()) + "</td>"
                    + "<td>" + (p.getImagen().isEmpty() ? "-" : "<img src='" + p.getImagen() + "' width='50'>") + "</td>"
                    + "<td class='acciones'>"
                    + "<a href='productos?accion=editar&codigo=" + p.getCodigo() + "' class='btn-editar'>Editar</a>"
                    + "<form method='post' action='productos' style='display:inline' "
                    + "onsubmit=\"return confirm('Eliminar " + p.getNombre() + "?')\">"
                    + "<input type='hidden' name='accion' value='eliminar'>"
                    + "<input type='hidden' name='codigo' value='" + p.getCodigo() + "'>"
                    + "<button type='submit' class='btn-eliminar'>Eliminar</button>"
                    + "</form></td></tr>");
            }
            out.println("</tbody></table>");
        }
        out.println("</section>");
    }

    private void mostrarFormularioEditar(PrintWriter out, Producto p) {
        out.println("<section><h2>Editando: " + p.getNombre() + "</h2>");
        out.println("<form method='post' action='productos' class='form-crud'>");
        out.println("<input type='hidden' name='accion' value='editar'>");
        camposProducto(out, p);
        out.println("<button type='submit'>Actualizar</button>");
        out.println("<a href='productos' class='btn-cancelar'>Cancelar</a>");
        out.println("</form></section>");
    }

    private void camposProducto(PrintWriter out, Producto p) {
        boolean e = p != null;
        String codigo   = e ? p.getCodigo()   : "";
        String nombre   = e ? p.getNombre()   : "";
        String cantidad = e ? String.valueOf(p.getCantidad()) : "";
        String precio   = e ? String.valueOf(p.getPrecio())   : "";
        String imagen   = e ? p.getImagen()   : "";

        out.println("<div class='grid-form'>");
        out.println("<label>Codigo<input type='text' name='codigo' value='" + codigo + "' " + (e ? "readonly" : "required") + "></label>");
        out.println("<label>Nombre<input type='text' name='nombre' value='" + nombre + "' required></label>");
        out.println("<label>Cantidad<input type='number' name='cantidad' value='" + cantidad + "' min='0' required></label>");
        out.println("<label>Precio<input type='number' name='precio' value='" + precio + "' min='0' step='0.01' required></label>");
        out.println("<label>Imagen URL<input type='text' name='imagen' value='" + imagen + "'></label>");
        out.println("</div>");
    }

    private void mostrarResultadoBusqueda(PrintWriter out, Producto p, String codigo) {
        if (p == null) {
            out.println("<p class='msg error'>No se encontro producto con codigo: " + codigo + "</p>");
        } else {
            out.println("<div class='card-resultado'><h3>Resultado de busqueda</h3>"
                + "<p><strong>Codigo:</strong> " + p.getCodigo() + "</p>"
                + "<p><strong>Nombre:</strong> " + p.getNombre() + "</p>"
                + "<p><strong>Cantidad:</strong> " + p.getCantidad() + "</p>"
                + "<p><strong>Precio:</strong> $" + String.format("%.2f", p.getPrecio()) + "</p>"
                + "</div>");
        }
    }

    private void escribirCabecera(PrintWriter out, String titulo, HttpServletRequest request) {
        out.println("<!DOCTYPE html><html lang='es'><head>"
            + "<meta charset='UTF-8'>"
            + "<meta name='viewport' content='width=device-width, initial-scale=1.0'>"
            + "<title>" + titulo + "</title>"
            + "<style>" + estilos() + "</style>"
            + "</head><body>"
            + "<header><h1>" + titulo + "</h1>"
            + "<nav><a href='usuarios'>Usuarios</a> | <a href='productos'>Productos</a></nav></header>"
            + "<main>");

        String msg = request.getParameter("msg");
        String tipoMsg = request.getParameter("tipoMsg");
        if (msg != null) {
            out.println("<p class='msg " + tipoMsg + "'>" + msg.replace("+", " ") + "</p>");
        }
    }

    private void escribirPie(PrintWriter out) {
        out.println("</main><footer><p>JavaWeb Project1 - Umariana</p></footer></body></html>");
    }

    private String estilos() {
        return "* { box-sizing: border-box; margin: 0; padding: 0; }"
            + "body { font-family: 'Segoe UI', sans-serif; background: #f0f2f5; color: #333; }"
            + "header { background: #1e3a5f; color: white; padding: 18px 30px; }"
            + "header h1 { font-size: 1.5rem; }"
            + "nav { margin-top: 8px; }"
            + "nav a { color: #90caf9; text-decoration: none; margin-right: 12px; }"
            + "nav a:hover { text-decoration: underline; }"
            + "main { max-width: 1100px; margin: 24px auto; padding: 0 16px; }"
            + "section { background: white; border-radius: 10px; padding: 24px; margin-bottom: 24px; box-shadow: 0 2px 8px rgba(0,0,0,0.08); }"
            + "h2 { margin-bottom: 16px; color: #1e3a5f; font-size: 1.2rem; }"
            + ".grid-form { display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 14px; margin-bottom: 16px; }"
            + "label { display: flex; flex-direction: column; font-size: 0.85rem; font-weight: 600; color: #555; gap: 4px; }"
            + "input, select { padding: 8px 10px; border: 1px solid #ccc; border-radius: 6px; font-size: 0.95rem; width: 100%; }"
            + "input:focus, select:focus { outline: none; border-color: #1e3a5f; }"
            + "input[readonly] { background: #f5f5f5; cursor: not-allowed; }"
            + "button[type=submit] { background: #1e3a5f; color: white; border: none; padding: 10px 22px; border-radius: 6px; cursor: pointer; font-size: 0.95rem; }"
            + "button[type=submit]:hover { background: #2a5298; }"
            + ".btn-cancelar { background: #6c757d; color: white; padding: 10px 18px; border-radius: 6px; text-decoration: none; font-size: 0.95rem; margin-left: 8px; }"
            + "table { width: 100%; border-collapse: collapse; font-size: 0.9rem; }"
            + "thead tr { background: #1e3a5f; color: white; }"
            + "th, td { padding: 10px 12px; border-bottom: 1px solid #e0e0e0; text-align: left; }"
            + "tbody tr:hover { background: #f7f9fc; }"
            + ".acciones { display: flex; gap: 6px; align-items: center; }"
            + ".btn-editar { background: #f0a500; color: white; padding: 5px 10px; border-radius: 5px; text-decoration: none; font-size: 0.82rem; }"
            + ".btn-eliminar { background: #c0392b; color: white; border: none; padding: 5px 10px; border-radius: 5px; cursor: pointer; font-size: 0.82rem; }"
            + ".form-buscar { display: flex; gap: 10px; margin-bottom: 16px; }"
            + ".form-buscar input { max-width: 280px; }"
            + ".form-buscar button { background: #2a5298; color: white; border: none; padding: 8px 16px; border-radius: 6px; cursor: pointer; }"
            + ".msg { padding: 12px 16px; border-radius: 6px; margin-bottom: 16px; font-weight: 500; }"
            + ".msg.ok { background: #d4edda; color: #155724; border: 1px solid #c3e6cb; }"
            + ".msg.error { background: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }"
            + ".card-resultado { background: #e8f0fe; border-radius: 8px; padding: 16px; margin-bottom: 16px; border-left: 4px solid #1e3a5f; }"
            + ".card-resultado p { margin: 4px 0; }"
            + "footer { text-align: center; padding: 20px; color: #888; font-size: 0.85rem; }";
    }
}
