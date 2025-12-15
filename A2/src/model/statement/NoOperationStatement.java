package model.statement;

import state.ProgramState;
import model.dictionary.MyIDictionary;
import model.type.Type;

public class NoOperationStatement implements Statement {
    @Override
    public ProgramState execute(ProgramState state) {
        return null;
    }

    @Override
    public Statement deepCopy() {
        return new NoOperationStatement();
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) {
        return typeEnv;
    }
}
