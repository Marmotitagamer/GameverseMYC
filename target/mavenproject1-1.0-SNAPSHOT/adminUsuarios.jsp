<%@page import="java.util.LinkedList"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Modelo.Usuario"%>
<%@page import="Modelo.GestionarUsuarios"%>
<%@include file="lib/header.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<style>
    /* estilo para el bóton  "eliminar"  */
    button.btn.custom-btn,
    input[type="submit"].btn.custom-btn {
        appearance: none;       
        -webkit-appearance: none;
        color: #fff !important;
        background-color: #dc3545 !important;
        border: none;
        padding: 0.25rem 0.5rem;
        border-radius: 0.2rem;
        font-size: 0.875rem;
        line-height: 1.2;
        cursor: pointer;
    }

    button.btn.custom-btn:hover {
        background-color: #bb2d3b !important; 
    }
</style>
<div class="container mt-4">
    <h1 class="mb-4 text-center">Administración de Usuarios</h1>

    <!-- Formulario para búsqueda - filtro -->
    <form class="container mt-4" method="get" action="adminUsuarios.jsp">
        <div class="input-group">
            <input type="text" name="cedula" 
                   value="<%= request.getParameter("cedula") != null ? request.getParameter("cedula") : ""%>" 
                   class="form-control" placeholder="Buscar por cédula...">
            <button class="btn btn-primary" type="submit">Buscar</button>
        </div>
    </form>

    <a href="registroUsuario.jsp" class="btn btn-success mb-3">Agregar Usuario</a>

    <table class="table table-bordered table-hover">
        <thead class="table-dark">
            <tr>
                <th>Cédula</th>
                <th>Nombre</th>
                <th>Edad</th>
                <th>Tipo</th>
                <th>Acciones</th>
            </tr>
        </thead>
        <tbody>
            <%
                GestionarUsuarios gestor = new GestionarUsuarios();
                LinkedList<Usuario> listaCompleta = gestor.ListarUsuarios();

                String buscar = request.getParameter("cedula");
                LinkedList<Usuario> lista = new LinkedList<>(listaCompleta); 
                if (buscar != null && !buscar.trim().isEmpty()) {
                    lista.removeIf(u -> !u.getCedula().contains(buscar.trim()));
                }

                for (Usuario u : lista) {
            %>
            <tr>
                <td><%=u.getCedula()%></td>
                <td><%=u.getNombre()%></td>
                <td><%=u.getEdad()%></td>
                <td><%=u.getTipoUsuario()%></td>
                
                <td>
                    <div class="d-flex gap-1">
                        <!-- Botón eliminar -->
                        <form action="usuarios" method="post" style="display:inline;">
                            <input type="hidden" name="accion" value="eliminar">
                            <input type="hidden" name="cedula" value="<%=u.getCedula()%>">
                            <button type="submit" class="btn btn-danger btn-sm custom-btn"
                                    onclick="return confirm('¿Eliminar usuario?')">
                                Eliminar
                            </button>
                        </form>

                        <!-- Botón editar -->
                        <button type="button"
                                class="btn btn-warning btn-sm"
                                data-bs-toggle="modal"
                                data-bs-target="#modalEditar"
                                onclick="cargarUsuario('<%=u.getCedula()%>', '<%=u.getNombre()%>', '<%=u.getEdad()%>', '<%=u.getTipoUsuario()%>')">
                            Editar
                        </button>
                    </div>
                </td>
            </tr>
            <% }%>
        </tbody>
    </table>
</div>


<div class="modal fade" id="modalEditar" tabindex="-1" aria-labelledby="editarLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <form action="usuarios" method="post">
                <div class="modal-header">
                    <h5 class="modal-title" id="editarLabel">Editar Usuario</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <input type="hidden" name="accion" value="editar">
                    <input type="hidden" id="idEditar" name="cedula">

                    <div class="mb-3">
                        <label>Nombre:</label>
                        <input type="text" id="nombreEditar" name="nombre" class="form-control" required>
                    </div>

                    <div class="mb-3">
                        <label>Edad:</label>
                        <input type="number" id="edadEditar" name="edad" class="form-control" required>
                    </div>

                    <div class="mb-3">
                        <label>Tipo:</label>
                        <input type="text" id="tipoEditar" name="tipoUsuario" class="form-control" required>
                    </div>

                </div>

                <div class="modal-footer">
                    <button type="submit" class="btn btn-primary">Guardar Cambios</button>
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
    function cargarUsuario(id, nombre, edad, tipo) {
        document.getElementById('idEditar').value = id;
        document.getElementById('nombreEditar').value = nombre;
        document.getElementById('edadEditar').value = edad;
        document.getElementById('tipoEditar').value = tipo;
    }
</script>

<%@include file="lib/footer.jsp" %>