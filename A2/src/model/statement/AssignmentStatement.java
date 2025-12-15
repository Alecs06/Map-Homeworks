package model.statement;

import model.exception.InvalidTypeException;
import model.exception.VariableNotDefinedException;
import model.expression.Expression;
import model.value.Value;
import state.ProgramState;
import state.SymbolTable;
import model.dictionary.MyIDictionary;
import model.type.Type;

public record AssignmentStatement
        (String variableName, Expression expression) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) {
        SymbolTable symbolTable = state.symbolTable();
        if (!symbolTable.isDefined(variableName)) {
            throw new VariableNotDefinedException();
        }

        Value value = expression.evaluate(symbolTable, state.heap());
        if (value.getType() != symbolTable.getVariableType(variableName)) {
            throw new InvalidTypeException();
        }

        symbolTable.updateValue(variableName, value);
        return null;
    }

    @Override
    public Statement deepCopy() {
        return new AssignmentStatement(variableName, expression.deepCopy());
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws Exception {
        Type typevar = typeEnv.lookup(variableName);
        Type typexp = expression.typecheck(typeEnv);
        if (typevar == null) throw new VariableNotDefinedException();
        if (!typevar.equals(typexp))
            throw new InvalidTypeException();
        return typeEnv;
    }
}
