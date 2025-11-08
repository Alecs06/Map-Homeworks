package model.statement;

import model.type.Type;
import model.exception.VariableAlreadyDefinedException;
import state.ProgramState;
import state.SymbolTable;

public record VariableDeclarationStatement(Type type, String variableName) implements Statement {


    @Override
    public ProgramState execute(ProgramState state) {
        SymbolTable symbolTable = state.symbolTable();
        if (symbolTable.isDefined(variableName)) {
            throw new VariableAlreadyDefinedException();
        }

        symbolTable.declareVariable(type, variableName);
        return state;
    }

    @Override
    public Statement deepCopy() {
        return new VariableDeclarationStatement(type, variableName);
    }
}
    