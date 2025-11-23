package model.value;

import model.type.Type;
import model.type.SimpleType;

public record BooleanValue(boolean value) implements Value {

    @Override
    public Type getType() {
        return SimpleType.BOOLEAN;
    }

    @Override
    public Value deepCopy() {
        return new BooleanValue(value);
    }

    @Override
    public boolean equals(Value value) {
        return this.value == ((BooleanValue) value).value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
