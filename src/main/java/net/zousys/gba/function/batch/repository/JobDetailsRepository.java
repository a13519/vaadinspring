package net.zousys.gba.function.batch.repository;

import net.zousys.gba.function.batch.entity.JobDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobDetailsRepository extends JpaRepository<JobDetails, Long> {

    Page<JobDetails> findByName(String name, Pageable pageable);
    long countByName(String name);
}
