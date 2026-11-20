package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dao.CadastroProjetos;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Projeto;

@WebServlet(name = "ProjetoController", urlPatterns = { "/ProjetoController", "/cadastroProjeto", "/listaProjeto",
        "/editarProjeto", "/selecaoProjeto", "/excluirProjeto" })
public class ProjetoController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /*
     * Mapa utilizado para associar as rotas às páginas JSP.
     * Exemplo: /cadastroProjeto -> cadastroProjeto.jsp
     */
    private final Map<String, String> rotas = new HashMap<>();

    public ProjetoController() {
        rotas.put("/cadastroProjeto", "/cadastroProjeto.jsp");
        rotas.put("/listaProjeto", "/listaProjeto.jsp");
        rotas.put("/selecaoProjeto", "/editarProjeto.jsp");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processar(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processar(request, response);
    }

    private void processar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String rota = request.getServletPath();
        boolean post = "POST".equalsIgnoreCase(request.getMethod());

        try {
            switch (rota) {
            case "/cadastroProjeto":
                if (post) {
                    criar(request);                       // CREATE
                    response.sendRedirect(request.getContextPath() + "/listaProjeto?msg=criado");
                } else {
                    encaminhar(request, response, rota);  // abre o formulário vazio
                }
                break;

            case "/listaProjeto":                         // READ (lista)
                ArrayList<Projeto> lista = new CadastroProjetos().listarProjetos();
                request.setAttribute("projetos", lista);
                encaminhar(request, response, rota);
                break;

            case "/selecaoProjeto":                       // READ (um registro) -> abre a edição
                Projeto p = new Projeto();
                p.setId(lerId(request));
                p.buscaProjeto(p);
                if (p.getTitulo() == null) {              // id não existe
                    response.sendRedirect(request.getContextPath() + "/listaProjeto?msg=naoencontrado");
                    return;
                }
                request.setAttribute("projeto", p);
                encaminhar(request, response, rota);
                break;

            case "/editarProjeto":                        // UPDATE
                if (post) {
                    atualizar(request);
                    response.sendRedirect(request.getContextPath() + "/listaProjeto?msg=atualizado");
                } else {
                    response.sendRedirect(request.getContextPath() + "/listaProjeto");
                }
                break;

            case "/excluirProjeto":                       // DELETE
                if (post) {
                    Projeto d = new Projeto();
                    d.setId(lerId(request));
                    d.excluirProjeto(d);
                    response.sendRedirect(request.getContextPath() + "/listaProjeto?msg=excluido");
                } else {
                    response.sendRedirect(request.getContextPath() + "/listaProjeto");
                }
                break;

            default:                                      // /ProjetoController
                response.sendRedirect(request.getContextPath() + "/listaProjeto");
            }
        } catch (SQLException e) {
            throw new ServletException("Erro ao acessar o banco de dados", e);
        }
    }

    private void encaminhar(HttpServletRequest request, HttpServletResponse response, String rota)
            throws ServletException, IOException {
        request.getRequestDispatcher(rotas.get(rota)).forward(request, response);
    }

    private int lerId(HttpServletRequest request) {
        try {
            return Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void criar(HttpServletRequest request) throws SQLException {
        Projeto p = new Projeto(
                request.getParameter("titulo").trim(),
                request.getParameter("descricao").trim(),
                request.getParameter("link").trim(),
                request.getParameter("tecnologias").trim(),
                request.getParameter("data_criacao"));
        p.cadastrarProjeto(p);
    }

    private void atualizar(HttpServletRequest request) throws SQLException {
        Projeto p = new Projeto(
                lerId(request),
                request.getParameter("titulo").trim(),
                request.getParameter("descricao").trim(),
                request.getParameter("link").trim(),
                request.getParameter("tecnologias").trim(),
                request.getParameter("data_criacao"));
        p.editaProjeto(p);
    }
}