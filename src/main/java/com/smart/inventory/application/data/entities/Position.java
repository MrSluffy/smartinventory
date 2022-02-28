package com.smart.inventory.application.data.entities;

import com.smart.inventory.application.data.AbstractEntity;

import javax.persistence.Entity;
import java.util.Objects;

@Entity
public class Position extends AbstractEntity {

    public String postionName;

    public Position(){
    }

    public Position(String postionName){
        this.postionName = postionName;
    }

    public String getPostionName(){
        return postionName;
    }

    public void setPostionName(String postionName){
        this.postionName = postionName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Position position = (Position) o;
        return Objects.equals(postionName, position.postionName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), postionName);
    }
}
