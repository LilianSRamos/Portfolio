package util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import jakarta.servlet.http.Part;

/**
 * Guarda as imagens enviadas pelo usuário.
 *
 * As imagens ficam numa pasta FORA do projeto (C:\Users\SEU_USUARIO\portfolio-uploads),
 * assim elas não se perdem quando o Eclipse limpa ou republica o servidor.
 * O nome do arquivo é gerado com UUID, como no exemplo do repositório do professor.
 */
public final class Arquivos {

    private static final Set<String> EXTENSOES = Set.of("png", "jpg", "jpeg", "gif", "webp");

    private Arquivos() {}

    public static Path pasta() throws IOException {
        Path pasta = Paths.get(System.getProperty("user.home"), "portfolio-uploads");
        Files.createDirectories(pasta);
        return pasta;
    }

    /** Só aceita nomes simples, sem barras nem "..". */
    public static boolean nomeValido(String nome) {
        return nome != null && nome.matches("[A-Za-z0-9._-]{1,120}") && !nome.contains("..");
    }

    /**
     * Salva a imagem enviada e devolve o novo nome do arquivo.
     * Devolve null se nenhum arquivo foi escolhido.
     * Lança IllegalArgumentException se o arquivo não for uma imagem aceita.
     */
    public static String salvarImagem(Part parte) throws IOException {
        if (parte == null || parte.getSize() == 0) {
            return null;
        }
        String original = parte.getSubmittedFileName();
        String tipo = parte.getContentType();
        String extensao = "";
        if (original != null && original.contains(".")) {
            extensao = original.substring(original.lastIndexOf('.') + 1).toLowerCase(Locale.ROOT);
        }
        if (!EXTENSOES.contains(extensao) || tipo == null || !tipo.startsWith("image/")) {
            throw new IllegalArgumentException("Envie uma imagem PNG, JPG, GIF ou WEBP.");
        }
        String nome = UUID.randomUUID() + "." + extensao;
        try (InputStream entrada = parte.getInputStream()) {
            Files.copy(entrada, pasta().resolve(nome), StandardCopyOption.REPLACE_EXISTING);
        }
        return nome;
    }

    public static void apagar(String nome) {
        if (!nomeValido(nome)) {
            return;
        }
        try {
            Files.deleteIfExists(pasta().resolve(nome));
        } catch (IOException e) {
            // se não deu para apagar o arquivo, o registro no banco continua válido
        }
    }
}
