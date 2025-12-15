package model.statement;

import state.ProgramState;
import model.dictionary.MyIDictionary;
import model.type.Type;

public interface Statement {
    ProgramState execute(ProgramState state);
    Statement deepCopy();

    // typecheck method: returns the (possibly updated) type environment after typechecking this statement
    MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws Exception;
}
