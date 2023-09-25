package com.matthewa.openapidemo;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Employee {
    private int empId;

    @Size(min = 4, max = 100)
    @NotBlank
    private String name;
    private String designation;

    @Max(1_000_000)
    @NotNull
    private java.math.BigDecimal salary;
}
