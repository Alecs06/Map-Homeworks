package controller;

import repository.Repository;
import model.statement.Statement;
import state.ExecutionStack;
import state.ProgramState;

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

    @Override
    public void allSteps() throws Exception {
        ProgramState programState =repository.getCrtPrg() ;

        if(displayFlag){
            displayCurrentState();
        }

        while (!programState.executionStack().isEmpty()) {
            programState = oneStep(programState);
            if(displayFlag){
                displayCurrentState();
            }
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
