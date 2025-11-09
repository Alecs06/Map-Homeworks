package model.value;

import model.type.Type;

public record StringValue(String value) implements Value {

    @Override
    public Type getType() {
        return Type.STRING;
    }

    @Override
    public Value deepCopy() {
        return new StringValue(value);
    }

    @Override
    public boolean equals(Value value) {
        return this.value.equals(((StringValue) value).value);
    }
}
