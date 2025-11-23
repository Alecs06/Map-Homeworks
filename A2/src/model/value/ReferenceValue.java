package model.value;

import model.type.ReferenceType;
import model.type.Type;

public record ReferenceValue(int address, Type locationType) implements Value {

    @Override
    public Type getType() {
        return new ReferenceType(locationType);
    }

    @Override
    public Value deepCopy() {
        return new ReferenceValue(address, locationType);
    }

    @Override
    public boolean equals(Value value) {
        if (value instanceof ReferenceValue refValue) {
            return this.address == refValue.address;
        }
        return false;
    }

    @Override
    public String toString() {
        return "(" + address + ", " + locationType.toString() + ")";
    }

    public int getAddr() {
        return address;
    }
}
