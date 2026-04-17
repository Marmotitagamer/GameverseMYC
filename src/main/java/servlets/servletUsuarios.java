package servlets;

import Modelo.GestionarUsuarios;
import Modelo.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

@WebServlet("/usuarios")
public class servletUsuarios extends HttpServlet {

    private final GestionarUsuarios gestor = new GestionarUsuarios();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        String accion = request.getParameter("accion");
        PrintWriter out = response.getWriter();

        escribirCabecera(out, "Gestión de Usuarios", request);

        if ("editar".equals(accion)) {
            String cedula = request.getParameter("cedula");
            Usuario u = gestor.buscarUsuario(cedula);
            if (u != null) {
                mostrarFormularioEditar(out, u);
            } else {
                out.println("<p class='msg error'>Usuario no encontrado.</p>");
                mostrarTablaYFormulario(out);
            }
        } else if ("buscar".equals(accion)) {
            String cedula = request.getParameter("cedula");
            Usuario u = gestor.buscarUsuario(cedula);
            mostrarResultadoBusqueda(out, u, cedula);
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
        
        String origen = request.getParameter("origen");
        boolean esAdmin = "admin".equals(origen);

        switch (accion) {
            case "crear" : {
                Usuario nuevo = extraerUsuario(request);
                if (gestor.buscarUsuario(nuevo.getCedula()) != null) {
                    redirigirConMensaje(response, "error", "Ya+existe+un+usuario+con+esa+cedula.");
                } else {
                    gestor.agregarUsuario(nuevo);
                    redirigirConMensaje(response, "ok", "Usuario+creado+correctamente.");
                }
                return;
            }
            case "editar" : {
                gestor.editarUsuario(extraerUsuario(request));
                redirigirConMensaje(response, "ok", "Usuario+actualizado+correctamente.");
                return;
            }
            case "eliminar" : {
                gestor.eliminarUsuario(request.getParameter("cedula"));
                redirigirConMensaje(response, "ok", "Usuario+eliminado+correctamente.");
                return;
            }
            default : response.sendRedirect("usuarios");
        }
    }

    private Usuario extraerUsuario(HttpServletRequest req) {
        String cedula   = req.getParameter("cedula");
        String nombre   = req.getParameter("nombre");
        String edadStr  = req.getParameter("edad");
        String correo   = req.getParameter("correo");
        String password = req.getParameter("password");
        String imagen   = req.getParameter("imagenPerfil");
        String tipo     = req.getParameter("tipoUsuario");
        String direccion= req.getParameter("direccion");

        
        if (correo == null || password == null) {
            Usuario actual = gestor.buscarUsuario(cedula);
            if (actual != null) {
                if (nombre    == null) nombre    = actual.getNombre();
                if (edadStr   == null) edadStr   = String.valueOf(actual.getEdad());
                if (correo    == null) correo    = actual.getCorreo();
                if (password  == null) password  = actual.getPassword();
                if (imagen    == null) imagen    = actual.getImagenPerfil();
                if (tipo      == null) tipo      = actual.getTipoUsuario();
                if (direccion == null) direccion = actual.getDireccion();
            }
        }

        int edad = 0;
        try { edad = Integer.parseInt(edadStr != null ? edadStr : "0"); } catch (Exception ignored) {}

        return new Usuario(
            nombre    != null ? nombre    : "",
            edad,
            cedula    != null ? cedula    : "",
            correo    != null ? correo    : "",
            password  != null ? password  : "",
            imagen    != null ? imagen    : "",
            tipo      != null ? tipo      : "cliente",
            direccion != null ? direccion : ""
        );
    }

    private void redirigirConMensaje(HttpServletResponse response, String tipo, String msg)
            throws IOException {
        response.sendRedirect("usuarios?msg=" + msg + "&tipoMsg=" + tipo);
    }

    private void mostrarTablaYFormulario(PrintWriter out) {
        mostrarFormularioCrear(out);
        mostrarTablaUsuarios(out);
    }

