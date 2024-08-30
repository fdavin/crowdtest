package com.example.Crowdsource.repo;

import com.example.Crowdsource.model.Assignment;
import com.example.Crowdsource.model.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestCaseRepo extends JpaRepository<TestCase, Integer> {
}
