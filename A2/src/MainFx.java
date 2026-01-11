import controller.Controller;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.expression.ValueExpression;
import model.statement.*;
import model.type.SimpleType;
import model.value.IntegerValue;
import model.value.Value;
import repository.Repository;
import state.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainFx extends Application {
    private Controller controller;

    private TextField prgStatesCountField;
    private TableView<Map.Entry<Integer, Value>> heapTableView;
    private ListView<String> outListView;
    private ListView<String> fileTableView;
    private ListView<Integer> prgIdListView;
    private TableView<Map.Entry<String, Value>> symTableView;
    private ListView<String> exeStackListView;

    @Override
    public void start(Stage primaryStage) {
        showSelectionWindow(primaryStage);
    }

    private void showSelectionWindow(Stage stage) {
        VBox layout = new VBox(10);

        ListView<Statement> prgListView = new ListView<>();
        ObservableList<Statement> examples = FXCollections.observableArrayList(getExamples());
        prgListView.setItems(examples);

        Button selectButton = new Button("Select Program");
        selectButton.setOnAction(_ -> {
            Statement selected = prgListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                try {
                    selected.typecheck(new model.dictionary.MyDictionary<>());
                    setupController(selected);
                    showMainWindow();
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Typecheck Error");
                    alert.setHeaderText("The selected program is invalid");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            }
        });

        layout.getChildren().addAll(new Label("Select a program:"), prgListView, selectButton);
        stage.setScene(new Scene(layout, 500, 400));
        stage.setTitle("Program Selection");
        stage.show();
    }

    private void setupController(Statement statement) {
        ProgramState prg = new ProgramState(new ListExecutionStack(), new MapSymbolTable(), new ListOut(), new MapFileTable(), new MapHeap());
        prg.executionStack().push(statement);
        List<ProgramState> list = new ArrayList<>();
        list.add(prg);
        Repository repo = new Repository(list, "logGui.txt");
        controller = new Controller(repo);
    }

    private void showMainWindow() {
        Stage mainStage = new Stage();
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        prgStatesCountField = new TextField();
        prgStatesCountField.setEditable(false);
        grid.add(new Label("Program States:"), 0, 0);
        grid.add(prgStatesCountField, 1, 0);

        heapTableView = new TableView<>();
        TableColumn<Map.Entry<Integer, Value>, Integer> addrCol = new TableColumn<>("Address");
        addrCol.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getKey()).asObject());
        TableColumn<Map.Entry<Integer, Value>, String> valCol = new TableColumn<>("Value");
        valCol.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().toString()));
        heapTableView.getColumns().addAll(addrCol, valCol);
        grid.add(new Label("HeapTable:"), 0, 1);
        grid.add(heapTableView, 0, 2);

        outListView = new ListView<>();
        grid.add(new Label("Out:"), 1, 1);
        grid.add(outListView, 1, 2);

        fileTableView = new ListView<>();
        grid.add(new Label("FileTable:"), 2, 1);
        grid.add(fileTableView, 2, 2);

        prgIdListView = new ListView<>();
        prgIdListView.getSelectionModel().selectedItemProperty().addListener((_, _, _) -> updateStateDetails());
        grid.add(new Label("PrgState IDs:"), 0, 3);
        grid.add(prgIdListView, 0, 4);

        symTableView = new TableView<>();
        TableColumn<Map.Entry<String, Value>, String> varCol = new TableColumn<>("Variable");
        varCol.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getKey()));
        TableColumn<Map.Entry<String, Value>, String> symValCol = new TableColumn<>("Value");
        symValCol.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().toString()));
        symTableView.getColumns().addAll(varCol, symValCol);
        grid.add(new Label("Symbol Table:"), 1, 3);
        grid.add(symTableView, 1, 4);

        exeStackListView = new ListView<>();
        grid.add(new Label("ExeStack:"), 2, 3);
        grid.add(exeStackListView, 2, 4);

        Button runButton = getButton();
        grid.add(runButton, 1, 1);

        updateAll();
        mainStage.setScene(new Scene(grid, 800, 600));
        mainStage.setTitle("Interpreter Execution");
        mainStage.show();
    }

    private Button getButton() {
        Button runButton = new Button("Run One Step");
        runButton.setOnAction(_ -> {
            try {
                List<ProgramState> prgList = controller.removeCompletedPrg(controller.getPrgList());
                if (prgList.isEmpty()) {
                    new Alert(Alert.AlertType.INFORMATION, "Program finished!").showAndWait();
                    return;
                }
                controller.oneStepForAllPrg(prgList);
                updateAll();
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
            }
        });
        return runButton;
    }

    private void updateAll() {
        List<ProgramState> prgs = controller.getPrgList();
        prgStatesCountField.setText(String.valueOf(prgs.size()));

        if (!prgs.isEmpty()) {
            ProgramState first = prgs.getFirst();
            heapTableView.setItems(FXCollections.observableArrayList(new java.util.ArrayList<>(first.heap().getContent().entrySet())));
            heapTableView.refresh();

            outListView.setItems(FXCollections.observableArrayList(first.out().toString()));


            if (first.fileTable() instanceof MapFileTable mapFileTable) {
                fileTableView.setItems(FXCollections.observableArrayList(mapFileTable.getFileNames()));
            } else {
                fileTableView.setItems(FXCollections.observableArrayList(first.fileTable().toString()));
            }
        }

        ObservableList<Integer> ids = FXCollections.observableArrayList(prgs.stream().map(ProgramState::id).collect(Collectors.toList()));
        prgIdListView.setItems(ids);
        if (prgIdListView.getSelectionModel().getSelectedItem() == null && !ids.isEmpty()) {
            prgIdListView.getSelectionModel().select(0);
        }
        updateStateDetails();
    }

    private void updateStateDetails() {
        Integer selectedId = prgIdListView.getSelectionModel().getSelectedItem();
        if (selectedId == null) {
            symTableView.setItems(FXCollections.observableArrayList());
            exeStackListView.setItems(FXCollections.observableArrayList());
            return;
        }

        ProgramState state = controller.getPrgList().stream()
                .filter(p -> p.id() == selectedId)
                .findFirst().orElse(null);

        if (state != null) {
            ObservableList<Map.Entry<String, Value>> symTableData = FXCollections.observableArrayList(
                    state.symbolTable().getContent().entrySet()
            );
            symTableView.setItems(symTableData);
            symTableView.refresh();

            String stackStr = state.executionStack().toString().replace("ExecutionStack: [", "").replace("]", "");
            List<String> stackItems = stackStr.isEmpty() ? List.of() : Arrays.asList(stackStr.split(", "));
            exeStackListView.setItems(FXCollections.observableArrayList(stackItems));
        }
    }

    private List<Statement> getExamples() {
        List<Statement> list = new ArrayList<>();

        // Example 1: int v; v=2; print(v)
        Statement ex1 = new CompoundStatement(new VariableDeclarationStatement(SimpleType.INTEGER, "v"),
                new CompoundStatement(new AssignmentStatement("v", new ValueExpression(new IntegerValue(2))),
                        new PrintStatement(new model.expression.VariableExpression("v")))
        );
        list.add(ex1);

        // Example 2: int a,b; a=2+3*5; b=a+1; print(b)
        Statement ex2 = new CompoundStatement(new VariableDeclarationStatement(SimpleType.INTEGER, "a"),
                new CompoundStatement(new VariableDeclarationStatement(SimpleType.INTEGER, "b"),
                        new CompoundStatement(new AssignmentStatement("a", new model.expression.ArithmeticExpression(new ValueExpression(new IntegerValue(2)), new model.expression.ArithmeticExpression(new ValueExpression(new IntegerValue(3)), new ValueExpression(new IntegerValue(5)), '*'), '+')),
                                new CompoundStatement(new AssignmentStatement("b", new model.expression.ArithmeticExpression(new model.expression.VariableExpression("a"), new ValueExpression(new IntegerValue(1)), '+')),
                                        new PrintStatement(new model.expression.VariableExpression("b")))))
        );
        list.add(ex2);

        // Example 3: bool a; int v; a=true; if(a) v=2 else v=3; print(v)
        Statement ex3 = new CompoundStatement(new VariableDeclarationStatement(SimpleType.BOOLEAN, "a"),
                new CompoundStatement(new VariableDeclarationStatement(SimpleType.INTEGER, "v"),
                        new CompoundStatement(new AssignmentStatement("a", new ValueExpression(new model.value.BooleanValue(true))),
                                new CompoundStatement(new IfStatement(new model.expression.VariableExpression("a"), new AssignmentStatement("v", new ValueExpression(new IntegerValue(2))), new AssignmentStatement("v", new ValueExpression(new IntegerValue(3)))),
                                        new PrintStatement(new model.expression.VariableExpression("v")))))
        );
        list.add(ex3);

        // Example 4: File operations
        Statement fileExample = new CompoundStatement(new VariableDeclarationStatement(SimpleType.STRING, "varf"),
                new CompoundStatement(new AssignmentStatement("varf", new ValueExpression(new model.value.StringValue("test.in"))),
                        new CompoundStatement(new OpenRFileStatement(new model.expression.VariableExpression("varf")),
                                new CompoundStatement(new VariableDeclarationStatement(SimpleType.INTEGER, "varc"),
                                        new CompoundStatement(new ReadFileStatement(new model.expression.VariableExpression("varf"), "varc"),
                                                new CompoundStatement(new PrintStatement(new model.expression.VariableExpression("varc")),
                                                        new CompoundStatement(new ReadFileStatement(new model.expression.VariableExpression("varf"), "varc"),
                                                                new CompoundStatement(new PrintStatement(new model.expression.VariableExpression("varc")), new CloseRFileStatement(new model.expression.VariableExpression("varf")))))))))
        );
        list.add(fileExample);

        // Example 5: Heap - Ref Ref int (garbage collector test)
        Statement heapExample1 = new CompoundStatement(
                new VariableDeclarationStatement(new model.type.ReferenceType(SimpleType.INTEGER), "v"),
                new CompoundStatement(new HeapAllocationStatement("v", new ValueExpression(new IntegerValue(20))),
                        new CompoundStatement(new HeapAllocationStatement("v", new ValueExpression(new IntegerValue(30))),
                                new PrintStatement(new model.expression.HeapReadExpression(new model.expression.VariableExpression("v")))))
        );
        list.add(heapExample1);

        // Example 6: While loop
        Statement whileExample = new CompoundStatement(
                new VariableDeclarationStatement(SimpleType.INTEGER, "v"),
                new CompoundStatement(new AssignmentStatement("v", new ValueExpression(new IntegerValue(4))),
                        new CompoundStatement(new WhileStatement(
                                new model.expression.RelationExpression(new model.expression.VariableExpression("v"), new ValueExpression(new IntegerValue(0)), ">"),
                                new CompoundStatement(
                                        new PrintStatement(new model.expression.VariableExpression("v")),
                                        new AssignmentStatement("v", new model.expression.ArithmeticExpression(new model.expression.VariableExpression("v"), new ValueExpression(new IntegerValue(1)), '-'))
                                )
                        ),
                                new PrintStatement(new model.expression.VariableExpression("v"))))
        );
        list.add(whileExample);

        // Example 7: Fork example
        Statement forkBody = new CompoundStatement(
                new HeapWriteStatement("a", new ValueExpression(new IntegerValue(30))),
                new CompoundStatement(new AssignmentStatement("v", new ValueExpression(new IntegerValue(32))),
                        new CompoundStatement(new PrintStatement(new model.expression.VariableExpression("v")), new PrintStatement(new model.expression.HeapReadExpression(new model.expression.VariableExpression("a")))))
        );
        Statement afterFork = new CompoundStatement(new PrintStatement(new model.expression.VariableExpression("v")), new PrintStatement(new model.expression.HeapReadExpression(new model.expression.VariableExpression("a"))));
        Statement forkExample = new CompoundStatement(
                new VariableDeclarationStatement(SimpleType.INTEGER, "v"),
                new CompoundStatement(new VariableDeclarationStatement(new model.type.ReferenceType(SimpleType.INTEGER), "a"),
                        new CompoundStatement(new AssignmentStatement("v", new ValueExpression(new IntegerValue(10))),
                                new CompoundStatement(new HeapAllocationStatement("a", new ValueExpression(new IntegerValue(22))),
                                        new CompoundStatement(new ForkStatement(forkBody), afterFork)
                                )
                        )
                )
        );
        list.add(forkExample);

        // Example 8: Type error test (int x; x = true)
        Statement ex8 = new CompoundStatement(
                new VariableDeclarationStatement(SimpleType.INTEGER, "x"),
                new AssignmentStatement("x", new ValueExpression(new model.value.BooleanValue(true)))
        );
        list.add(ex8);

        return list;
    }

    static void main(String[] args) {
        launch(args);
    }
}
