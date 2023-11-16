package ru.gadzhiev.course_mag.models;

import jakarta.validation.constraints.NotBlank;

public record Department(int id, @NotBlank String name) {
}
