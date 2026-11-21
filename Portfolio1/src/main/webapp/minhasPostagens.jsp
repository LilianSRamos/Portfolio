<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="tituloPagina" value="Minhas postagens" />
<%@ include file="/topo.jspf" %>

<div class="d-flex justify-content-between align-items-end mb-3 flex-wrap gap-2">
  <div>
    <h1 class="h3 titulo-pagina">Minhas postagens</h1>
    <div class="subtitulo">Crie, edite e exclua as suas postagens.</div>
  </div>
  <a class="btn btn-destaque" href="${raiz}/cadastroPostagem">Nova postagem</a>
</div>

<c:choose>
  <c:when test="${param.msg == 'criada'}"><div class="alert alert-success">Postagem criada.</div></c:when>
  <c:when test="${param.msg == 'atualizada'}"><div class="alert alert-success">Postagem atualizada.</div></c:when>
  <c:when test="${param.msg == 'excluida'}"><div class="alert alert-success">Postagem excluída.</div></c:when>
  <c:when test="${param.msg == 'semPermissao'}"><div class="alert alert-warning">Você só pode alterar as suas próprias postagens.</div></c:when>
</c:choose>

<div class="painel p-3">
  <c:choose>
    <c:when test="${empty postagens}">
      <div class="vazio">
        <p class="mb-3">Você ainda não escreveu nenhuma postagem.</p>
        <a class="btn btn-destaque" href="${raiz}/cadastroPostagem">Escrever a primeira postagem</a>
      </div>
    </c:when>
    <c:otherwise>
      <div class="table-responsive">
        <table class="table tabela mb-0">
          <thead>
            <tr>
              <th>Título</th>
              <th>Situação</th>
              <th>Data</th>
              <th class="text-end">Ações</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach var="p" items="${postagens}">
              <tr>
                <td><a href="${raiz}/postagem?id=${p.id}"><strong><c:out value="${p.titulo}"/></strong></a></td>
                <td>
                  <c:choose>
                    <c:when test="${p.status == 'PUBLICADA'}"><span class="etiqueta">Publicada</span></c:when>
                    <c:otherwise><span class="badge text-bg-secondary">Rascunho</span></c:otherwise>
                  </c:choose>
                </td>
                <td><fmt:formatDate value="${p.dataPublicacao}" pattern="dd/MM/yyyy"/></td>
                <td class="text-end text-nowrap">
                  <a class="btn btn-sm btn-outline-secondary" href="${raiz}/selecaoPostagem?id=${p.id}">Editar</a>
                  <form class="d-inline" method="post" action="${raiz}/excluirPostagem"
                        onsubmit="return confirm('Excluir esta postagem?');">
                    <input type="hidden" name="id" value="${p.id}">
                    <button type="submit" class="btn btn-sm btn-outline-danger">Excluir</button>
                  </form>
                </td>
              </tr>
            </c:forEach>
          </tbody>
        </table>
      </div>
    </c:otherwise>
  </c:choose>
</div>

<%@ include file="/rodape.jspf" %>
