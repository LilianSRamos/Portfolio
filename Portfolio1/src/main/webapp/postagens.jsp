<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Postagens" />
<%@ include file="/topo.jspf" %>

<div class="d-flex justify-content-between align-items-end mb-3 flex-wrap gap-2">
  <div>
    <h1 class="h3 titulo-pagina">Postagens</h1>
    <div class="subtitulo">Tudo o que foi publicado.</div>
  </div>
  <c:if test="${not empty sessionScope.usuarioLogado}">
    <a class="btn btn-destaque" href="${raiz}/cadastroPostagem">Nova postagem</a>
  </c:if>
</div>

<c:choose>
  <c:when test="${empty postagens}">
    <div class="painel vazio"><p class="mb-0">Ainda não há postagens publicadas.</p></div>
  </c:when>
  <c:otherwise>
    <div class="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-3">
      <c:forEach var="p" items="${postagens}">
        <%@ include file="/cartaoPostagem.jspf" %>
      </c:forEach>
    </div>
  </c:otherwise>
</c:choose>

<%@ include file="/rodape.jspf" %>
