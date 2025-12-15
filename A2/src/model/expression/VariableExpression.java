package model.expression;

import model.exception.VariableNotDefinedException;
import model.value.Value;
import state.Heap;
import state.SymbolTable;
import model.type.Type;
import model.dictionary.MyIDictionary;

public record VariableExpression(String variableName) implements Expression  {

    @Override
    public Value evaluate(SymbolTable symbolTable, Heap heap) {
        if (!symbolTable.isDefined(variableName)) {
            throw new VariableNotDefinedException();
        }
        return symbolTable.getVariableValue(variableName);
    }

    @Override
    public Expression deepCopy() {
        return new VariableExpression(variableName);
    }

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws Exception {
        if (!typeEnv.isDefined(variableName)) {
            throw new VariableNotDefinedException();
        }
        return typeEnv.lookup(variableName);
    }
}
