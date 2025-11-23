package model.statement;

import model.exception.InvalidTypeException;
import model.exception.VariableNotDefinedException;
import model.expression.Expression;
import model.type.ReferenceType;
import model.value.ReferenceValue;
import model.value.Value;
import state.ProgramState;

public record HeapWriteStatement(String variableName, Expression expression) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) {
        if (!state.symbolTable().isDefined(variableName)) {
            throw new VariableNotDefinedException();
        }

        if (!(state.symbolTable().getVariableType(variableName) instanceof ReferenceType refType)) {
            throw new InvalidTypeException();
        }

        Value varValue = state.symbolTable().getVariableValue(variableName);
        if (!(varValue instanceof ReferenceValue refValue)) {
            throw new InvalidTypeException();
        }

        int address = refValue.getAddr();
        if (!state.heap().isDefined(address)) {
            throw new RuntimeException("Address " + address + " not defined in heap");
        }

        Value value = expression.evaluate(state.symbolTable(), state.heap());

        if (!value.getType().equals(refType.getInner())) {
            throw new InvalidTypeException();
        }

        state.heap().update(address, value);
        return state;
    }

    @Override
    public Statement deepCopy() {
        return new HeapWriteStatement(variableName, expression.deepCopy());
    }

    @Override
    public String toString() {
        return "wH(" + variableName + ", " + expression + ")";
    }
}
