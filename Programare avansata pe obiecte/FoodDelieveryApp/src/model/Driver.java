package model;

import java.util.Date;
import java.util.Random;

public class Driver {
    private String empName;
    private String empEmail;
    private String empPhone;
    private double salary;
    private Car car;
    private String workingDay;
    private int isWorking;

    public Driver(String name, String email, String phone, double sal, Car car) {
        this.empName = name;
        this.empEmail = email;
        this.empPhone = phone;
        this.salary = sal;
        this.car = car;
        this.workingDay = "";
        this.isWorking = 0;
    }

    public int isWorkingToday() {
        Random random = new Random();
        Date now = new Date();
        String snow = now.toString().substring(0, 10);
        if (!workingDay.equals(snow)) {
            workingDay = snow;
            isWorking = random.nextInt(3);
        }
        return isWorking;
    }
}
