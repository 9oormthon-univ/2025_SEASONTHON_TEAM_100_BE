package com.example.nextvalue.repository;

import com.example.nextvalue.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    public Optional<Country> findByCode(String code);
}
