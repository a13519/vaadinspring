package net.zousys.gba.batch.repository;

import net.zousys.gba.batch.entity.StepDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StepDetailsRepository extends JpaRepository<StepDetails, Long> {
}
