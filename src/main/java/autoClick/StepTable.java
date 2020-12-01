package autoClick;

import javafx.collections.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.*;
import javafx.util.converter.IntegerStringConverter;

public class StepTable {

    static TableView tableView;

    public static TableView getTable() {
        if(tableView == null) {
            tableView = new TableView();

            App.customizeElement(tableView, 360, 220, 20, 190);
            tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            tableView.setPadding(Insets.EMPTY);
            tableView.setEditable(true);

            TableColumn<Step, Type> typeCol = new TableColumn<>("Type");
            typeCol.setCellValueFactory(new PropertyValueFactory<>("stepType"));
            typeCol.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(Type.values())));
            typeCol.setOnEditCommit(event -> {
                event.getRowValue().changeType(event.getNewValue());
                refreshTable();
            });
            Callback<TableColumn<Step, Type>, TableCell<Step, Type>> comboCellFactory = new Callback<>() {
                @Override
                public TableCell<Step, Type> call(final TableColumn<Step, Type> param) {
                    final TableCell<Step, Type> cell = new TableCell<>() {

                        private final ComboBox<Type> comboBox = new ComboBox<>(FXCollections.observableArrayList(Type.values()));

                        {
                            comboBox.setPadding(Insets.EMPTY);
                            comboBox.setOnAction(event
                                    -> {
                                Step step = getTableView().getItems().get(getIndex());
                                step.changeType(comboBox.getSelectionModel().getSelectedItem());
                                refreshTable();
                            });
                        }

                        @Override
                        public void updateItem(Type item, boolean empty) {
                            super.updateItem(item, empty);
                            if(empty) {
                                setGraphic(null);
                            }
                            else {
                                setGraphic(comboBox);
                            }
                        }

                    };
                    return cell;
                }

            };
//            column1.setCellFactory(comboCellFactory);
            typeCol.setPrefWidth(100);

            TableColumn<Step, String> descCOl = new TableColumn<>("Description");
            descCOl.setCellValueFactory(new PropertyValueFactory<>("description"));
            descCOl.setPrefWidth(180);
            descCOl.setEditable(true);

            TableColumn<Step, Integer> waitCol = new TableColumn<>("Wait");
            waitCol.setCellValueFactory(new PropertyValueFactory<>("waitMilisec"));
            waitCol.setPrefWidth(50);
            waitCol.setEditable(true);
            waitCol.setStyle("-fx-alignment: CENTER-RIGHT;");
            waitCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
            waitCol.setOnEditCommit((event) -> {
                event.getRowValue().setWaitMilisec(event.getNewValue());
            });

            TableColumn<Step, Void> listenCol = new TableColumn("");

            Callback<TableColumn<Step, Void>, TableCell<Step, Void>> buttonCellFactory = new Callback<>() {
                @Override
                public TableCell<Step, Void> call(final TableColumn<Step, Void> param) {
                    final TableCell<Step, Void> cell = new TableCell<>() {

                        private final Button btn = new Button("");

                        {

                            btn.setPadding(Insets.EMPTY);
                            App.customizeElement(btn, 20, 20, 0, 0);
                            btn.setOnAction((ActionEvent event) -> {
                                btn.setText("X");
                                Step step = getTableView().getItems().get(getIndex());
                                step.keys.clear();
                            });
                            btn.setOnKeyPressed(event -> {
                                Step step = getTableView().getItems().get(getIndex());
                                switch(step.stepType.get()) {
                                    case HOLD_KEYS:
                                    case RELEASE_KEYS:
                                    case PRESS_KEYS:
                                        if(!step.keys.contains(event.getCode())) {
                                            step.keys.add(event.getCode());
                                        }
                                        break;
                                }
                            }
                            );
                            btn.setOnKeyReleased(event -> {
                                Step step = getTableView().getItems().get(getIndex());
                                switch(step.stepType.get()) {
                                    case HOLD_KEYS:
                                    case RELEASE_KEYS:
                                    case PRESS_KEYS:
                                        App.mainPane.requestFocus();
                                        break;
                                }
                            }
                            );
                            btn.focusedProperty().addListener((obs, oldVal, newVal) -> {
                                if(!newVal) { // quit focus and has button selected
                                    if(getIndex() > -1) {
                                        Step step = getTableView().getItems().get(getIndex());
                                        step.mouseX = (int) Bot.robot.getMouseX();
                                        step.mouseY = (int) Bot.robot.getMouseY();
                                        step.updateDescription();
                                    }
                                    getTableView().refresh();
                                    btn.setText("");
                                }
                            });
                        }

                        @Override
                        public void updateItem(Void item, boolean empty) {
                            super.updateItem(item, empty);
                            if(empty) {
                                setGraphic(null);
                            }
                            else {
                                setGraphic(btn);
                            }
                        }

                    };
                    return cell;
                }

            };

            listenCol.setCellFactory(buttonCellFactory);
            listenCol.setPrefWidth(20);

            tableView.getColumns().addAll(typeCol,
                                          descCOl,
                                          waitCol,
                                          listenCol);

            ObservableList<TableColumn> columns = tableView.getColumns();
            for(TableColumn col : columns) {
                col.setSortable(false);
                col.setResizable(false);
                col.setReorderable(false);
            }
            tableView.getItems().add(new Step(Type.MOVE_LEFT_CLICK, 1000));
            tableView.getItems().add(new Step(Type.PASTE_EMAIL, 1000));
            tableView.getItems().add(new Step(Type.PASTE_CLIP_0, 1000));
        }
        return tableView;
    }

    public static void moveRecordUp() {
        int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
        if(selectedIndex > 0) {
            // swap items
            tableView.getItems().add(selectedIndex - 1, tableView.getItems().remove(selectedIndex));
            // select item at new position
            tableView.getSelectionModel().clearAndSelect(selectedIndex - 1);
        }
    }

    public static void moveRecordDown() {
        int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
        if(selectedIndex > -1 && selectedIndex < tableView.getItems().size() - 1) {
            // swap items
            tableView.getItems().add(selectedIndex + 1, tableView.getItems().remove(selectedIndex));
            // select item at new position
            tableView.getSelectionModel().clearAndSelect(selectedIndex + 1);
        }
    }

    public static void addRecord() {
        int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
        if(selectedIndex > -1) {
            tableView.getItems().add(selectedIndex + 1, new Step(Type.MOVE_LEFT_CLICK, 1000));
            tableView.getSelectionModel().select(selectedIndex + 1);
        }
        else {
            tableView.getItems().add(new Step(Type.MOVE_LEFT_CLICK, 1000));
            tableView.getSelectionModel().clearSelection();
            tableView.getSelectionModel().selectLast();
        }
    }

    public static void removeRecord() {
        int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
        if(selectedIndex > -1) {
            tableView.getItems().remove(selectedIndex);
        }
    }

    public static void refreshTable() {
        tableView.refresh();

    }

}
