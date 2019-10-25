package com.concurrent.chapter2;

/**
 * @author : hujing
 * @date : 2019/10/25
 */
public class TaxCalculate {

    private final double bonus;
    private final double salary;
    private final TaxCalculateStrategy taxCalculateStrategy;

    public TaxCalculate(double bonus, double salary, TaxCalculateStrategy taxCalculateStrategy) {
        this.bonus = bonus;
        this.salary = salary;
        this.taxCalculateStrategy = taxCalculateStrategy;
    }

    protected double getTax() {
        return taxCalculateStrategy.calculate(salary, bonus);
    }
}
