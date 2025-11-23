package model.statement;

import model.exception.InvalidTypeException;
import model.exception.VariableNotDefinedException;
import model.expression.Expression;
import model.type.ReferenceType;
import model.value.ReferenceValue;
import model.value.Value;
import state.ProgramState;

public record HeapAllocationStatement(String variableName, Expression expression) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) {
        if (!state.symbolTable().isDefined(variableName)) {
            throw new VariableNotDefinedException();
        }

        if (!(state.symbolTable().getVariableType(variableName) instanceof ReferenceType refType)) {
            throw new InvalidTypeException();
        }

        Value value = expression.evaluate(state.symbolTable(), state.heap());

        if (!value.getType().equals(refType.getInner())) {
            throw new InvalidTypeException();
        }

        int newAddress = state.heap().allocate(value);

        ReferenceValue newRefValue = new ReferenceValue(newAddress, refType.getInner());
        state.symbolTable().updateValue(variableName, newRefValue);

        return state;
    }

    @Override
    public Statement deepCopy() {
        return new HeapAllocationStatement(variableName, expression.deepCopy());
    }

    @Override
    public String toString() {
        return "new(" + variableName + ", " + expression + ")";
    }
}
