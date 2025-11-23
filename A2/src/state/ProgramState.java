package state;

public record ProgramState(
        ExecutionStack executionStack,
        SymbolTable symbolTable,
        Out out,
        FileTable fileTable,
        Heap heap) {

    @Override
    public String toString() {
        return "ProgramState{" +
                "executionStack=" + executionStack +
                ", symbolTable=" + symbolTable +
                ", out=" + out +
                ", fileTable=" + fileTable +
                ", heap=" + heap +
                '}';
    }
}
