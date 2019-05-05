package controller;

import utils.UtilsExcel;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import javafx.util.StringConverter;
import model.Product;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class ControllerMain {
    public static ObservableList<Product> products = FXCollections.observableArrayList();
    public static String login = "", password = "";
    public static String storeFrom, storeTo;
    public static LocalDate dataOfAnalyse1, dataOfAnalyse2, dataOfPlanning1, dataOfPlanning2;
    public static ObservableList suppliers = FXCollections.observableArrayList();

    @FXML
    TableView tableView;
    @FXML
    TableColumn activeColumn, codeColumn, nameColumn, uomColumn, turnOverColumn1, stockColumn1,
                orderColumn, turnOverColumn2, stockColumn2, supplierColumn;
    @FXML
    ComboBox comboSuppliers;
    @FXML
    DatePicker dataAnalyse1, dataAnalyse2, dataPlanning1, dataPlanning2;

    public ControllerMain(){
    }

    @FXML
    private void initialize() {
        comboSuppliers.getItems().add("Все поставщики");
        comboSuppliers.setValue("Все поставщики");

        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Product> filteredData = new FilteredList<>(products, p -> true);

        comboSuppliers.valueProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(product -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.equals("Все поставщики")) {
                    return true;
                }
                if (product.getSupplierName().equals(newValue)) {
                    return true; // Filter matches supplier name.
                }
                return false; // Does not match.
            });
        });

        tableView.setOnMouseClicked((MouseEvent mouseEvent)->{
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                Node node = ((Node) mouseEvent.getTarget()).getParent();;
                if (node.getId() != null && node.getId().equals("activeColumn")) {
                    TableRow row = null;
                    if (node.getParent() instanceof TableRow) {
                        // clicking on text part
                        row = (TableRow) node.getParent();
                    }
                    if (row != null) {
                        Product product = (Product) row.getItem();
                        product.setActive(!product.getActive());
                        tableView.refresh();
                    }
                }
            }
        });

        tableView.setRowFactory(new StyleRowFactory());

        codeColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("code"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        supplierColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("supplierName"));
        uomColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("uom"));
        turnOverColumn1.setCellValueFactory(new PropertyValueFactory<Product, Double>("turnOver1"));
        turnOverColumn2.setCellValueFactory(new PropertyValueFactory<Product, Double>("turnOver2"));
        stockColumn1.setCellValueFactory(new PropertyValueFactory<Product, Double>("stock1"));
        stockColumn2.setCellValueFactory(new PropertyValueFactory<Product, Double>("stock2"));
        orderColumn.setCellValueFactory(new PropertyValueFactory<Product, Double>("order"));
        orderColumn.setCellFactory(new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn param) {
                return new TextFieldTableCell(new StringConverter<Double>() {
                    @Override
                    public String toString(Double object) {
                        return (object!=null ? object.toString() : "");
                    }
                    @Override
                    public Double fromString(String string) {
                        return Double.parseDouble(string);
                    }
                });
            }
        });

        orderColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Product, Double>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Product, Double> event) {
                double order = event.getNewValue();
                double orderRound = Math.round(order);
                if (order > orderRound)
                    order = orderRound + 1;
                else order = orderRound;
                event.getTableView().getItems().get(event.getTablePosition().getRow()).setOrder(order);
                tableView.refresh();
            }
        });
        activeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Product, Boolean>, SimpleStringProperty>() {
            @Override
            public SimpleStringProperty call(TableColumn.CellDataFeatures<model.Product, Boolean> param) {
                Boolean bool = param.getValue().getActive();
                return (bool ? new SimpleStringProperty("+") : new SimpleStringProperty("-"));
            }
        });
        stockColumn1.setCellFactory(new Callback<TableColumn<Product, Double>, TableCell<Product, Double>>() {
            @Override
            public TableCell<Product, Double> call(TableColumn<Product, Double> param) {
                return new TableCell<Product, Double>(){
                    @Override
                    public void updateItem(Double item, boolean empty) {
                        setStyle("");
                        super.updateItem(item, empty);
                        if (!empty) {
                            Product product = param.getTableView().getItems().get(getIndex());
                            if (product.getTurnOver1() > product.getStock1()) {
                                setStyle("-fx-background-color:#FF5252");
                                setText(item + "");
                            } else {
                                setStyle("");
                                setText(item + "");
                            }
                        }else {
                            setText("");
                        }
                    }
                };
            }
        });
        stockColumn2.setCellFactory(new Callback<TableColumn<Product, Double>, TableCell<Product, Double>>() {
            @Override
            public TableCell<Product, Double> call(TableColumn<Product, Double> param) {
                return new TableCell<Product, Double>(){
                    @Override
                    public void updateItem(Double item, boolean empty) {
                        setStyle("");
                        super.updateItem(item, empty);
                        if (!empty) {
                            Product product = param.getTableView().getItems().get(getIndex());
                            if (product.getTurnOver2() > product.getStock2()) {
                                setStyle("-fx-background-color:#FF5252");
                                setText(item + "");
                            } else {
                                setStyle("");
                                setText(item + "");
                            }
                        } else {
                            setText("");
                        }

                    }
                };
            }
        });
        comboSuppliers.setItems(suppliers);
        tableView.setItems(filteredData);
    }

    @FXML
    private void onDownload() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выбрать файл:");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File file = fileChooser.showOpenDialog(tableView.getScene().getWindow());
        ArrayList<Product> products = new ArrayList<>();
        HashSet<String> set = new HashSet<>();
        UtilsExcel.getProductsFromFile(file, products, set);
        this.suppliers.clear();
        this.suppliers.add("Все поставщики");
        this.suppliers.addAll(set);
        this.products.clear();
        this.products.addAll(products);
    }

    @FXML
    private void onCalcOrder(){
        dataOfAnalyse1 = dataAnalyse1.getValue();
        dataOfAnalyse2 = dataAnalyse2.getValue();
        dataOfPlanning1 = dataPlanning1.getValue();
        dataOfPlanning2 = dataPlanning2.getValue();
        if (dataOfAnalyse1 == null || dataOfAnalyse2 == null || dataOfPlanning2 == null || dataOfPlanning1 == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("");
            alert.setHeaderText("");
            String s = "Период анализа и период планирования должны быть заполнены.";
            alert.setContentText(s);
            alert.show();
        }else if (products.get(0).getOrder() != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("");
            alert.setHeaderText("Продолжить?");
            String s = "Поле «К заказу» будет перезаполнено.";
            alert.setContentText(s);
            Optional<ButtonType> result = alert.showAndWait();
            if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                calculateOrder();
            }
        }else {
            calculateOrder();
        }
        tableView.refresh();
    }

    @FXML
    private void onDeficit(){
        if (products.get(0).getOrder() != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("");
            alert.setHeaderText("Продолжить?");
            String s = "Поле «К заказу» будет перезаполнено.";
            alert.setContentText(s);
            Optional<ButtonType> result = alert.showAndWait();
            if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                calculateDeficit();
            }
        }else {
            calculateDeficit();
        }
        tableView.refresh();
    }

    @FXML
    private void onCreateFile(){
        try {
            if (products.get(0).getOrder() == null){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("");
                alert.setHeaderText("");
                String s = "Поле 'К заказу' пустое.";
                alert.setContentText(s);
                alert.show();
            }else {
                UtilsExcel.createFile(products, (comboSuppliers.getValue() != null ? comboSuppliers.getValue().toString() : ""),
                        dataOfPlanning1.getDayOfMonth() + "_" + dataOfPlanning1.getMonthValue() + "___" +
                                dataOfPlanning2.getDayOfMonth() + "_" + dataOfPlanning2.getMonthValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void calculateOrder(){
        Period periodAnalyse = Period.between(dataOfAnalyse1, dataOfAnalyse2);
        Period periodOfPlanning = Period.between(dataOfPlanning1, dataOfPlanning2);
        long periodAnalyseInDays = ChronoUnit.DAYS.between(dataOfAnalyse1, dataOfAnalyse2);
        long periodPlanningInDays = ChronoUnit.DAYS.between(dataOfPlanning1, dataOfPlanning2);
        if (periodPlanningInDays > periodAnalyseInDays){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("");
            alert.setHeaderText("");
            String s = "Период планирования не может быть больше периода анализа.";
            alert.setContentText(s);
            alert.show();
        }else {
            for (int i = 0; i < products.size(); i++) {
                Product product = products.get(i);
                double koef = periodAnalyseInDays / periodPlanningInDays;
                product.setCalcTurnOver1(product.getTurnOver1() / koef);
                product.setCalcTurnOver2(product.getTurnOver2() / koef);
            }
            for (int i = 0; i < products.size(); i++) {
                Product product = products.get(i);
                if (product.getStock1() - product.getCalcTurnOver1() > 0) {
                    product.setOrder(0d);
                } else {
                    if (product.getStock2() > product.getCalcTurnOver1() - product.getStock1()) {
                        product.setOrder(product.getCalcTurnOver1() - product.getStock1());
                    } else {
                        product.setOrder(product.getStock2() <= 0 ? 0d : product.getStock2());
                    }
                }
                double order = product.getOrder();
                double orderRound = Math.round(order);
                if (order > orderRound)
                    order = orderRound + 1;
                else order = orderRound;
                product.setOrder(order);
            }
        }
    }

    private static void calculateDeficit() {
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            product.setOrder(product.getTurnOver1() + product.getTurnOver2() -
                    product.getStock1() - product.getStock2());;
            if (product.getOrder() > 0) {
                product.setActive(true);
            } else {
                product.setActive(false);
            }
            double order = product.getOrder();
            double orderRound = Math.round(order);
            if (order > orderRound)
                order = orderRound + 1;
            else order = orderRound;
            product.setOrder(order);
        }
    }
}
