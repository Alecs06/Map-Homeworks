package model.expression;

import model.value.Value;
import state.Heap;
import state.SymbolTable;
import model.type.Type;
import model.dictionary.MyIDictionary;
import model.exception.InvalidTypeException;
import model.exception.VariableNotDefinedException;
import model.exception.UnknownOperatorException;

public interface Expression {
    Value evaluate(SymbolTable symbolTable, Heap heap);
    Expression deepCopy();

    // typecheck method: returns the type of the expression in the given type environment
    Type typecheck(MyIDictionary<String, Type> typeEnv) throws Exception;
}
