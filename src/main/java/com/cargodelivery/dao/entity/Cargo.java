package com.cargodelivery.dao.entity;

public record Cargo(double length, double width, double height, double weight) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cargo cargo = (Cargo) o;
        return Double.compare(cargo.length, length) == 0 &&
                Double.compare(cargo.width, width) == 0 &&
                Double.compare(cargo.height, height) == 0 &&
                Double.compare(cargo.weight, weight) == 0;
    }

    @Override
    public String toString() {
        return String.format("Cargo{length=%.2f, width=%.2f, height=%.2f, weight=%.2f}", length, width, height, weight);
    }
}
