package controller;

import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import model.Product;

class StyleRowFactory implements Callback<TableView<Product>, TableRow<Product>> {
    @Override
    public TableRow<Product> call(TableView<Product> tableView) {
        TableRow<Product> row =  new TableRow<Product>() {
            @Override
            protected void updateItem(Product item, boolean empty ) {
                // Сначала обязательно сбрасываем стиль.
                setStyle("");
                // и только после этого вызываем метод super.updateItem
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else {
                    Boolean currentBool = item.getActive();
                    if (currentBool) {
                        setStyle("");
                    } else {
                        setStyle("-fx-background-color:#A0A0A0");
                    }
                }
            }
        };
        return row;
    }
}