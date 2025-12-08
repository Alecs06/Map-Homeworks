package model.statement;

import model.exception.InvalidTypeException;
import model.expression.Expression;
import model.value.BooleanValue;
import model.value.Value;
import state.ProgramState;

public record WhileStatement(Expression condition, Statement statement) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) {
        Value value = condition.evaluate(state.symbolTable(), state.heap());

        if (!(value instanceof BooleanValue(boolean boolValue))) {
            throw new InvalidTypeException();
        }

        if (boolValue) {
            state.executionStack().push(this);
            state.executionStack().push(statement);
        }
        return null;
    }

    @Override
    public Statement deepCopy() {
        return new WhileStatement(condition.deepCopy(), statement.deepCopy());
    }

    @Override
    public String toString() {
        return "while(" + condition + ") " + statement;
    }
}
