package model.statement;

import model.exception.FileAlreadyOpenException;
import model.expression.Expression;
import state.ProgramState;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import static model.statement.StatementUtil.evaluateToString;
import model.dictionary.MyIDictionary;
import model.type.Type;
import model.type.SimpleType;

public record OpenRFileStatement(Expression expression) implements Statement {
    @Override
    public ProgramState execute(ProgramState state) {
        String fileName = evaluateToString(expression, state);

        if(state.fileTable().isOpened(fileName))
            throw new FileAlreadyOpenException();

        try {
            var br = new BufferedReader(new FileReader(fileName));
            state.fileTable().add(fileName, br);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public Statement deepCopy() {
        return new OpenRFileStatement(expression.deepCopy());
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws Exception {
        Type t = expression.typecheck(typeEnv);
        if (!t.equals(SimpleType.STRING)) throw new Exception("Open file expression is not a string");
        return typeEnv;
    }
}