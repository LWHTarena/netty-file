package com.lwhtarena.netty.tutorial03.model;

/**
 * @author： liwh
 * @Date: 2016/11/28.
 * @Description：
 */
public class Process {

    private int amount;//量

    private String rate; //进度

    private String speed; //速度

    public Process() {
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "Process{" +
                "amount=" + amount +
                ", rate='" + rate + '\'' +
                ", speed='" + speed + '\'' +
                '}';
    }
}
