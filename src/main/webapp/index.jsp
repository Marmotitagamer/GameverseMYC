<%@include file="lib/header.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!-- Web pages 2026_1 -->
<!DOCTYPE html>
<div style="height: 20px;">
    <img src="images/baner.jpg" alt="banner principal">
</div>

<div class=" container" >
    <form action="registroUsuario.jsp" method="GET">
        <button type="submit" class="btn btn-primary"> Registrar Usuario</button>
        <a href="adminUsuarios.jsp" class="btn btn-secondary">Ver Registros Guardados</a>
    </form>
</div>

<%@include   file="lib/footer.jsp" %>


