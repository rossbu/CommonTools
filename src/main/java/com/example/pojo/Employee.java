package com.example.pojo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Employee {

    String employeeName;
    int employeeID;
    String company;
    int age;


    public Employee() {
        super();
    }

    public Employee(String employeeName, int employeeID, String company,int age) {
        super();
        this.employeeName = employeeName;
        this.employeeID = employeeID;
        this.company = company;
        this.age=age;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((employeeName == null) ? 0 : employeeName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Employee other = (Employee) obj;
        if (employeeName == null) {
            if (other.employeeName != null) {
                return false;
            }
        } else if (!employeeName.equals(other.employeeName)) {
            return false;
        }
        return true;
    }

    public static List<Employee> getEmployees(){
        List<Employee> empList = new ArrayList<Employee>();
        Set<Employee> empSet = new HashSet<Employee>();
        Employee e = new Employee("AAA", 1, "Company1", 24);
        Employee e1 = new Employee("DDD", 1, "Company1", 24);
        Employee e2 = new Employee("AAA", 1, "Company1", 24);
        Employee e3 = new Employee("BBB", 1, "Company1", 36);
        Employee e4 = new Employee("CCC", 1, "Company1", 52);
        Employee e5 = new Employee("CCC", 1, "Company1", 30);
        empList.add(e);
        empList.add(e1);
        empList.add(e2);
        empList.add(e3);
        empList.add(e4);
        return empList;
    }
}