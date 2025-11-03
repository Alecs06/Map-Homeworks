package repository;

import state.ProgramState;

import java.util.List;

public class Repository implements RepositoryInterface{
    private final List<ProgramState> programStates;
    private final int currentStateIndex;

    public Repository(List<ProgramState> programStates) {
        this.programStates = programStates;
        this.currentStateIndex = 0;
        if (programStates.isEmpty()) {
            throw new IllegalArgumentException("Program states list cannot be empty");
        }
    }


    @Override
    public ProgramState getCrtPrg() {
        return programStates.get(currentStateIndex);
    }
}
