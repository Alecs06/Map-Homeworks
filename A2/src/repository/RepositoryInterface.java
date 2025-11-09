package repository;

import state.ProgramState;

import java.io.IOException;

public interface RepositoryInterface {
    ProgramState getCrtPrg();
    void setCrtPrg(ProgramState state);
    void logCrtPrg() throws IOException;
}
