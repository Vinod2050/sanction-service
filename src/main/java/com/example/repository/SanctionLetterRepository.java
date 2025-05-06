package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.SanctionLetter;
import com.example.enums.SanctionStatus;


public interface SanctionLetterRepository extends JpaRepository<SanctionLetter, Integer> {


	List<SanctionLetter> findBySanctionStatus(SanctionStatus sanctionStatus);

}
