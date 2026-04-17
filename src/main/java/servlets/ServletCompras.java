package servlets;

import Modelo.GestionarCompras;
import Modelo.Producto;
import Modelo.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * ServletCompras
 * Maneja toda la lógica del módulo de compras:
 *  - Mostrar catálogo y carrito (GET)
 *  - Agregar, reducir, eliminar productos del carrito y procesar compra (POST)
 *
 * Toda la lógica de negocio se delega a la clase Modelo.GestionarCompras,
 * manteniendo este servlet delgado y el JSP libre de scriptlets Java.
 */
@WebServlet("/compras")
public class ServletCompras extends HttpServlet {

    private final GestionarCompras gestor = new GestionarCompras();

    // ------------------------------------------------------------------
    // GET – mostrar página de compras
    // ------------------------------------------------------------------
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(true);

        // Inicializar carrito si no existe
        @SuppressWarnings("unchecked")
        List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = gestor.nuevoCarrito();
            session.setAttribute("carrito", carrito);
        }

        // Preparar datos para la vista
        request.setAttribute("catalogo",    gestor.getCatalogo());
        request.setAttribute("carrito",     carrito);
        request.setAttribute("totalCarrito", gestor.calcularTotal(carrito));
        request.setAttribute("itemsCarrito", gestor.contarItems(carrito));

        request.getRequestDispatcher("/compras.jsp").forward(request, response);
    }

    // ------------------------------------------------------------------
    // POST – operaciones sobre el carrito
    // ------------------------------------------------------------------
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // Verificar que el usuario esté autenticado para procesar compra
        HttpSession session = request.getSession(false);
        String accion = request.getParameter("accion");

        if (session == null) {
            response.sendRedirect(request.getContextPath()
                    + "/login.jsp?msg=Debes+iniciar+sesion+para+continuar&tipoMsg=error");
            return;
        }

        @SuppressWarnings("unchecked")
        List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = gestor.nuevoCarrito();
            session.setAttribute("carrito", carrito);
        }

        String codigo = request.getParameter("codigo");
        String mensaje = null;
        String tipoMsg = "ok";
        String redirect = request.getContextPath() + "/compras";

        switch (accion != null ? accion : "") {

            case "agregar" : {
                if (codigo != null && !codigo.trim().isEmpty()) {
                    mensaje = gestor.agregarAlCarrito(carrito, codigo.trim());
                }
                session.setAttribute("carrito", carrito);
                break;
            }

            case "reducir" : {
                if (codigo != null && !codigo.trim().isEmpty()) {
                    mensaje = gestor.reducirCantidad(carrito, codigo.trim());
                }
                session.setAttribute("carrito", carrito);
                break;
            }

            case "eliminar" : {
                if (codigo != null && !codigo.trim().isEmpty()) {
                    gestor.eliminarDelCarrito(carrito, codigo.trim());
                    mensaje = "Producto eliminado del carrito.";
                }
                session.setAttribute("carrito", carrito);
                break;
            }

            case "vaciar" : {
                gestor.vaciarCarrito(carrito);
                session.setAttribute("carrito", carrito);
                mensaje = "Carrito vaciado.";
                break;
            }

            case "comprar" : {
                // Requiere sesión activa con usuario logueado
                Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
                if (usuarioLogueado == null) {
                    response.sendRedirect(request.getContextPath()
                            + "/login.jsp?msg=Debes+iniciar+sesion+para+comprar&tipoMsg=error");
                    return;
                }
                String resultado = gestor.procesarCompra(carrito);
                session.setAttribute("carrito", carrito);
                if (resultado.startsWith("ok:")) {
                    mensaje = resultado.substring(4);
                } else {
                    tipoMsg = "error";
                    mensaje = resultado.substring(7);
                }
                break;
            }
            
            
        }
        if (mensaje != null) {
                redirect += "?msg=" + java.net.URLEncoder.encode(mensaje, "UTF-8")
                        + "&tipoMsg=" + tipoMsg;
            }
        
        response.sendRedirect(redirect);
    }
}
