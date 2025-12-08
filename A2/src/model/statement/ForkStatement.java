package model.statement;

import state.*;

public record ForkStatement(Statement statement) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) {

        ExecutionStack childStack = new ListExecutionStack();
        childStack.push(statement);
        SymbolTable childSymbolTable = state.symbolTable().deepCopy();
        Heap sharedHeap = state.heap();
        FileTable sharedFileTable = state.fileTable();
        Out sharedOut = state.out();

        ProgramState newPrg = new ProgramState(childStack, childSymbolTable, sharedOut, sharedFileTable, sharedHeap);
        return newPrg;
    }

    @Override
    public Statement deepCopy() {
        return new ForkStatement(statement.deepCopy());
    }

    @Override
    public String toString() {
        return "fork(" + statement + ")";
    }
}

