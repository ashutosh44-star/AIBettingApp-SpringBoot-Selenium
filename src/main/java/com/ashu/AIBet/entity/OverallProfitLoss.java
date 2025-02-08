package com.ashu.AIBet.entity;

import org.springframework.stereotype.Component;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@Entity
@Component
public class OverallProfitLoss {
	@Id
    private int id;
    private Double TotalMoney;
    private Double OverAllProfitMoney;

    // Default no-argument constructor for JPA
    public OverallProfitLoss() {}

    // Parameterized constructor for manual instantiation
    public OverallProfitLoss(int id, Double TotalMoney, Double OverAllProfitMoney) {
        this.id = id;
        this.TotalMoney = TotalMoney;
        this.OverAllProfitMoney = OverAllProfitMoney;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getTotalMoney() {
        return TotalMoney;
    }

    public void setTotalMoney(Double totalMoney) {
        TotalMoney = totalMoney;
    }

    public Double getOverAllProfitMoney() {
        return OverAllProfitMoney;
    }

    public void setOverAllProfitMoney(Double overAllProfitMoney) {
        OverAllProfitMoney = overAllProfitMoney;
    }
}
