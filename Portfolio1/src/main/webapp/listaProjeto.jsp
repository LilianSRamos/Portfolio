<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Projetos" />
<%@ include file="/topo.jspf" %>

<div class="d-flex justify-content-between align-items-end mb-3 flex-wrap gap-2">
  <div>
    <h1 class="h3 titulo-pagina">Projetos</h1>
    <div class="subtitulo">Cadastre, edite e exclua os projetos do seu portfólio.</div>
  </div>
  <a class="btn btn-destaque" href="${pageContext.request.contextPath}/cadastroProjeto">Novo projeto</a>
</div>

<c:choose>
  <c:when test="${param.msg == 'criado'}"><div class="alert alert-success">Projeto cadastrado.</div></c:when>
  <c:when test="${param.msg == 'atualizado'}"><div class="alert alert-success">Projeto atualizado.</div></c:when>
  <c:when test="${param.msg == 'excluido'}"><div class="alert alert-success">Projeto excluído.</div></c:when>
  <c:when test="${param.msg == 'naoencontrado'}"><div class="alert alert-warning">Esse projeto não existe mais.</div></c:when>
</c:choose>

<div class="painel p-3">
  <c:choose>
    <c:when test="${empty projetos}">
      <div class="vazio">
        <p class="mb-3">Nenhum projeto cadastrado ainda.</p>
        <a class="btn btn-destaque" href="${pageContext.request.contextPath}/cadastroProjeto">Cadastrar o primeiro projeto</a>
      </div>
    </c:when>
    <c:otherwise>
      <div class="table-responsive">
        <table class="table tabela mb-0">
          <thead>
            <tr>
              <th>Título</th>
              <th>Descrição</th>
              <th>Tecnologias</th>
              <th>Data</th>
              <th class="text-end">Ações</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach var="p" items="${projetos}">
              <tr>
                <td>
                  <strong><c:out value="${p.titulo}"/></strong><br>
                  <a href="<c:out value='${p.link}'/>" target="_blank" rel="noopener" class="small">Abrir link</a>
                </td>
                <td class="descricao"><c:out value="${p.descricao}"/></td>
                <td>
                  <c:forTokens var="t" items="${p.tecnologias}" delims=",">
                    <span class="etiqueta"><c:out value="${t}"/></span>
                  </c:forTokens>
                </td>
                <td><c:out value="${p.data_criacao}"/></td>
                <td class="text-end text-nowrap">
                  <a class="btn btn-sm btn-outline-secondary"
                     href="${pageContext.request.contextPath}/selecaoProjeto?id=${p.id}">Editar</a>
                  <form class="d-inline" method="post"
                        action="${pageContext.request.contextPath}/excluirProjeto"
                        onsubmit="return confirm('Excluir o projeto &quot;' + this.dataset.nome + '&quot;?');"
                        data-nome="<c:out value='${p.titulo}'/>">
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
