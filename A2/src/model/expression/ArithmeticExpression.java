package model.expression;

import model.exception.DivideByZeroException;
import model.exception.InvalidTypeException;
import model.exception.UnknownOperatorException;
import model.value.IntegerValue;
import model.value.Value;
import state.Heap;
import state.SymbolTable;
import model.type.Type;
import model.type.SimpleType;
import model.dictionary.MyIDictionary;

public record ArithmeticExpression(Expression leftOperand, Expression rightOperand, char operator) implements Expression {

    @Override
    public Value evaluate(SymbolTable symbolTable, Heap heap) {

        Value leftValue = leftOperand.evaluate(symbolTable, heap);
        if (!(leftValue instanceof IntegerValue(int leftInt)))
            throw new InvalidTypeException();

        Value rightValue = rightOperand.evaluate(symbolTable, heap);
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
        if (rightInt == 0) throw new DivideByZeroException();
        return leftInt / rightInt;
    }

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws Exception {
        Type typ1 = leftOperand.typecheck(typeEnv);
        Type typ2 = rightOperand.typecheck(typeEnv);
        if (!typ1.equals(SimpleType.INTEGER))
            throw new InvalidTypeException();
        if (!typ2.equals(SimpleType.INTEGER))
            throw new InvalidTypeException();
        return SimpleType.INTEGER;
    }
}
