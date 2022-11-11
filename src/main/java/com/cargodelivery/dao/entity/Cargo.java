package com.cargodelivery.dao.entity;

import java.util.Objects;

public class Cargo {

    private final double length;
    private final double width;
    private final double height;
    private final double weight;

    // Wrap data about cargo from Http request into model-object for better use
    public Cargo(double length, double width, double height, double weight) {
        this.length = length;
        this.width = width;
        this.height = height;
        this.weight = weight;
    }

    public double getLength() {
        return length;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cargo cargo = (Cargo) o;
        return Double.compare(cargo.length, length) == 0 && Double.compare(cargo.width, width) == 0 && Double.compare(cargo.height, height) == 0 && Double.compare(cargo.weight, weight) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(length, width, height, weight);
    }

    @Override
    public String toString() {
        return  String.format("Cargo{length=%.2f, width=%.2f, height=%.2f, weight=%.2f}", length, width, height, weight);
    }
}
