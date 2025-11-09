package model.expression;

import model.exception.InvalidTypeException;
import model.exception.UnknownOperatorException;
import model.value.BooleanValue;
import model.value.IntegerValue;
import model.value.Value;
import state.SymbolTable;

public record RelationExpression(Expression leftOperand, Expression rightOperand, String operator) implements Expression {
    
    @Override
    public Value evaluate(SymbolTable symbolTable) {
        Value leftValue = leftOperand.evaluate(symbolTable);
        if (!(leftValue instanceof IntegerValue(int leftInt)))
            throw new InvalidTypeException();

        Value rightValue = rightOperand.evaluate(symbolTable);
        if (!(rightValue instanceof IntegerValue(int rightInt)))
            throw new InvalidTypeException();

        boolean result = switch (operator) {
            case "<" -> leftInt < rightInt;
            case "<=" -> leftInt <= rightInt;
            case "==" -> leftInt == rightInt;
            case "!=" -> leftInt != rightInt;
            case ">" -> leftInt > rightInt;
            case ">=" -> leftInt >= rightInt;
            default -> throw new UnknownOperatorException();
        };

        return new BooleanValue(result);
    }

    @Override
    public Expression deepCopy() {
        return new RelationExpression(leftOperand.deepCopy(), rightOperand.deepCopy(), operator);
    }
}
