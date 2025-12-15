package controller;

import repository.Repository;
import state.ProgramState;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import model.statement.Statement;
import state.ExecutionStack;
import model.value.ReferenceValue;
import model.value.Value;

public class Controller implements ControllerInterface{
    private final Repository repository;
    private boolean displayFlag;
    private ExecutorService executor;

    public Controller(Repository repository) {
        this.repository = repository;
        this.displayFlag = false;
    }

    //public Controller(Repository repository, boolean displayFlag) {
    //    this.repository = repository;
    //    this.displayFlag = displayFlag;
    //}

    @Override
    public ProgramState oneStep(ProgramState state) throws Exception {
        return state.oneStep();
    }

        //private List<Integer> getAddrFromSymTable(Collection<Value> symTableValues) {
        //    return symTableValues.stream().filter(v -> v instanceof ReferenceValue).map(v -> {
        //                ReferenceValue v1 = (ReferenceValue) v;
        //                return v1.getAddr();
        //            }).collect(Collectors.toList());
        //}

    private List<Integer> getAddrFromHeap(Collection<Value> heapValues) {
        return heapValues.stream().filter(v -> v instanceof ReferenceValue).map(v -> {
                    ReferenceValue v1 = (ReferenceValue) v;
                    return v1.getAddr();
                }).collect(Collectors.toList());
    }

    private Map<Integer, Value> safeGarbageCollector(List<Integer> symTableAddr, Map<Integer, Value> heap) {
        Set<Integer> reachableAddresses = new HashSet<>(symTableAddr);
        boolean changed = true;
        while (changed) {
            changed = false;
            List<Integer> heapAddresses = getAddrFromHeap(
                    heap.entrySet().stream().filter(e -> reachableAddresses.contains(e.getKey()))
                    .map(Map.Entry::getValue).collect(Collectors.toList())
            );
            for (Integer addr : heapAddresses) {
                if (!reachableAddresses.contains(addr)) {
                    reachableAddresses.add(addr);
                    changed = true;
                }
            }
        }
        return heap.entrySet().stream()
                .filter(e -> reachableAddresses.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public void allSteps() throws Exception {
        executor = Executors.newFixedThreadPool(2);

        List<ProgramState> prgList = removeCompletedPrg(repository.getPrgList());

        while (!prgList.isEmpty()) {
            oneStepForAllPrg(prgList);

            List<Integer> allSymTableAddr = prgList.stream()
                    .flatMap(p -> p.symbolTable().getContent().values().stream())
                    .filter(v -> v instanceof ReferenceValue)
                    .map(v -> ((ReferenceValue) v).getAddr()).collect(Collectors.toList());

            if (!prgList.isEmpty()) {
                prgList.getFirst().heap().setContent(safeGarbageCollector(allSymTableAddr, prgList.getFirst().heap().getContent())
                );
            }
            prgList = removeCompletedPrg(repository.getPrgList());
        }
        executor.shutdownNow();
        repository.setPrgList(prgList);
    }

    public List<ProgramState> removeCompletedPrg(List<ProgramState> inPrgList) {
        return inPrgList.stream().filter(ProgramState::isNotCompleted).collect(Collectors.toList());
    }

    public void oneStepForAllPrg(List<ProgramState> prgList) throws InterruptedException {
        prgList.forEach(prg -> {
            try {
                repository.logPrgStateExec(prg);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        List<Callable<ProgramState>> callList = prgList.stream().map(
                (ProgramState p) -> (Callable<ProgramState>)(p::oneStep)).collect(Collectors.toList()
        );

        List<ProgramState> newPrgList = executor.invokeAll(callList).stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .filter(Objects::nonNull)
                .toList();

        prgList.addAll(newPrgList);


        prgList.forEach(prg -> {
            try {
                repository.logPrgStateExec(prg);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        repository.setPrgList(prgList);
    }

    @Override
    public void displayCurrentState() {
        System.out.println("Current state:");
        repository.getPrgList().forEach(System.out::println);
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
