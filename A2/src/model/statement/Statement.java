package model.statement;

import state.ProgramState;
import model.dictionary.MyIDictionary;
import model.type.Type;

public interface Statement {
    ProgramState execute(ProgramState state);
    Statement deepCopy();

    MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws Exception;
}
