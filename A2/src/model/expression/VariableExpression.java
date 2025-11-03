package model.expression;

import model.exception.VariableNotDefinedException;
import model.value.Value;
import state.SymbolTable;

public record VariableExpression(
        String variableName) implements Expression  {

    @Override
    public Value evaluate(SymbolTable symbolTable) {
        if (!symbolTable.isDefined(variableName)) {
            throw new VariableNotDefinedException();
        }
        return symbolTable.getVariableValue(variableName);
    }
}
