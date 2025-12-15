package model.expression;

import model.exception.InvalidTypeException;
import model.exception.UnknownOperatorException;
import model.value.BooleanValue;
import model.value.Value;
import state.Heap;
import state.SymbolTable;
import model.type.Type;
import model.type.SimpleType;
import model.dictionary.MyIDictionary;

public record LogicalExpression(
        Expression leftOperand,
        Expression rightOperand,
        String operator
) implements Expression {
    @Override
    public Value evaluate(SymbolTable symbolTable, Heap heap) {
        Value leftValue = leftOperand.evaluate(symbolTable, heap);
        if (!(leftValue instanceof BooleanValue(boolean leftBool)))
            throw new InvalidTypeException();

        Value rightValue = rightOperand.evaluate(symbolTable, heap);
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

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws Exception {
        Type typ1 = leftOperand.typecheck(typeEnv);
        Type typ2 = rightOperand.typecheck(typeEnv);
        if (!typ1.equals(SimpleType.BOOLEAN))
            throw new InvalidTypeException();
        if (!typ2.equals(SimpleType.BOOLEAN))
            throw new InvalidTypeException();
        return SimpleType.BOOLEAN;
    }
}
