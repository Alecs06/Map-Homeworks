package model.statement;

import state.*;
import model.dictionary.MyIDictionary;
import model.type.Type;

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

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws Exception {
        // fork executes the statement in a copy of the environment
        statement.typecheck(typeEnv.deepCopy());
        return typeEnv;
    }
}
