package model.statement;

import model.expression.Expression;
import model.value.Value;
import state.ProgramState;
import model.dictionary.MyIDictionary;
import model.type.Type;

public record PrintStatement(Expression expression) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) {
        Value value = expression.evaluate(state.symbolTable(), state.heap());
        state.out().append(value);

        return null;
    }

    @Override
    public Statement deepCopy() {
        return new PrintStatement(expression.deepCopy());
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws Exception {
        expression.typecheck(typeEnv);
        return typeEnv;
    }
}
