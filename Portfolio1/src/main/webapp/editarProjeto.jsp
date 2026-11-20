<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Editar projeto" />
<%@ include file="/topo.jspf" %>

<h1 class="h3 titulo-pagina">Editar projeto</h1>
<div class="subtitulo mb-3">Altere os dados e salve as mudanças.</div>

<div class="painel p-4">
  <form method="post" action="${pageContext.request.contextPath}/editarProjeto">
    <input type="hidden" name="id" value="${projeto.id}">
    <div class="mb-3">
      <label for="titulo" class="form-label">Título</label>
      <input type="text" class="form-control" id="titulo" name="titulo" maxlength="100"
             value="<c:out value='${projeto.titulo}'/>" required>
    </div>
    <div class="mb-3">
      <label for="descricao" class="form-label">Descrição</label>
      <textarea class="form-control" id="descricao" name="descricao" rows="4" required><c:out value="${projeto.descricao}"/></textarea>
    </div>
    <div class="mb-3">
      <label for="link" class="form-label">Link do projeto</label>
      <input type="url" class="form-control" id="link" name="link" maxlength="250"
             value="<c:out value='${projeto.link}'/>" required>
    </div>
    <div class="row">
      <div class="col-md-8 mb-3">
        <label for="tecnologias" class="form-label">Tecnologias</label>
        <input type="text" class="form-control" id="tecnologias" name="tecnologias" maxlength="250"
               value="<c:out value='${projeto.tecnologias}'/>" required>
        <div class="form-text">Separe com vírgulas.</div>
      </div>
      <div class="col-md-4 mb-3">
        <label for="data_criacao" class="form-label">Data de criação</label>
        <input type="date" class="form-control" id="data_criacao" name="data_criacao"
               value="<c:out value='${projeto.data_criacao}'/>" required>
      </div>
    </div>
    <div class="d-flex gap-2">
      <button type="submit" class="btn btn-destaque">Salvar alterações</button>
      <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/listaProjeto">Cancelar</a>
    </div>
  </form>
</div>

<%@ include file="/rodape.jspf" %>
