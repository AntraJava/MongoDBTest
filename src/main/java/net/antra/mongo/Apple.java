package net.antra.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by daweizhuang on 4/14/16.
 */
@Document(collection="apple")
public class Apple {
    @Id
    private String id;
    private String color;
    private Double weight;

    public Apple() {
    }

    public Apple(String color) {
        this.color = color;
    }

    public Apple(String color, Double weight) {
        this.color = color;
        this.weight = weight;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getColor() {

        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Apple{" +
                "id='" + id + '\'' +
                ", color='" + color + '\'' +
                ", weight=" + weight +
                '}';
    }
}
