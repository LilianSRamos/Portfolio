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
        }
    }
}
