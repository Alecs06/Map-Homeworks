package controller;

import state.ProgramState;

public interface ControllerInterface {
    ProgramState oneStep(ProgramState state) throws Exception;
    void allSteps() throws Exception;
    void displayCurrentState();
    void setDislayFlag(boolean displayFlag);
    boolean getDisplayFlag();

}
