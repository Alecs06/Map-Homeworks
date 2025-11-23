package model.type;

import model.value.BooleanValue;
import model.value.IntegerValue;
import model.value.StringValue;
import model.value.Value;

public enum SimpleType implements Type {
    INTEGER,
    BOOLEAN,
    STRING;

    @Override
    public Value getDefaultValue() {
        return switch (this) {
            case INTEGER -> new IntegerValue(0);
            case BOOLEAN -> new BooleanValue(false);
            case STRING -> new StringValue("");
        };
    }
}
