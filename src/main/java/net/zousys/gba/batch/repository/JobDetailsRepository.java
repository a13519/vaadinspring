package net.zousys.gba.batch.repository;

import net.zousys.gba.batch.entity.JobDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobDetailsRepository extends JpaRepository<JobDetails, Long> {
}
