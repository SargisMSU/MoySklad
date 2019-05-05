package model;

import javafx.beans.property.SimpleStringProperty;

public class Product {
    String name, supplierName, uom;
    Double order, turnOver1, stock1, turnOver2, stock2, calcTurnOver1, calcTurnOver2;
    Integer code;
    Boolean active;

    public Product(String name, String supplierName, String code, String uom,
                   String turnOver1, String stock1, String turnOver2, String stock2) {
        this.name = name;
        this.supplierName = supplierName;
        this.code = (!code.equals("") ? Integer.parseInt(code) : 0);
        this.uom = uom;
        this.turnOver1 = (!turnOver1.equals("") ? Double.parseDouble(turnOver1) : 0d);
        this.stock1 = (!stock1.equals("") ? Double.parseDouble(stock1) : 0d);
        this.turnOver2 = (!turnOver2.equals("") ? Double.parseDouble(turnOver2) : 0d);
        this.stock2 = (!stock2.equals("") ? Double.parseDouble(stock2) : 0d);
        this.active = true;
    }

    public String getName() {
        return name;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public Integer getCode() {
        return code;
    }

    public String getUom() {
        return uom;
    }

    public Double getTurnOver1() {
        return turnOver1;
    }

    public Double getOrder() {
        return order;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Double getStock1() {
        return stock1;
    }

    public Double getTurnOver2() {
        return turnOver2;
    }

    public Double getStock2() {
        return stock2;
    }

    public void setOrder(Double order) {
        this.order = order;
    }

    public void setTurnOver1(Double turnOver1) {
        this.turnOver1 = turnOver1;
    }

    public void setStock1(Double stock1) {
        this.stock1 = stock1;
    }

    public void setTurnOver2(Double turnOver2) {
        this.turnOver2 = turnOver2;
    }

    public void setStock2(Double stock2) {
        this.stock2 = stock2;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Double getCalcTurnOver1() {
        return calcTurnOver1;
    }

    public void setCalcTurnOver1(Double calcTurnOver1) {
        this.calcTurnOver1 = calcTurnOver1;
    }

    public Double getCalcTurnOver2() {
        return calcTurnOver2;
    }

    public void setCalcTurnOver2(Double calcTurnOver2) {
        this.calcTurnOver2 = calcTurnOver2;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", supplierName='" + supplierName + '\'' +
                ", uom='" + uom + '\'' +
                ", order=" + order +
                ", turnOver1=" + turnOver1 +
                ", stock1=" + stock1 +
                ", turnOver2=" + turnOver2 +
                ", stock2=" + stock2 +
                ", code=" + code +
                ", active=" + active +
                '}';
    }
}
