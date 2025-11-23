package state;

import model.value.Value;
import java.util.HashMap;
import java.util.Map;

public class MapHeap implements Heap {
    private Map<Integer, Value> heap;
    private int nextFreeAddress;

    public MapHeap() {
        this.heap = new HashMap<>();
        this.nextFreeAddress = 1;
    }

    @Override
    public int allocate(Value value) {
        int address = nextFreeAddress++;
        heap.put(address, value);
        return address;
    }

    @Override
    public Value get(int address) {
        return heap.get(address);
    }

    @Override
    public void update(int address, Value value) {
        heap.put(address, value);
    }

    @Override
    public boolean isDefined(int address) {
        return heap.containsKey(address);
    }

    @Override
    public Map<Integer, Value> getContent() {
        return heap;
    }

    @Override
    public void setContent(Map<Integer, Value> newContent) {
        this.heap = newContent;
    }

    @Override
    public String toString() {
        return "Heap: " + heap;
    }
}
