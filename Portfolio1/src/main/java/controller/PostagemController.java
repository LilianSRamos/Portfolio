package controller;

import java.io.IOException;
import java.sql.SQLException;

import dao.PostagemDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Postagem;
import model.Usuario;
import util.Arquivos;

@WebServlet(name = "PostagemController", urlPatterns = { "/postagens", "/postagem", "/minhasPostagens",
        "/cadastroPostagem", "/selecaoPostagem", "/editarPostagem", "/excluirPostagem" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 5 * 1024 * 1024, maxRequestSize = 6 * 1024 * 1024)
public class PostagemController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final transient PostagemDAO dao = new PostagemDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processar(request, response, false);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processar(request, response, true);
    }

    private void processar(HttpServletRequest request, HttpServletResponse response, boolean post)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String rota = request.getServletPath();
        String raiz = request.getContextPath();

        try {
            switch (rota) {

            case "/postagens": // READ 
                request.setAttribute("postagens", dao.listarPublicadas(0));
                encaminhar(request, response, "/postagens.jsp");
                break;

            case "/postagem": { // READ 
                Postagem p = dao.buscarPorId(lerId(request));
                Usuario u = usuarioLogado(request);
                boolean visivel = p != null
                        && ("PUBLICADA".equals(p.getStatus()) || (u != null && u.getId() == p.getAutorId()));
                if (!visivel) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                request.setAttribute("postagem", p);
                encaminhar(request, response, "/postagem.jsp");
                break;
            }

            case "/minhasPostagens": // READ 
                request.setAttribute("postagens", dao.listarPorAutor(usuarioLogado(request).getId()));
                encaminhar(request, response, "/minhasPostagens.jsp");
                break;

            case "/cadastroPostagem": // CREATE
                if (post) {
                    criar(request, response);
                } else {
                    encaminhar(request, response, "/cadastroPostagem.jsp");
                }
                break;

            case "/selecaoPostagem": { 
                Postagem p = dao.buscarPorId(lerId(request));
                if (!doAutor(request, p)) {
                    response.sendRedirect(raiz + "/minhasPostagens?msg=semPermissao");
                    return;
                }
                request.setAttribute("postagem", p);
                encaminhar(request, response, "/editarPostagem.jsp");
                break;
            }

            case "/editarPostagem": // UPDATE
                if (post) {
                    atualizar(request, response);
                } else {
                    response.sendRedirect(raiz + "/minhasPostagens");
                }
                break;

            case "/excluirPostagem": // DELETE
                if (post) {
                    Postagem p = dao.buscarPorId(lerId(request));
                    if (doAutor(request, p) && dao.excluir(p.getId(), p.getAutorId())) {
                        Arquivos.apagar(p.getImagem());
                        response.sendRedirect(raiz + "/minhasPostagens?msg=excluida");
                    } else {
                        response.sendRedirect(raiz + "/minhasPostagens?msg=semPermissao");
                    }
                } else {
                    response.sendRedirect(raiz + "/minhasPostagens");
                }
                break;

            default:
                response.sendRedirect(raiz + "/postagens");
            }
        } catch (SQLException e) {
            throw new ServletException("Erro ao acessar o banco de dados", e);
        }
    }

    // CREATE
    private void criar(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        Postagem p = lerCampos(request);
        String erro = validar(p);
        String imagem = null;

        if (erro == null) {
            try {
                // imagem opcional, mas se enviada precisa ser uma imagem válida
                imagem = Arquivos.salvarImagem(request.getPart("imagem"));
            } catch (IllegalArgumentException e) {
                erro = e.getMessage();
            }
        }
        if (erro != null) {
            request.setAttribute("erro", erro);
            request.setAttribute("postagem", p);
            encaminhar(request, response, "/cadastroPostagem.jsp");
            return;
        }

        p.setImagem(imagem);
        p.setAutorId(usuarioLogado(request).getId());
        dao.inserir(p);
        response.sendRedirect(request.getContextPath() + "/minhasPostagens?msg=criada");
    }

    // UPDATE
    private void atualizar(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        Postagem atual = dao.buscarPorId(lerId(request));
        if (!doAutor(request, atual)) {
            response.sendRedirect(request.getContextPath() + "/minhasPostagens?msg=semPermissao");
            return;
        }

        Postagem novo = lerCampos(request);
        novo.setId(atual.getId());
        novo.setAutorId(atual.getAutorId());
        novo.setImagem(atual.getImagem());

        String erro = validar(novo);
        String imagemNova = null;
        if (erro == null) {
            try {
                imagemNova = Arquivos.salvarImagem(request.getPart("imagem"));
            } catch (IllegalArgumentException e) {
                erro = e.getMessage();
            }
        }
        if (erro != null) {
            request.setAttribute("erro", erro);
            request.setAttribute("postagem", novo);
            encaminhar(request, response, "/editarPostagem.jsp");
            return;
        }

        if (imagemNova != null) {
            novo.setImagem(imagemNova);
        }
        dao.atualizar(novo);
        if (imagemNova != null) {
            Arquivos.apagar(atual.getImagem()); 
        }
        response.sendRedirect(request.getContextPath() + "/minhasPostagens?msg=atualizada");
    }

    private Postagem lerCampos(HttpServletRequest request) {
        Postagem p = new Postagem();
        p.setTitulo(texto(request, "titulo"));
        p.setConteudo(texto(request, "conteudo"));
        String status = texto(request, "status");
        p.setStatus("PUBLICADA".equals(status) ? "PUBLICADA" : "RASCUNHO");
        return p;
    }

    private String validar(Postagem p) {
        if (p.getTitulo().isEmpty() || p.getConteudo().isEmpty()) {
            return "Preencha o título e o conteúdo.";
        }
        if (p.getTitulo().length() > 180) {
            return "O título pode ter no máximo 180 caracteres.";
        }
        return null;
    }

    private boolean doAutor(HttpServletRequest request, Postagem p) {
        Usuario u = usuarioLogado(request);
        return p != null && u != null && p.getAutorId() == u.getId();
    }

    private Usuario usuarioLogado(HttpServletRequest request) {
        HttpSession sessao = request.getSession(false);
        return sessao == null ? null : (Usuario) sessao.getAttribute("usuarioLogado");
    }

    private void encaminhar(HttpServletRequest request, HttpServletResponse response, String pagina)
            throws ServletException, IOException {
        request.getRequestDispatcher(pagina).forward(request, response);
    }

    private int lerId(HttpServletRequest request) {
        try {
            return Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private String texto(HttpServletRequest request, String campo) {
        String v = request.getParameter(campo);
        return v == null ? "" : v.trim();
    }
}
