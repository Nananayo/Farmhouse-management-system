package com.farmhouseSystem.common;

import com.farmhouseSystem.entity.Employee;
import com.farmhouseSystem.entity.EmployeeAll;
import lombok.Data;

@Data
public class LoginBoy {
    EmployeeAll employeeAll;
    Employee employee;
    String token;
}