    private void mostrarTablaUsuarios(PrintWriter out) {
        LinkedList<Usuario> lista = gestor.ListarUsuarios();
        out.println("<section>");
        out.println("<h2>Usuarios registrados</h2>");
        out.println("<form method='get' action='usuarios' class='form-buscar'>");
        out.println("<input type='hidden' name='accion' value='buscar'>");
        out.println("<input type='text' name='cedula' placeholder='Buscar por cedula...' required>");
        out.println("<button type='submit'>Buscar</button>");
        out.println("</form>");

        if (lista.isEmpty()) {
            out.println("<p class='msg'>No hay usuarios registrados.</p>");
        } else {
            out.println("<table><thead><tr><th>Nombre</th><th>Edad</th><th>Cedula</th>"
                    + "<th>Correo</th><th>Tipo</th><th>Direccion</th><th>Acciones</th></tr></thead><tbody>");
            for (Usuario u : lista) {
                out.println("<tr>"
                    + "<td>" + u.getNombre() + "</td>"
                    + "<td>" + u.getEdad() + "</td>"
                    + "<td>" + u.getCedula() + "</td>"
                    + "<td>" + u.getCorreo() + "</td>"
                    + "<td>" + u.getTipoUsuario() + "</td>"
                    + "<td>" + u.getDireccion() + "</td>"
                    + "<td class='acciones'>"
                    + "<a href='usuarios?accion=editar&cedula=" + u.getCedula() + "' class='btn-editar'>Editar</a>"
                    + "<form method='post' action='usuarios' style='display:inline' "
                    + "onsubmit=\"return confirm('Eliminar a " + u.getNombre() + "?')\">"
                    + "<input type='hidden' name='accion' value='eliminar'>"
                    + "<input type='hidden' name='cedula' value='" + u.getCedula() + "'>"
                    + "<button type='submit' class='btn-eliminar'>Eliminar</button>"
                    + "</form></td></tr>");
            }
            out.println("</tbody></table>");
        }
        out.println("</section>");
    }

    private void mostrarFormularioCrear(PrintWriter out) {
        out.println("<section><h2>Crear nuevo Usuario</h2>");
        out.println("<form method='post' action='usuarios' class='form-crud'>");
        out.println("<input type='hidden' name='accion' value='crear'>");
        camposUsuario(out, null);
        out.println("<button type='submit'>Guardar Usuario</button>");
        out.println("</form></section>");
    }

    private void mostrarFormularioEditar(PrintWriter out, Usuario u) {
        out.println("<section><h2>Editando: " + u.getNombre() + "</h2>");
        out.println("<form method='post' action='usuarios' class='form-crud'>");
        out.println("<input type='hidden' name='accion' value='editar'>");
        camposUsuario(out, u);
        out.println("<button type='submit'>Actualizar</button>");
        out.println("<a href='usuarios' class='btn-cancelar'>Cancelar</a>");
        out.println("</form></section>");
    }

    private void camposUsuario(PrintWriter out, Usuario u) {
        boolean e = u != null;
        String nombre  = e ? u.getNombre()       : "";
        String edad    = e ? String.valueOf(u.getEdad()) : "";
        String cedula  = e ? u.getCedula()        : "";
        String correo  = e ? u.getCorreo()        : "";
        String pass    = e ? u.getPassword()      : "";
        String imagen  = e ? u.getImagenPerfil()  : "";
        String tipo    = e ? u.getTipoUsuario()   : "";
        String dir     = e ? u.getDireccion()     : "";

        out.println("<div class='grid-form'>");
        out.println("<label>Nombre<input type='text' name='nombre' value='" + nombre + "' required></label>");
        out.println("<label>Edad<input type='number' name='edad' value='" + edad + "' min='1' required></label>");
        out.println("<label>Cedula<input type='text' name='cedula' value='" + cedula + "' " + (e ? "readonly" : "required") + "></label>");
        out.println("<label>Correo<input type='email' name='correo' value='" + correo + "' required></label>");
        out.println("<label>Password<input type='password' name='password' value='" + pass + "' required></label>");
        out.println("<label>Imagen URL<input type='text' name='imagenPerfil' value='" + imagen + "'></label>");
        out.println("<label>Tipo<select name='tipoUsuario'>"
            + "<option value='admin'" + ("admin".equals(tipo) ? " selected" : "") + ">Admin</option>"
            + "<option value='cliente'" + ("cliente".equals(tipo) ? " selected" : "") + ">Cliente</option>"
            + "<option value='vendedor'" + ("vendedor".equals(tipo) ? " selected" : "") + ">Vendedor</option>"
            + "</select></label>");
        out.println("<label>Direccion<input type='text' name='direccion' value='" + dir + "' required></label>");
        out.println("</div>");
    }

    private void mostrarResultadoBusqueda(PrintWriter out, Usuario u, String cedula) {
        if (u == null) {
            out.println("<p class='msg error'>No se encontro usuario con cedula: " + cedula + "</p>");
        } else {
            out.println("<div class='card-resultado'><h3>Resultado de busqueda</h3>"
                + "<p><strong>Nombre:</strong> " + u.getNombre() + "</p>"
                + "<p><strong>Edad:</strong> " + u.getEdad() + "</p>"
                + "<p><strong>Cedula:</strong> " + u.getCedula() + "</p>"
                + "<p><strong>Correo:</strong> " + u.getCorreo() + "</p>"
                + "<p><strong>Tipo:</strong> " + u.getTipoUsuario() + "</p>"
                + "<p><strong>Direccion:</strong> " + u.getDireccion() + "</p>"
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
            + "body { font-family: 'Segoe UI', sans-serif; background: #f0f2f5; color: #33; }"
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
