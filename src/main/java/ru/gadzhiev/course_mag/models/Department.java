package ru.gadzhiev.course_mag.models;

import jakarta.validation.constraints.Size;

public record Department(int id, @Size(min = 1, max = 50) String name) { }
