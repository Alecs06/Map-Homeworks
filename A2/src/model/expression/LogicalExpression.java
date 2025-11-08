package model.expression;

import model.exception.InvalidTypeException;
import model.exception.UnknownOperatorException;
import model.value.BooleanValue;
import model.value.Value;
import state.SymbolTable;

public record LogicalExpression(
        Expression leftOperand,
        Expression rightOperand,
        String operator
) implements Expression {
    @Override
    public Value evaluate(SymbolTable symbolTable) {
        Value leftValue = leftOperand.evaluate(symbolTable);
        if (!(leftValue instanceof BooleanValue(boolean leftBool)))
            throw new InvalidTypeException();

        Value rightValue = rightOperand.evaluate(symbolTable);
        if (!(rightValue instanceof BooleanValue(boolean rightBool)))
            throw new InvalidTypeException();

        boolean result = switch (operator) {
            case "&&" -> leftBool && rightBool;
            case "||" -> leftBool || rightBool;
            default -> throw new UnknownOperatorException();
        };

        return new BooleanValue(result);
    }

    @Override
    public Expression deepCopy() {
        return new LogicalExpression(leftOperand.deepCopy(), rightOperand.deepCopy(), operator);
    }
}
