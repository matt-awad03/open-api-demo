package com.matthewa.openapidemo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
@RequestMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
public class EmployeeController {
    private List<Employee> employees = new ArrayList<Employee>();

    @GetMapping(value="/employees")
    public List<Employee> firstPage() {
        return employees;
    }

    @DeleteMapping("/{id}")
    public Employee delete(
        @Parameter(description = "Id of employee to delete", required = true) @PathVariable("id") int id
    ) throws UserNotExistException {
        Employee deletedEmp = null;
        for (Employee emp : employees) {
            if (emp.getEmpId() == id) {
				employees.remove(emp);
				deletedEmp = emp;
				break;
			}
        }
        if (deletedEmp == null) {
            throw new UserNotExistException();
        }
        return deletedEmp;
    }

    @PreAuthorize(value = "ABCD")
    @PostMapping(value="/", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Employee create(@RequestBody Employee user) throws InvalidEmployeeDataException {
        if (user.getName() == "bob") throw new InvalidEmployeeDataException();
		employees.add(user);
		System.out.println(employees);
		return user;
	}

    @GetMapping(value="/employees/{id}")
    public Employee getEmployee(@RequestHeader("id-header-xyz") int empId, @PathVariable int id) throws UserNotExistException {
        return employees.stream().filter(e -> e.getEmpId() == empId).findFirst().orElseThrow(() -> new UserNotExistException());
    }

}
