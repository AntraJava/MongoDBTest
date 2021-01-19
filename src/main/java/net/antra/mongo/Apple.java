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
    private Integer weight;

    public Apple() {
    }

    public Apple(String color) {
        this.color = color;
    }

    public Apple(String color, Integer weight) {
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

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
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
