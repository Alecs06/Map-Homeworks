package model.expression;

import model.value.Value;
import state.Heap;
import state.SymbolTable;
import model.type.Type;
import model.dictionary.MyIDictionary;

public record ValueExpression(Value value) implements Expression {

    @Override
    public Value evaluate(SymbolTable symbolTable, Heap heap) {
        return value;
    }

    @Override
    public Expression deepCopy() {
        return new ValueExpression(value);
    }

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) {
        return value.getType();
    }
}
