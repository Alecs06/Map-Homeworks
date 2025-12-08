package repository;

import state.ProgramState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class Repository implements RepositoryInterface{
    private List<ProgramState> programStates;
    private final String logFileName;

    public Repository(List<ProgramState> programStates) {
        if (programStates == null || programStates.isEmpty()) {
            throw new IllegalArgumentException("Program states list cannot be null or empty");
        }
        this.programStates = programStates;
        this.logFileName = "logFile.txt";
    }
    public Repository(List<ProgramState> programStates,String logFileName) {
        if (programStates == null || programStates.isEmpty()) {
            throw new IllegalArgumentException("Program states list cannot be null or empty");
        }
        this.programStates = programStates;
        this.logFileName = logFileName;
    }

    @Override
    public List<ProgramState> getPrgList() {
        return programStates;
    }

    @Override
    public void setPrgList(List<ProgramState> prgList) {
        this.programStates = prgList;
    }

    @Override
    public void logPrgStateExec(ProgramState state) throws IOException {
        try (PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(logFileName,true)))) {
            printWriter.println(state);
        }
    }
}
