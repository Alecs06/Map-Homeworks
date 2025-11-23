package model.type;

import model.value.ReferenceValue;
import model.value.Value;

public record ReferenceType(Type inner) implements Type {

    @Override
    public Value getDefaultValue() {
        return new ReferenceValue(0, inner);
    }

    @Override
    public boolean equals(Object another) {
        if (another instanceof ReferenceType refType) {
            return inner.equals(refType.inner);
        }
        return false;
    }

    @Override
    public String toString() {
        return "Ref(" + inner.toString() + ")";
    }

    public Type getInner() {
        return inner;
    }
}
