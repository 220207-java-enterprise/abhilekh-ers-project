package com.revature.app.models;

import java.util.Objects;

public class ReimbursementType {

    private String id;
    private String type;

    public ReimbursementType(){

    }

    public ReimbursementType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReimbursementType that = (ReimbursementType) o;
        return Objects.equals(id, that.id) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type);
    }

    @Override
    public String toString() {
        return "ReimbursementType{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
