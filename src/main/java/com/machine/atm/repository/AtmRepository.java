package com.machine.atm.repository;

import com.machine.atm.entity.AtmEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AtmRepository extends JpaRepository<AtmEntity, String> {


}
