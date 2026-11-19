package model;

import java.io.IOException;
import java.util.List;

/**
 *
 * @author Joice
 */
public interface RepositorioPlanejamento {
    void salvar(List<Planejamento> planejamentos) throws IOException;

    List<Planejamento> carregar() throws IOException;
}
