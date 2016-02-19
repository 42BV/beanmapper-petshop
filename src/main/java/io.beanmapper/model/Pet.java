package io.beanmapper.model;

import javax.persistence.Entity;

@Entity
public class Pet extends BaseModel {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}