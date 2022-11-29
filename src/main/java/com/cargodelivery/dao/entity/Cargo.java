package com.cargodelivery.dao.entity;

public class Cargo {

	private double length;
	private double width;
	private double height;
	private double weight;
	
    public Cargo(double length, double width, double height, double weight) {
		this.length = length;
		this.width = width;
		this.height = height;
		this.weight = weight;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

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
