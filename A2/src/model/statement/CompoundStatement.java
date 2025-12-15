package model.statement;

import state.ExecutionStack;
import state.ProgramState;
import model.dictionary.MyIDictionary;
import model.type.Type;

public record CompoundStatement
        (Statement first, Statement second) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) {
        ExecutionStack executionStack = state.executionStack();
        executionStack.push(second);
        executionStack.push(first);

        return null;
    }

    @Override
    public Statement deepCopy() {
        return new CompoundStatement(first.deepCopy(), second.deepCopy());
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws Exception {
        return second.typecheck(first.typecheck(typeEnv));
    }
}
