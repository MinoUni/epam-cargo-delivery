package com.cargodelivery.dao.entity;

/**
 * Model that represent MySQL cargo table
 * and easier to use
 * Contains 2 constructors:
 *  1. Wrap all parameters received from request
 *     with model-object;
 *  2. Build a model-object from data from database
 *
 */
public class Cargo {

    private int id;

    private final double length;

    private final double width;

    private final double height;

    private final double weight;

    // Wrap data about cargo from database cargo table into 1 model-object
    public Cargo(int id, double length, double width, double height, double weight) {
        this.id = id;
        this.length = length;
        this.width = width;
        this.height = height;
        this.weight = weight;
    }

    // Wrap data about cargo from Http request into model-object for better use
    public Cargo(double length, double width, double height, double weight) {
        this.length = length;
        this.width = width;
        this.height = height;
        this.weight = weight;
    }

    public int getId() {
        return id;
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

}
