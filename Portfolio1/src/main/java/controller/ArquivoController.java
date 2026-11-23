package controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.Arquivos;

@WebServlet(name = "ArquivoController", urlPatterns = { "/uploads/*" })
public class ArquivoController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String info = request.getPathInfo();
        String nome = (info == null || info.length() < 2) ? null : info.substring(1);

        if (!Arquivos.nomeValido(nome)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Path arquivo = Arquivos.pasta().resolve(nome);
        if (!Files.isRegularFile(arquivo)) {
            String real = getServletContext().getRealPath("/assets/uploads/" + nome);
            arquivo = real == null ? null : Paths.get(real);
        }
        if (arquivo == null || !Files.isRegularFile(arquivo)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String tipo = getServletContext().getMimeType(nome);
        response.setContentType(tipo != null && tipo.startsWith("image/") ? tipo : "application/octet-stream");
        response.setContentLengthLong(Files.size(arquivo));
        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setHeader("Cache-Control", "public, max-age=86400");
        Files.copy(arquivo, response.getOutputStream());
    }
}
