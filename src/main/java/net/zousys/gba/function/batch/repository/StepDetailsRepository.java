package net.zousys.gba.function.batch.repository;

import net.zousys.gba.function.batch.entity.StepDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StepDetailsRepository extends JpaRepository<StepDetails, Long> {
}
