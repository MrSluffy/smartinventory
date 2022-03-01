package com.smart.inventory.application.data.entities;

import com.smart.inventory.application.data.AbstractEntity;

import javax.persistence.Entity;

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
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

}
