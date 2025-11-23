package model.value;

import model.type.Type;
import model.type.SimpleType;

public record IntegerValue(int value) implements Value {

    @Override
    public Type getType() {
        return SimpleType.INTEGER;
    }

    @Override
    public Value deepCopy() {
       return new IntegerValue(value);
    }

    @Override
    public boolean equals(Value value) {
        return this.value == ((IntegerValue) value).value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
