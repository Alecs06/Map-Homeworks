package model.expression;

import model.exception.DivideByZeroException;
import model.exception.InvalidTypeException;
import model.exception.UnknownOperatorException;
import model.value.IntegerValue;
import model.value.Value;
import state.SymbolTable;

public record ArithmeticExpression(Expression leftOperand, Expression rightOperand, char operator) implements Expression {

    @Override
    public Value evaluate(SymbolTable symbolTable) {

        Value leftValue = leftOperand.evaluate(symbolTable);
        if (!(leftValue instanceof IntegerValue(int leftInt)))
            throw new InvalidTypeException();

        Value rightValue = rightOperand.evaluate(symbolTable);
        if (!(rightValue instanceof IntegerValue(int rightInt)))
            throw new InvalidTypeException();

        int result = switch (operator) {
            case '+' -> leftInt + rightInt;
            case '-' -> leftInt - rightInt;
            case '*' -> leftInt * rightInt;
            case '/' -> divide(leftInt, rightInt);
            default -> throw new UnknownOperatorException();
        };

        return new IntegerValue(result);
    }

    @Override
    public Expression deepCopy() {
        return new ArithmeticExpression(leftOperand.deepCopy(), rightOperand.deepCopy(), operator);
    }

    private static int divide(int leftInt, int rightInt) {
        if (rightInt == 0) throw new    DivideByZeroException();
        return leftInt / rightInt;
    }
}
