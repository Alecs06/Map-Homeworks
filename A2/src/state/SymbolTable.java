package state;

import model.Type;
import model.value.Value;

public interface SymbolTable {
    boolean isDefined(String variableName);

    void declareVariable(Type type, String variableName);

    Type getVariableType(String variableName);

    void updateValue(String variableName, Value value);

    Value getVariableValue(String variableName);

}
