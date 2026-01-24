package model;

import exception.InvalidInputException;

public interface Validatable {
    void validate() throws InvalidInputException;
}
