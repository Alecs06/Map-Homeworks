//package view;
//
//import controller.Controller;
//import model.type.Type;
//import model.expression.*;
//import model.statement.*;
//import model.value.BooleanValue;
//import model.value.IntegerValue;
//import model.value.StringValue;
//import repository.Repository;
//import state.*;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Scanner;
//
//public class View implements ViewInterface{
//    private final Scanner scanner = new Scanner(System.in);
//
//    @Override
//    public void run() {
//        while (true) {
//            System.out.println("1. Example 1: int v; v=2; Print(v)");
//            System.out.println("2. Example 2: int a; int b; a=2+3*5; b=a+1; Print(b)");
//            System.out.println("3. Example 3: bool a; int v; a=true; (If a Then v=2 Else v=3); Print(v)");
//            System.out.println("0. Exit");
//            System.out.print("Choose: ");
//
//            int choice = scanner.nextInt();
//
//            if (choice == 0) {
//                break;
//            }
//            if (choice >= 1 && choice <= 4) {
//                runProgram(choice);
//            }
//        }
//    }
//
//    private void runProgram(int choice) {
//        Statement program = switch (choice) {
//            case 1 -> example1();
//            case 2 -> example2();
//            case 3 -> example3();
//            case 4 -> fileExample;
//            default -> null;
//        };
//
//        if (program == null) return;
//
//        ExecutionStack stack = new ListExecutionStack();
//        SymbolTable symbolTable = new MapSymbolTable();
//        Out out = new ListOut();
//        FileTable fileTable = new MapFileTable();
//
//        stack.push(program);
//        ProgramState state = new ProgramState(stack, symbolTable, out, fileTable);
//
//        List<ProgramState> states = new ArrayList<>();
//        states.add(state);
//        Repository repo = new Repository(states);
//        Controller controller = new Controller(repo, true);
//
//        try {
//            controller.allSteps();
//            System.out.println("\nFinal state:");
//            controller.displayCurrentState();
//        } catch (Exception e) {
//            System.out.println("Error: " + e.getMessage());
//        }
//    }
//
//    private Statement example1() {
//        return new CompoundStatement(new VariableDeclarationStatement(Type.INTEGER, "v"),
//               new CompoundStatement(new AssignmentStatement("v", new ValueExpression(new IntegerValue(2))),
//               new PrintStatement(new VariableExpression("v")))
//        );
//    }
//
//    private Statement example2() {
//        return new CompoundStatement(new VariableDeclarationStatement(Type.INTEGER, "a"),
//               new CompoundStatement(new VariableDeclarationStatement(Type.INTEGER, "b"),
//               new CompoundStatement(new AssignmentStatement("a", new ArithmeticExpression(new ValueExpression(new IntegerValue(2)), new ArithmeticExpression(new ValueExpression(new IntegerValue(3)), new ValueExpression(new IntegerValue(5)), '*'), '+')),
//               new CompoundStatement(new AssignmentStatement("b", new ArithmeticExpression(new VariableExpression("a"), new ValueExpression(new IntegerValue(1)), '+')),
//               new PrintStatement(new VariableExpression("b")))))
//        );
//    }
//
//    private Statement example3() {
//        return new CompoundStatement(new VariableDeclarationStatement(Type.BOOLEAN, "a"),
//               new CompoundStatement(new VariableDeclarationStatement(Type.INTEGER, "v"),
//               new CompoundStatement(new AssignmentStatement("a", new ValueExpression(new BooleanValue(true))),
//               new CompoundStatement(new IfStatement(new VariableExpression("a"), new AssignmentStatement("v", new ValueExpression(new IntegerValue(2))), new AssignmentStatement("v", new ValueExpression(new IntegerValue(3)))),
//               new PrintStatement(new VariableExpression("v")))))
//        );
//    }
//    Statement fileExample = new CompoundStatement(new VariableDeclarationStatement(Type.STRING, "varf"),
//                            new CompoundStatement(new AssignmentStatement("varf", new ValueExpression(new StringValue("test.in"))),
//                            new CompoundStatement(new OpenRFileStatement(new VariableExpression("varf")),
//                            new CompoundStatement(new VariableDeclarationStatement(Type.INTEGER, "varc"),
//                            new CompoundStatement(new ReadFileStatement(new VariableExpression("varf"), "varc"),
//                            new CompoundStatement(new PrintStatement(new VariableExpression("varc")),
//                            new CompoundStatement(new ReadFileStatement(new VariableExpression("varf"), "varc"),
//                            new CompoundStatement(new PrintStatement(new VariableExpression("varc")),new CloseRFileStatement(new VariableExpression("varf")))))))))
//    );
//}
