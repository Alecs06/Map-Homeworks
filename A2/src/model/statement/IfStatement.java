package model.statement;

import model.exception.InvalidTypeException;
import model.expression.Expression;
import model.value.BooleanValue;
import model.value.Value;
import state.ProgramState;
import model.dictionary.MyIDictionary;
import model.type.Type;
import model.type.SimpleType;

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

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws Exception {
        Type typexp = condition.typecheck(typeEnv);
        if (!typexp.equals(SimpleType.BOOLEAN)) {
            throw new InvalidTypeException();
        }
        thenStatement.typecheck(typeEnv.deepCopy());
        elseStatement.typecheck(typeEnv.deepCopy());
        return typeEnv;
    }
}
