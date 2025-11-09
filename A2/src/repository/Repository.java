package repository;

import state.ProgramState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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

    @Override
    public void setCrtPrg(ProgramState state) {
        programStates.set(currentStateIndex, state);
    }

    @Override
    public void logCrtPrg() throws IOException {
        PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter("logFile.txt",true)));
        printWriter.println(getCrtPrg());
        printWriter.close();
    }
}
