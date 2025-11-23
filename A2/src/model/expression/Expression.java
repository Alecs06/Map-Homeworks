package model.expression;

import model.value.Value;
import state.Heap;
import state.SymbolTable;

public interface Expression {
    Value evaluate(SymbolTable symbolTable, Heap heap);
    Expression deepCopy();
}
