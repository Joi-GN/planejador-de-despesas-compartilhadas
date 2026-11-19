package model;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

/**
 *
 * @author Joice
 */
public class RepositorioArquivo implements RepositorioPlanejamento {
    private final Path arquivo;

    public RepositorioArquivo(Path arquivo) {
        this.arquivo = arquivo.toAbsolutePath();
    }

    @Override
    public void salvar(List<Planejamento> planejamentos) throws IOException {
        if (planejamentos.isEmpty()) {
            throw new IllegalArgumentException("Não há projetos cadastrados.");
        }

        Path temporario = Files.createTempFile(arquivo.getParent(),
                "planejamentos-", ".tmp");
        try {
            try (OutputStream fluxo = Files.newOutputStream(temporario);
                    ObjectOutputStream saida = new ObjectOutputStream(fluxo)) {
                saida.writeObject(planejamentos);
            }
            Files.move(temporario, arquivo, StandardCopyOption.ATOMIC_MOVE);
        } finally {
            Files.deleteIfExists(temporario);
        }
    }

    @Override
    public List<Planejamento> carregar() throws IOException {
        try (InputStream fluxo = Files.newInputStream(arquivo);
                ObjectInputStream entrada = new ObjectInputStream(fluxo)) {
            List<Planejamento> p = (List<Planejamento>) entrada.readObject();
            if (p instanceof List<Planejamento> planejamentos) {
                return planejamentos;
            }
            throw new IOException("O arquivo não contém planejamentos.");
        } catch (NoSuchFileException erro) {
            throw new IOException("Nenhum arquivo foi encontrado.");
        } catch (ClassNotFoundException erro) {
            throw new IOException("Uma classe do planejamento não está disponível.", erro);
        }
    }
}
