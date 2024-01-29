package org.github.mbmll.example.jdbc;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @Author xlc
 * @Description
 * @Date 2024/1/30 01:32:21
 */
@JsonPropertyOrder({"id", "name", "price", "amount", "type"})
public class Device {
    private String id;
    private String name;
    private String price;
    private String amount;
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Device{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", price='" + price + '\'' +
            ", amount='" + amount + '\'' +
            ", type='" + type + '\'' +
            '}';
    }
}
