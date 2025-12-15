package model.statement;

import model.type.Type;
import model.exception.VariableAlreadyDefinedException;
import state.ProgramState;
import state.SymbolTable;
import model.dictionary.MyIDictionary;

public record VariableDeclarationStatement(Type type, String variableName) implements Statement {


    @Override
    public ProgramState execute(ProgramState state) {
        SymbolTable symbolTable = state.symbolTable();
        if (symbolTable.isDefined(variableName)) {
            throw new VariableAlreadyDefinedException();
        }

        symbolTable.declareVariable(type, variableName);
        return null;
    }

    @Override
    public Statement deepCopy() {
        return new VariableDeclarationStatement(type, variableName);
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws Exception {
        typeEnv.add(variableName, type);
        return typeEnv;
    }
}
