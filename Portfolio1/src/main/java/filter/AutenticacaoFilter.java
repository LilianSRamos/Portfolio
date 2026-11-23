package filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter(urlPatterns = {
      
        "/ProjetoController", "/cadastroProjeto", "/listaProjeto", "/editarProjeto", "/selecaoProjeto",
        "/excluirProjeto",
        "/minhasPostagens", "/cadastroPostagem", "/selecaoPostagem", "/editarPostagem", "/excluirPostagem" })
public class AutenticacaoFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        HttpSession sessao = request.getSession(false);
        boolean logado = sessao != null && sessao.getAttribute("usuarioLogado") != null;

        if (logado) {
            chain.doFilter(req, res);
        } else {
            response.sendRedirect(request.getContextPath() + "/login?msg=entre");
        }
    }
}
