package model.expression;

import model.exception.InvalidTypeException;
import model.value.ReferenceValue;
import model.value.Value;
import state.Heap;
import state.SymbolTable;

public record HeapReadExpression(Expression expression) implements Expression {

    @Override
    public Value evaluate(SymbolTable symbolTable, Heap heap) {
        Value value = expression.evaluate(symbolTable, heap);
        
        if (!(value instanceof ReferenceValue refValue)) {
            throw new InvalidTypeException();
        }

        int address = refValue.getAddr();
        if (!heap.isDefined(address)) {
            throw new RuntimeException("Address " + address + " not defined in heap");
        }

        return heap.get(address);
    }

    @Override
    public Expression deepCopy() {
        return new HeapReadExpression(expression.deepCopy());
    }

    @Override
    public String toString() {
        return "rH(" + expression + ")";
    }
}
