package com.jobportal.repository;

import com.jobportal.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByApplicantIdOrderByAppliedDateDesc(Long applicantId);

    List<Application> findByJobIdOrderByAppliedDateDesc(Long jobId);

    List<Application> findByJobEmployerIdOrderByAppliedDateDesc(Long employerId);

    boolean existsByApplicantIdAndJobId(Long applicantId, Long jobId);

    Optional<Application> findByApplicantIdAndJobId(Long applicantId, Long jobId);

    @Query("SELECT COUNT(a) FROM Application a")
    Long countTotalApplications();

    @Query("SELECT COUNT(a) FROM Application a WHERE a.status = :status")
    Long countByStatus(@Param("status") String status);

    @Query("SELECT COUNT(a) FROM Application a WHERE a.job.employer.id = :employerId")
    Long countApplicationsForEmployer(@Param("employerId") Long employerId);

    @Query("SELECT COUNT(a) FROM Application a WHERE a.job.employer.id = :employerId AND a.status = 'SHORTLISTED'")
    Long countShortlistedForEmployer(@Param("employerId") Long employerId);
}
