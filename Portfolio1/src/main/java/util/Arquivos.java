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

public final class Arquivos {

    private static final Set<String> EXTENSOES = Set.of("png", "jpg", "jpeg", "gif", "webp");

    private Arquivos() {}

    public static Path pasta() throws IOException {
        Path pasta = Paths.get(System.getProperty("user.home"), "portfolio-uploads");
        Files.createDirectories(pasta);
        return pasta;
    }
    
    public static boolean nomeValido(String nome) {
        return nome != null && nome.matches("[A-Za-z0-9._-]{1,120}") && !nome.contains("..");
    }

    /**
     * Salva a imagem, que é opcional: se nada foi enviado, devolve null.
     * Se algo foi enviado, o arquivo precisa ser uma imagem válida (PNG, JPG, GIF ou WEBP).
     *
     * @throws IllegalArgumentException se o arquivo enviado não for uma imagem válida.
     */
    public static String salvarImagem(Part parte) throws IOException {
        if (parte == null || parte.getSize() == 0) {
            return null;
        }
        String extensao = validarImagem(parte);
        String nome = UUID.randomUUID() + "." + extensao;
        try (InputStream entrada = parte.getInputStream()) {
            Files.copy(entrada, pasta().resolve(nome), StandardCopyOption.REPLACE_EXISTING);
        }
        return nome;
    }

    /** Confere extensão, tipo declarado e o conteúdo real do arquivo. Devolve a extensão em minúsculas. */
    private static String validarImagem(Part parte) throws IOException {
        String mensagem = "Envie uma imagem PNG, JPG, GIF ou WEBP válida.";
        String original = parte.getSubmittedFileName();
        String tipo = parte.getContentType();
        String extensao = "";
        if (original != null && original.contains(".")) {
            extensao = original.substring(original.lastIndexOf('.') + 1).toLowerCase(Locale.ROOT);
        }
        if (!EXTENSOES.contains(extensao) || tipo == null || !tipo.toLowerCase(Locale.ROOT).startsWith("image/")) {
            throw new IllegalArgumentException(mensagem);
        }

        // Lê os primeiros bytes para conferir a "assinatura" do arquivo (evita renomear um .exe/.pdf para .png)
        byte[] c = new byte[12];
        int lidos;
        try (InputStream entrada = parte.getInputStream()) {
            lidos = entrada.readNBytes(c, 0, c.length);
        }
        String real = tipoReal(c, lidos);
        boolean confere = real != null && (real.equals(extensao) || (real.equals("jpg") && extensao.equals("jpeg")));
        if (!confere) {
            throw new IllegalArgumentException(mensagem);
        }
        return extensao;
    }

    private static String tipoReal(byte[] c, int n) {
        if (n >= 8 && (c[0] & 0xFF) == 0x89 && c[1] == 'P' && c[2] == 'N' && c[3] == 'G'
                && c[4] == 0x0D && c[5] == 0x0A && c[6] == 0x1A && c[7] == 0x0A) {
            return "png";
        }
        if (n >= 3 && (c[0] & 0xFF) == 0xFF && (c[1] & 0xFF) == 0xD8 && (c[2] & 0xFF) == 0xFF) {
            return "jpg";
        }
        if (n >= 6 && c[0] == 'G' && c[1] == 'I' && c[2] == 'F' && c[3] == '8'
                && (c[4] == '7' || c[4] == '9') && c[5] == 'a') {
            return "gif";
        }
        if (n >= 12 && c[0] == 'R' && c[1] == 'I' && c[2] == 'F' && c[3] == 'F'
                && c[8] == 'W' && c[9] == 'E' && c[10] == 'B' && c[11] == 'P') {
            return "webp";
        }
        return null;
    }

    public static void apagar(String nome) {
        if (!nomeValido(nome)) {
            return;
        }
        try {
            Files.deleteIfExists(pasta().resolve(nome));
        } catch (IOException e) {
        }
    }
}
