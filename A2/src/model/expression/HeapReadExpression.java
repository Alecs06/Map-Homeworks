package model.expression;

import model.exception.InvalidTypeException;
import model.value.ReferenceValue;
import model.value.Value;
import state.Heap;
import state.SymbolTable;
import model.type.Type;
import model.type.ReferenceType;
import model.dictionary.MyIDictionary;

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

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws Exception {
        Type typ = expression.typecheck(typeEnv);
        if (typ instanceof ReferenceType refType) {
            return refType.getInner();
        }
        throw new InvalidTypeException();
    }
}
