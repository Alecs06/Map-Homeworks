import controller.Controller;
import model.expression.*;
import model.statement.*;
import model.type.ReferenceType;
import model.type.SimpleType;
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
import model.dictionary.MyDictionary;
import model.dictionary.MyIDictionary;
import model.type.Type;

void main() {
    TextMenu menu = new TextMenu();
    menu.addCommand(new ExitCommand("0", "exit"));

    Statement ex1 = new CompoundStatement(new VariableDeclarationStatement(SimpleType.INTEGER, "v"),
                    new CompoundStatement(new AssignmentStatement("v", new ValueExpression(new IntegerValue(2))),
                    new PrintStatement(new VariableExpression("v")))
    );
    try {
        MyIDictionary<String, Type> env1 = new MyDictionary<>();
        ex1.typecheck(env1);
        ProgramState prg1 = new ProgramState(new ListExecutionStack(), new MapSymbolTable(), new ListOut(), new MapFileTable(),new MapHeap());
        prg1.executionStack().push(ex1);
        List<ProgramState> list1 = new ArrayList<>();
        list1.add(prg1);
        Repository repo1 = new Repository(list1,"logFile1.txt");
        Controller ctr1 = new Controller(repo1);
        menu.addCommand(new RunExample("1","Example 1: int v; v=2; print(v)", ctr1));
    } catch (Exception e) {
        System.out.println("Typecheck error in example 1: " + e.getMessage());
    }

    // Example 2
    Statement ex2 = new CompoundStatement(new VariableDeclarationStatement(SimpleType.INTEGER, "a"),
                    new CompoundStatement(new VariableDeclarationStatement(SimpleType.INTEGER, "b"),
                    new CompoundStatement(new AssignmentStatement("a", new ArithmeticExpression(new ValueExpression(new IntegerValue(2)), new ArithmeticExpression(new ValueExpression(new IntegerValue(3)), new ValueExpression(new IntegerValue(5)), '*'), '+')),
                    new CompoundStatement(new AssignmentStatement("b", new ArithmeticExpression(new VariableExpression("a"), new ValueExpression(new IntegerValue(1)), '+')),
                    new PrintStatement(new VariableExpression("b")))))
    );
    try {
        MyIDictionary<String, Type> env2 = new MyDictionary<>();
        ex2.typecheck(env2);
        ProgramState prg2 = new ProgramState(new ListExecutionStack(), new MapSymbolTable(), new ListOut(), new MapFileTable(),new MapHeap());
        prg2.executionStack().push(ex2);
        List<ProgramState> list2 = new ArrayList<>();
        list2.add(prg2);
        Repository repo2 = new Repository(list2,"logFile2.txt");
        Controller ctr2 = new Controller(repo2);
        menu.addCommand(new RunExample("2","Example 2: int a,b; a=2+3*5; b=a+1; print(b)", ctr2));
    } catch (Exception e) {
        System.out.println("Typecheck error in example 2: " + e.getMessage());
    }

    // Example 3
    Statement ex3 = new CompoundStatement(new VariableDeclarationStatement(SimpleType.BOOLEAN, "a"),
                    new CompoundStatement(new VariableDeclarationStatement(SimpleType.INTEGER, "v"),
                    new CompoundStatement(new AssignmentStatement("a", new ValueExpression(new BooleanValue(true))),
                    new CompoundStatement(new IfStatement(new VariableExpression("a"), new AssignmentStatement("v", new ValueExpression(new IntegerValue(2))), new AssignmentStatement("v", new ValueExpression(new IntegerValue(3)))),
                    new PrintStatement(new VariableExpression("v")))))
    );
    try {
        MyIDictionary<String, Type> env3 = new MyDictionary<>();
        ex3.typecheck(env3);
        ProgramState prg3 = new ProgramState(new ListExecutionStack(), new MapSymbolTable(), new ListOut(), new MapFileTable(),new MapHeap());
        prg3.executionStack().push(ex3);
        List<ProgramState> list3 = new ArrayList<>();
        list3.add(prg3);
        Repository repo3 = new Repository(list3,"logFile3.txt");
        Controller ctr3 = new Controller(repo3);
        menu.addCommand(new RunExample("3","Example 3: bool a; int v; a=true; if(a) v=2 else v=3; print(v)", ctr3));
    } catch (Exception e) {
        System.out.println("Typecheck error in example 3: " + e.getMessage());
    }

    // Example 4 (file operations)
    Statement fileExample = new CompoundStatement(new VariableDeclarationStatement(SimpleType.STRING, "varf"),
                            new CompoundStatement(new AssignmentStatement("varf", new ValueExpression(new StringValue("test.in"))),
                            new CompoundStatement(new OpenRFileStatement(new VariableExpression("varf")),
                            new CompoundStatement(new VariableDeclarationStatement(SimpleType.INTEGER, "varc"),
                            new CompoundStatement(new ReadFileStatement(new VariableExpression("varf"), "varc"),
                            new CompoundStatement(new PrintStatement(new VariableExpression("varc")),
                            new CompoundStatement(new ReadFileStatement(new VariableExpression("varf"), "varc"),
                            new CompoundStatement(new PrintStatement(new VariableExpression("varc")),new CloseRFileStatement(new VariableExpression("varf")))))))))
    );
    try {
        MyIDictionary<String, Type> env4 = new MyDictionary<>();
        fileExample.typecheck(env4);
        ProgramState prg4 = new ProgramState(new ListExecutionStack(), new MapSymbolTable(), new ListOut(), new MapFileTable(),new MapHeap());
        prg4.executionStack().push(fileExample);
        List<ProgramState> list4 = new ArrayList<>();
        list4.add(prg4);
        Repository repo4 = new Repository(list4,"logFile4.txt");
        Controller ctr4 = new Controller(repo4);
        menu.addCommand(new RunExample("4","Example 4: File operations", ctr4));
    } catch (Exception e) {
        System.out.println("Typecheck error in example 4: " + e.getMessage());
    }

    // Example 5: Heap - Ref Ref int (garbage collector test)
    Statement heapExample1 = new CompoundStatement(
            new VariableDeclarationStatement(new ReferenceType(SimpleType.INTEGER), "v"),
            new CompoundStatement(new HeapAllocationStatement("v", new ValueExpression(new IntegerValue(20))),
            new CompoundStatement(new HeapAllocationStatement("v", new ValueExpression(new IntegerValue(30))),
            new PrintStatement(new HeapReadExpression(new VariableExpression("v")))))
    );

    try {
        MyIDictionary<String, Type> env5 = new MyDictionary<>();
        heapExample1.typecheck(env5);
        ProgramState prg5 = new ProgramState(new ListExecutionStack(), new MapSymbolTable(), new ListOut(), new MapFileTable(), new MapHeap());
        prg5.executionStack().push(heapExample1);
        List<ProgramState> list5 = new ArrayList<>();
        list5.add(prg5);
        Repository repo5 = new Repository(list5,"logFile5.txt");
        Controller ctr5 = new Controller(repo5);
        menu.addCommand(new RunExample("5","Example 5: Heap - Ref Ref int (garbage collector test)", ctr5));
    } catch (Exception e) {
        System.out.println("Typecheck error in example 5: " + e.getMessage());
    }

    // Example 6: While loop
    Statement whileExample = new CompoundStatement(
            new VariableDeclarationStatement(SimpleType.INTEGER, "v"),
            new CompoundStatement(new AssignmentStatement("v", new ValueExpression(new IntegerValue(4))),
            new CompoundStatement(new WhileStatement(
                    new RelationExpression(new VariableExpression("v"), new ValueExpression(new IntegerValue(0)), ">"),
                    new CompoundStatement(
                            new PrintStatement(new VariableExpression("v")),
                            new AssignmentStatement("v", new ArithmeticExpression(new VariableExpression("v"), new ValueExpression(new IntegerValue(1)), '-'))
                    )
            ),
            new PrintStatement(new VariableExpression("v"))))
    );

    try {
        MyIDictionary<String, Type> env6 = new MyDictionary<>();
        whileExample.typecheck(env6);
        ProgramState prg6 = new ProgramState(new ListExecutionStack(), new MapSymbolTable(), new ListOut(), new MapFileTable(), new MapHeap());
        prg6.executionStack().push(whileExample);
        List<ProgramState> list6 = new ArrayList<>();
        list6.add(prg6);
        Repository repo6 = new Repository(list6,"logFile6.txt");
        Controller ctr6 = new Controller(repo6);
        menu.addCommand(new RunExample("6","Example 6: While loop", ctr6));
    } catch (Exception e) {
        System.out.println("Typecheck error in example 6: " + e.getMessage());
    }

    // Example 7: Fork example
    Statement forkBody = new CompoundStatement(
            new HeapWriteStatement("a", new ValueExpression(new IntegerValue(30))),
            new CompoundStatement(new AssignmentStatement("v", new ValueExpression(new IntegerValue(32))),
            new CompoundStatement(new PrintStatement(new VariableExpression("v")), new PrintStatement(new HeapReadExpression(new VariableExpression("a")))))
    );

    Statement afterFork = new CompoundStatement(new PrintStatement(new VariableExpression("v")), new PrintStatement(new HeapReadExpression(new VariableExpression("a"))));

    Statement forkExample = new CompoundStatement(
            new VariableDeclarationStatement(SimpleType.INTEGER, "v"),
            new CompoundStatement(new VariableDeclarationStatement(new ReferenceType(SimpleType.INTEGER), "a"),
            new CompoundStatement(new AssignmentStatement("v", new ValueExpression(new IntegerValue(10))),
            new CompoundStatement(new HeapAllocationStatement("a", new ValueExpression(new IntegerValue(22))),
            new CompoundStatement(new ForkStatement(forkBody), afterFork)
                            )
                    )
            )
    );

    try {
        MyIDictionary<String, Type> env7 = new MyDictionary<>();
        forkExample.typecheck(env7);
        ProgramState prg7 = new ProgramState(new ListExecutionStack(), new MapSymbolTable(), new ListOut(), new MapFileTable(), new MapHeap());
        prg7.executionStack().push(forkExample);
        List<ProgramState> list7 = new ArrayList<>();
        list7.add(prg7);
        Repository repo7 = new Repository(list7,"logFile7.txt");
        Controller ctr7 = new Controller(repo7);
        menu.addCommand(new RunExample("7","Example 7: Fork example", ctr7));
    } catch (Exception e) {
        System.out.println("Typecheck error in example 7: " + e.getMessage());
    }

    Statement ex8 = new CompoundStatement(
            new VariableDeclarationStatement(SimpleType.INTEGER, "x"),
            new AssignmentStatement("x", new ValueExpression(new BooleanValue(true)))
    );
    try {
        MyIDictionary<String, Type> env8 = new MyDictionary<>();
        ex8.typecheck(env8); // expected to throw
        ProgramState prg8 = new ProgramState(new ListExecutionStack(), new MapSymbolTable(), new ListOut(), new MapFileTable(), new MapHeap());
        prg8.executionStack().push(ex8);
        List<ProgramState> list8 = new ArrayList<>();
        list8.add(prg8);
        Repository repo8 = new Repository(list8,"logFile8.txt");
        Controller ctr8 = new Controller(repo8);
        menu.addCommand(new RunExample("8","Example 8: Type error test (should not be added)", ctr8));
    } catch (Exception e) {
        System.out.println("Typecheck error in example 8 (expected): " + e.getMessage());
    }

    menu.show();
}