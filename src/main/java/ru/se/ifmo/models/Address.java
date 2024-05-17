package ru.se.ifmo.models;

import java.util.Objects;

public class Address {
    private Long id;

    private String zipCode; //Длина строки не должна быть больше 19, поле может быть null

    public Address(String zipCode) {
        this.zipCode = zipCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public boolean isValidZipCode() {
        return zipCode == null || zipCode.length() <= 19;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(id, address.id) && Objects.equals(zipCode, address.zipCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, zipCode);
    }

    @Override
    public String toString() {
        return "Address{" +
               "zipCode='" + zipCode + '\'' +
               '}';
    }
}

