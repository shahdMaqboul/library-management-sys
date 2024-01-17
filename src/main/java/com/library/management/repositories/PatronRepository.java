package com.library.management.repositories;

import com.library.management.model.entities.PatronEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatronRepository extends JpaRepository<PatronEntity, Long> {

}

