package state;

import model.type.Type;
import model.value.Value;

import java.util.HashMap;
import java.util.Map;

public class MapSymbolTable implements SymbolTable {
    private final Map<String, Value> symbolTable = new HashMap<>();

    public MapSymbolTable() {}

    // copy constructor used by deepCopy
    private MapSymbolTable(Map<String, Value> content) {
        this.symbolTable.putAll(content);
    }

    @Override
    public boolean isDefined(String variableName) {
        return symbolTable.containsKey(variableName);
    }

    @Override
    public void declareVariable(Type type, String variableName) {
        symbolTable.put(variableName, type.getDefaultValue());
    }

    @Override
    public Type getVariableType(String variableName) {
        return symbolTable.get(variableName).getType();
    }

    @Override
    public void updateValue(String variableName, Value value) {
        symbolTable.put(variableName, value);
    }

    @Override
    public Value getVariableValue(String variableName) {
        return symbolTable.get(variableName);
    }

    @Override
    public Map<String, Value> getContent() {
        return symbolTable;
    }

    @Override
    public SymbolTable deepCopy() {
        return new MapSymbolTable(new HashMap<>(this.symbolTable));
    }

    @Override
    public String toString() {
        return "SymbolTable: " + symbolTable;
    }
}
