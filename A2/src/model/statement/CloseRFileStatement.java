package model.statement;

import model.exception.FileNotOpenException;
import model.expression.Expression;
import state.ProgramState;
import model.dictionary.MyIDictionary;
import model.type.Type;
import model.type.SimpleType;

public record CloseRFileStatement(Expression expression) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) {
        String fileName = StatementUtil.evaluateToString(expression, state);
        if (!state.fileTable().isOpened(fileName)) {
            throw new FileNotOpenException();
        }
        state.fileTable().close(fileName);
        return null;
    }

    @Override
    public Statement deepCopy() {
        return new CloseRFileStatement(expression.deepCopy());
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws Exception {
        Type t = expression.typecheck(typeEnv);
        if (!t.equals(SimpleType.STRING)) throw new Exception("Close file expression is not a string");
        return typeEnv;
    }
}
