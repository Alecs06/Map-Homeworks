package model.statement;

import model.exception.InvalidTypeException;
import model.expression.Expression;
import model.value.BooleanValue;
import model.value.Value;
import state.ProgramState;

public record IfStatement
        (Expression condition, Statement thenStatement, Statement elseStatement) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) {
        Value value = condition.evaluate(state.symbolTable(), state.heap());
        if (!(value instanceof BooleanValue(boolean booleanValue))) {
            throw new InvalidTypeException();
        }

        Statement chosenStatement =
                booleanValue ? thenStatement : elseStatement;

        state.executionStack().push(chosenStatement);

        return null;
    }

    @Override
    public Statement deepCopy() {
        return new IfStatement(condition.deepCopy(), thenStatement.deepCopy(), elseStatement.deepCopy());
    }
}
