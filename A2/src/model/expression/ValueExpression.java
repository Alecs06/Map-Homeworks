package model.expression;

import model.value.Value;
import state.Heap;
import state.SymbolTable;

public record ValueExpression(Value value) implements Expression {

    @Override
    public Value evaluate(SymbolTable symbolTable, Heap heap) {
        return value;
    }

    @Override
    public Expression deepCopy() {
        return new ValueExpression(value);
    }
}
