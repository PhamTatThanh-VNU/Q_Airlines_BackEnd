package com.example.qairlines.Repository;

import com.example.qairlines.Model.AirCraft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AircraftRepository extends JpaRepository<AirCraft, Long> {
}
