package com.kcumendigital.democratic.parse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dario Chamorro on 22/10/2015.
 */
public class SunshineQuery {

    String orderAscending;
    String orderDescending;
    List<FieldValue> fieldValues;
    List<FieldValue> pointerValues;
    List<FieldValue> users;
    Integer limit;

    public SunshineQuery() {
    }

    protected String getOrderAscending() {
        return orderAscending;
    }

    public void setOrderAscending(String orderAscending) {
        this.orderAscending = orderAscending;
    }

    protected String getOrderDescending() {
        return orderDescending;
    }

    public void setOrderDescending(String orderDescending) {
        this.orderDescending = orderDescending;
    }

    public void addFieldValue(String field, String value) {
        if(fieldValues == null)
            fieldValues = new ArrayList<>();
        FieldValue fV =  new FieldValue(field, value);
        fieldValues.add(fV);
    }


    protected List<FieldValue> getFieldValues() {
        return fieldValues;
    }

    protected List<FieldValue> getPointerValues() {
        return pointerValues;
    }

    public void addPointerValue(String field, String id) {
        if(pointerValues == null)
            pointerValues = new ArrayList<>();
        FieldValue fV =  new FieldValue(field, id);
        pointerValues.add(fV);
    }


    protected List<FieldValue> getUsers() {
        return users;
    }

    public void addUser(String userfield, String id) {
        if(users== null)
            users= new ArrayList<>();
        FieldValue fV =  new FieldValue(userfield, id);
        users.add(fV);
    }

    protected Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    protected class FieldValue{
        String field;
        Object value;

        public FieldValue(String field, Object value) {
            this.field = field;
            this.value = value;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }

}
