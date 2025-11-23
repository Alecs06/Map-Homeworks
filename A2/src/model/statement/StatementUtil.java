package model.statement;
import model.exception.InvalidTypeException;
import model.expression.Expression;
import state.ProgramState;
import model.value.StringValue;


public class StatementUtil {
    public static String evaluateToString(Expression expression, ProgramState state){
        if(!(expression.evaluate(state.symbolTable(),state.heap())instanceof StringValue(String stringValue))){
            throw new InvalidTypeException();
        }
        return stringValue;
    }
}
