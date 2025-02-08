package com.ashu.AIBet.Dao;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import com.ashu.AIBet.entity.OverallProfitLoss;

@EnableJpaRepositories
public interface Dao extends JpaRepository<OverallProfitLoss, Integer> {
	@Query("SELECT o FROM OverallProfitLoss o WHERE o.id = :id")
	OverallProfitLoss findWithInitializedFields(@Param("id") Integer id);

}