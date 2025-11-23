package controller;

import repository.Repository;
import model.statement.Statement;
import state.ExecutionStack;
import model.value.ReferenceValue;
import model.value.Value;
import repository.Repository;
import model.statement.Statement;
import state.ExecutionStack;
import state.ProgramState;

import java.util.*;
import java.util.stream.Collectors;

public class Controller implements ControllerInterface{
    private final Repository repository;
    private boolean displayFlag;

    public Controller(Repository repository) {
        this.repository = repository;
        this.displayFlag = false;
    }

    public Controller(Repository repository, boolean displayFlag) {
        this.repository = repository;
        this.displayFlag = displayFlag;
    }

    @Override
    public ProgramState oneStep(ProgramState state) throws Exception {
        ExecutionStack executionStack = state.executionStack();
        if (executionStack.isEmpty()) {
            throw new Exception("Execution stack is empty");
        }

        Statement currentStatement = executionStack.pop();
        return currentStatement.execute(state);
    }

    private List<Integer> getAddrFromSymTable(Collection<Value> symTableValues) {
        return symTableValues.stream()
                .filter(v -> v instanceof ReferenceValue)
                .map(v -> {
                    ReferenceValue v1 = (ReferenceValue) v;
                    return v1.getAddr();
                })
                .collect(Collectors.toList());
    }

    private List<Integer> getAddrFromHeap(Collection<Value> heapValues) {
        return heapValues.stream()
                .filter(v -> v instanceof ReferenceValue)
                .map(v -> {
                    ReferenceValue v1 = (ReferenceValue) v;
                    return v1.getAddr();
                })
                .collect(Collectors.toList());
    }

    private Map<Integer, Value> safeGarbageCollector(List<Integer> symTableAddr, Map<Integer, Value> heap) {
        Set<Integer> reachableAddresses = new HashSet<>(symTableAddr);
        boolean changed = true;

        // Find all reachable addresses (including those referenced from heap)
        while (changed) {
            changed = false;
            List<Integer> heapAddresses = getAddrFromHeap(
                    heap.entrySet().stream()
                            .filter(e -> reachableAddresses.contains(e.getKey()))
                            .map(Map.Entry::getValue)
                            .collect(Collectors.toList())
            );

            for (Integer addr : heapAddresses) {
                if (!reachableAddresses.contains(addr)) {
                    reachableAddresses.add(addr);
                    changed = true;
                }
            }
        }

        // Return only reachable entries
        return heap.entrySet().stream()
                .filter(e -> reachableAddresses.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public void allSteps() throws Exception {
        ProgramState programState = repository.getCrtPrg();
        repository.logCrtPrg();
        if(displayFlag){
            displayCurrentState();
        }

        while (!programState.executionStack().isEmpty()) {
            programState = oneStep(programState);
            repository.logCrtPrg();

            programState.heap().setContent(
                    safeGarbageCollector(
                            getAddrFromSymTable(programState.symbolTable().getContent().values()),
                            programState.heap().getContent()
                    )
            );
        }
    }

    @Override
    public void displayCurrentState() {
        System.out.println("Current state:");
        System.out.println(repository.getCrtPrg());
        System.out.println("\n");
    }

    @Override
    public void setDislayFlag(boolean displayFlag) {
        this.displayFlag = displayFlag;
    }

    @Override
    public boolean getDisplayFlag() {
        return displayFlag;
    }
}
