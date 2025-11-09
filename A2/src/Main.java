import controller.Controller;
import model.expression.*;
import model.statement.*;
import model.type.Type;
import model.value.BooleanValue;
import model.value.IntegerValue;
import model.value.StringValue;
import repository.Repository;
import state.*;
import view.TextMenu;
import view.command.ExitCommand;
import view.command.RunExample;

import java.util.ArrayList;
import java.util.List;

void main() {
    Statement ex1 = new CompoundStatement(new VariableDeclarationStatement(Type.INTEGER, "v"),
            new CompoundStatement(new AssignmentStatement("v", new ValueExpression(new IntegerValue(2))),
                    new PrintStatement(new VariableExpression("v")))
    );
    ProgramState prg1 = new ProgramState(new ListExecutionStack(), new MapSymbolTable(), new ListOut(), new MapFileTable());
    prg1.executionStack().push(ex1);
    List<ProgramState> list1 = new ArrayList<>();
    list1.add(prg1);
    Repository repo1 = new Repository(list1,"logFile1.txt");
    Controller ctr1 = new Controller(repo1);

    Statement ex2 = new CompoundStatement(new VariableDeclarationStatement(Type.INTEGER, "a"),
            new CompoundStatement(new VariableDeclarationStatement(Type.INTEGER, "b"),
                    new CompoundStatement(new AssignmentStatement("a", new ArithmeticExpression(new ValueExpression(new IntegerValue(2)), new ArithmeticExpression(new ValueExpression(new IntegerValue(3)), new ValueExpression(new IntegerValue(5)), '*'), '+')),
                            new CompoundStatement(new AssignmentStatement("b", new ArithmeticExpression(new VariableExpression("a"), new ValueExpression(new IntegerValue(1)), '+')),
                                    new PrintStatement(new VariableExpression("b")))))
    );
    ProgramState prg2 = new ProgramState(new ListExecutionStack(), new MapSymbolTable(), new ListOut(), new MapFileTable());
    prg2.executionStack().push(ex2);
    List<ProgramState> list2 = new ArrayList<>();
    list2.add(prg2);
    Repository repo2 = new Repository(list2,"logFile2.txt");
    Controller ctr2 = new Controller(repo2);

    Statement ex3 = new CompoundStatement(new VariableDeclarationStatement(Type.BOOLEAN, "a"),
            new CompoundStatement(new VariableDeclarationStatement(Type.INTEGER, "v"),
                    new CompoundStatement(new AssignmentStatement("a", new ValueExpression(new BooleanValue(true))),
                            new CompoundStatement(new IfStatement(new VariableExpression("a"), new AssignmentStatement("v", new ValueExpression(new IntegerValue(2))), new AssignmentStatement("v", new ValueExpression(new IntegerValue(3)))),
                                    new PrintStatement(new VariableExpression("v")))))
    );
    ProgramState prg3 = new ProgramState(new ListExecutionStack(), new MapSymbolTable(), new ListOut(), new MapFileTable());
    prg3.executionStack().push(ex3);
    List<ProgramState> list3 = new ArrayList<>();
    list3.add(prg3);
    Repository repo3 = new Repository(list3,"logFile3.txt");
    Controller ctr3 = new Controller(repo3);

    Statement fileExample = new CompoundStatement(new VariableDeclarationStatement(Type.STRING, "varf"),
            new CompoundStatement(new AssignmentStatement("varf", new ValueExpression(new StringValue("test.in"))),
                    new CompoundStatement(new OpenRFileStatement(new VariableExpression("varf")),
                            new CompoundStatement(new VariableDeclarationStatement(Type.INTEGER, "varc"),
                                    new CompoundStatement(new ReadFileStatement(new VariableExpression("varf"), "varc"),
                                            new CompoundStatement(new PrintStatement(new VariableExpression("varc")),
                                                    new CompoundStatement(new ReadFileStatement(new VariableExpression("varf"), "varc"),
                                                            new CompoundStatement(new PrintStatement(new VariableExpression("varc")),new CloseRFileStatement(new VariableExpression("varf"))
                                                            )
                                                    )
                                            )
                                    )
                            )
                    )
            )
    );

    ProgramState prg4 = new ProgramState(new ListExecutionStack(), new MapSymbolTable(), new ListOut(), new MapFileTable());
    prg4.executionStack().push(fileExample);
    List<ProgramState> list4 = new ArrayList<>();
    list4.add(prg4);
    Repository repo4 = new Repository(list4,"logFile4.txt");
    Controller ctr4 = new Controller(repo4);

    TextMenu menu = new TextMenu();
    menu.addCommand(new ExitCommand("0", "exit"));
    menu.addCommand(new RunExample("1","Example 1", ctr1));
    menu.addCommand(new RunExample("2","Example 2", ctr2));
    menu.addCommand(new RunExample("3","Example 3", ctr3));
    menu.addCommand(new RunExample("4","File Example", ctr4));
    menu.show();
}