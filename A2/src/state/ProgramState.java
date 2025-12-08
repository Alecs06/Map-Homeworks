package state;

import model.statement.Statement;

import java.util.concurrent.atomic.AtomicInteger;

public class ProgramState {
    private static final AtomicInteger lastId = new AtomicInteger(0);

    private final int id;
    private final ExecutionStack executionStack;
    private final SymbolTable symbolTable;
    private final Out out;
    private final FileTable fileTable;
    private final Heap heap;

    private static int getNextId() {
        return lastId.incrementAndGet();
    }

    public ProgramState(ExecutionStack executionStack, SymbolTable symbolTable, Out out, FileTable fileTable, Heap heap) {
        this.id = getNextId();
        this.executionStack = executionStack;
        this.symbolTable = symbolTable;
        this.out = out;
        this.fileTable = fileTable;
        this.heap = heap;
    }

    public int id() { return id; }

    public ExecutionStack executionStack() { return executionStack; }
    public SymbolTable symbolTable() { return symbolTable; }
    public Out out() { return out; }
    public FileTable fileTable() { return fileTable; }
    public Heap heap() { return heap; }

    public boolean isNotCompleted() {
        return !executionStack.isEmpty();
    }

    public ProgramState oneStep() throws Exception {
        if (executionStack.isEmpty()) {
            throw new Exception("prgstate stack is empty");
        }
        Statement crtStmt = executionStack.pop();
        return crtStmt.execute(this);
    }

    @Override
    public String toString() {
        return "Id:" + id + "\n" +
                "ExecutionStack:" + executionStack + "\n" +
                "SymbolTable:" + symbolTable + "\n" +
                "Out:" + out + "\n" +
                "FileTable:" + fileTable + "\n" +
                "Heap:" + heap + "\n";
    }
}
