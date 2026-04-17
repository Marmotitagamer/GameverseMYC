<%-- 
    compras.jsp  
    Vista del módulo de compras.
    Toda la lógica está en ServletCompras + GestionarCompras.
    Este JSP solo muestra los datos que el servlet coloca como atributos.
--%>
<%@include file="lib/header.jsp"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List, Modelo.Producto"%>

<%
    // Recuperar datos preparados por ServletCompras
    List<Producto> catalogo = (List<Producto>) request.getAttribute("catalogo");
    List<Producto> carrito  = (List<Producto>) request.getAttribute("carrito");
    Double totalObj         = (Double)  request.getAttribute("totalCarrito");
    Integer itemsObj        = (Integer) request.getAttribute("itemsCarrito");

    double total = (totalObj  != null) ? totalObj  : 0.0;
    int    items = (itemsObj  != null) ? itemsObj  : 0;

    // Si el usuario llegó directamente al JSP (sin pasar por el servlet), redirigir
    if (catalogo == null) {
        response.sendRedirect("compras");
        return;
    }

    String msg     = request.getParameter("msg");
    String tipoMsg = request.getParameter("tipoMsg");
    boolean logueado = (session.getAttribute("usuarioLogueado") != null);
%>

<style>
    .compras-wrapper { padding: 2rem 3rem 6rem 3rem; }
    .section-title {
        color: #2dd4bf; font-size: 1.6rem; font-weight: 700;
        border-bottom: 2px solid #1a4a42; padding-bottom: .5rem;
        margin-bottom: 1.5rem; text-shadow: 0 0 10px rgba(45,212,191,.3);
    }
    .catalogo-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(195px, 1fr));
        gap: 1.2rem; margin-bottom: 3rem;
    }
    .juego-card {
        background: #0d2b27; border: 1px solid #1a4a42; border-radius: 12px;
        padding: 1.2rem; text-align: center;
        transition: transform .25s ease, box-shadow .25s ease;
    }
    .juego-card:hover {
        transform: translateY(-4px);
        box-shadow: 0 12px 32px rgba(0,0,0,.6), 0 0 0 1px rgba(45,212,191,.2);
    }
    .juego-card img { width: 100%; height: 130px; object-fit: cover; border-radius: 8px; margin-bottom: .7rem; }
    .juego-nombre { font-size: .9rem; font-weight: 700; color: #2dd4bf; margin-bottom: .3rem; }
    .juego-precio { font-size: 1rem; color: #fbbf24; font-weight: 600; margin-bottom: .8rem; }
    .juego-stock  { font-size: .75rem; color: #94a3b8; margin-bottom: .6rem; }
    .btn-agregar {
        background: #1a6060; color: #fff; border: none; border-radius: 6px;
        padding: .4rem 1rem; font-size: .82rem; font-weight: 600; cursor: pointer;
        text-decoration: none; display: inline-block;
        transition: background .2s, transform .2s;
    }
    .btn-agregar:hover { background: #228080; transform: scale(1.05); color: #fff; }
    .btn-agregar:disabled, .btn-agregar.disabled {
        background: #2a4a40; color: #5a7a70; cursor: not-allowed;
    }
    .carrito-section {
        background: #0a1f1c; border: 1px solid #1a4a42; border-radius: 14px;
        padding: 1.5rem 2rem;
    }
    .carrito-tabla { width: 100%; border-collapse: collapse; margin-bottom: 1rem; }
    .carrito-tabla th {
        color: #2dd4bf; font-size: .85rem; text-transform: uppercase;
        letter-spacing: .05em; border-bottom: 1px solid #1a4a42;
        padding: .6rem .8rem; text-align: left;
    }
    .carrito-tabla td {
        padding: .7rem .8rem; border-bottom: 1px solid rgba(26,74,66,.4);
        font-size: .9rem; color: #a8d5cf; vertical-align: middle;
    }
    .carrito-tabla tr:hover td { background: rgba(45,212,191,.05); }
    .total-row { font-size: 1.15rem; font-weight: 700; color: #fbbf24; text-align: right; }
    .qty-controls { display: flex; align-items: center; gap: .4rem; }
    .qty-btn {
        background: #1a4a42; color: #2dd4bf; border: 1px solid #2dd4bf;
        border-radius: 5px; width: 26px; height: 26px; font-size: .85rem;
        cursor: pointer; line-height: 1; padding: 0;
    }
    .qty-btn:hover { background: #2dd4bf; color: #000; }
    .btn-eliminar {
        background: transparent; color: #f87171; border: 1px solid #f87171;
        border-radius: 5px; padding: .25rem .6rem; font-size: .78rem; cursor: pointer;
        text-decoration: none; display: inline-block;
        transition: background .2s, color .2s;
    }
    .btn-eliminar:hover { background: #f87171; color: #1a0000; }
    .btn-vaciar {
        background: transparent; color: #94a3b8; border: 1px solid #94a3b8;
        border-radius: 6px; padding: .4rem 1rem; font-size: .82rem; cursor: pointer;
        transition: background .2s; text-decoration: none; display: inline-block;
    }
    .btn-vaciar:hover { background: #94a3b8; color: #000; }
    .btn-comprar {
        background: linear-gradient(135deg,#16a34a,#15803d); color: #fff;
        border: none; border-radius: 8px; padding: .7rem 2rem;
        font-size: 1rem; font-weight: 700; cursor: pointer;
        transition: opacity .2s, transform .2s;
    }
    .btn-comprar:hover { opacity: .9; transform: scale(1.02); }
    .carrito-vacio { color: #5a7a70; text-align: center; padding: 2rem; }
    .alert-compras {
        border-radius: 8px; padding: .8rem 1.2rem; margin-bottom: 1.5rem;
        font-weight: 500;
    }
    .alert-ok    { background: rgba(22,163,74,.15); color: #86efac; border: 1px solid #16a34a; }
    .alert-error { background: rgba(239,68,68,.15); color: #fca5a5; border: 1px solid #ef4444; }
    .badge-en-carrito {
        background: rgba(45,212,191,.15); color: #2dd4bf;
        border: 1px solid #2dd4bf; border-radius: 20px;
        padding: .15rem .6rem; font-size: .72rem; font-weight: 600;
    }
</style>

<div class="compras-wrapper">

    
    <% if (msg != null && !msg.isEmpty()) { %>
        <div class="alert-compras alert-<%= tipoMsg %>">
            <%= msg %>
        </div>
    <% } %>

   
    <h2 class="section-title"> Catálogo de Juegos</h2>
    <div class="catalogo-grid">
        <% for (Producto p : catalogo) {
               boolean yaEnCarrito = false;
               if (carrito != null) {
                   for (Producto c : carrito) {
                       if (c.getCodigo().equals(p.getCodigo())) { yaEnCarrito = true; break; }
                   }
               }
               boolean sinStock = p.getCantidad() <= 0;
        %>
        <div class="juego-card">
            <img src="<%= p.getImagen() %>" alt="<%= p.getNombre() %>">
            <div class="juego-nombre"><%= p.getNombre() %></div>
            <div class="juego-precio">$<%= String.format("%,.0f", p.getPrecio()) %></div>
            <div class="juego-stock">Stock: <%= p.getCantidad() %></div>

            <% if (sinStock) { %>
                <span class="btn-agregar disabled">Sin stock</span>
            <% } else if (yaEnCarrito) { %>
                <span class="badge-en-carrito">✓ En carrito</span>
            <% } else { %>
                <form method="POST" action="compras" style="display:inline;">
                    <input type="hidden" name="accion"  value="agregar">
                    <input type="hidden" name="codigo"  value="<%= p.getCodigo() %>">
                    <button type="submit" class="btn-agregar">+ Agregar</button>
                </form>
            <% } %>
        </div>
        <% } %>
    </div>


    <h2 class="section-title"> Mi Carrito
        <% if (items > 0) { %>
            <span style="font-size:.9rem; color:#fbbf24;"> (<%= items %> ítem<%= items != 1 ? "s" : "" %>)</span>
        <% } %>
    </h2>

    <div class="carrito-section">
        <% if (carrito == null || carrito.isEmpty()) { %>
            <p class="carrito-vacio">Tu carrito está vacío. ¡Agrega un juego del catálogo!</p>
        <% } else { %>
            <table class="carrito-tabla">
                <thead>
                    <tr>
                        <th>Juego</th>
                        <th>Precio unit.</th>
                        <th>Cantidad</th>
                        <th>Subtotal</th>
                        <th>Acción</th>
                    </tr>
                </thead>
                <tbody>
                <% for (Producto c : carrito) { %>
                    <tr>
                        <td><%= c.getNombre() %></td>
                        <td>$<%= String.format("%,.0f", c.getPrecio()) %></td>
                        <td>
                            <div class="qty-controls">
                                <!-- Reducir cantidad -->
                                <form method="POST" action="compras" style="margin:0;">
                                    <input type="hidden" name="accion" value="reducir">
                                    <input type="hidden" name="codigo" value="<%= c.getCodigo() %>">
                                    <button type="submit" class="qty-btn">−</button>
                                </form>
                                <span><%= c.getCantidad() %></span>
                                <!-- Aumentar cantidad -->
                                <form method="POST" action="compras" style="margin:0;">
                                    <input type="hidden" name="accion" value="agregar">
                                    <input type="hidden" name="codigo" value="<%= c.getCodigo() %>">
                                    <button type="submit" class="qty-btn">+</button>
                                </form>
                            </div>
                        </td>
                        <td>$<%= String.format("%,.0f", c.getPrecio() * c.getCantidad()) %></td>
                        <td>
                            <form method="POST" action="compras" style="display:inline;">
                                <input type="hidden" name="accion" value="eliminar">
                                <input type="hidden" name="codigo" value="<%= c.getCodigo() %>">
                                <button type="submit" class="btn-eliminar"
                                        onclick="return confirm('¿Quitar <%= c.getNombre() %> del carrito?')">
                                    Quitar
                                </button>
                            </form>
                        </td>
                    </tr>
                <% } %>
                </tbody>
            </table>

            <div class="total-row">
                Total: $<%= String.format("%,.0f", total) %>
            </div>

            <div style="display:flex; gap:1rem; justify-content:flex-end; margin-top:1.2rem;">
                <!-- Vaciar carrito -->
                <form method="POST" action="compras">
                    <input type="hidden" name="accion" value="vaciar">
                    <button type="submit" class="btn-vaciar"
                            onclick="return confirm('¿Vaciar todo el carrito?')">
                         Vaciar carrito
                    </button>
                </form>

                <!-- Procesar compra -->
                <% if (logueado) { %>
                    <form method="POST" action="compras">
                        <input type="hidden" name="accion" value="comprar">
                        <button type="submit" class="btn-comprar">
                             Finalizar compra
                        </button>
                    </form>
                <% } else { %>
                    <a href="login.jsp?msg=Inicia+sesion+para+comprar&tipoMsg=error" class="btn-comprar"
                       style="text-decoration:none; display:inline-block;">
                         Inicia sesión para comprar
                    </a>
                <% } %>
            </div>
        <% } %>
    </div>
</div>

<%@include file="lib/footer.jsp"%>
