package com.jobportal.repository;

import com.jobportal.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByStatusOrderByPostedDateDesc(String status);

    List<Job> findByEmployerIdOrderByPostedDateDesc(Long employerId);

    // Multi-parameter Search and Filter Query
    @Query("SELECT j FROM Job j WHERE " +
           "j.status = 'ACTIVE' AND " +
           "(:keyword IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           " LOWER(j.companyName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           " LOWER(j.skillsRequired) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:category IS NULL OR :category = '' OR LOWER(j.category) = LOWER(:category)) AND " +
           "(:location IS NULL OR :location = '' OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
           "(:experienceLevel IS NULL OR :experienceLevel = '' OR LOWER(j.experienceLevel) = LOWER(:experienceLevel)) " +
           "ORDER BY j.postedDate DESC")
    List<Job> searchAndFilterJobs(
            @Param("keyword") String keyword,
            @Param("category") String category,
            @Param("location") String location,
            @Param("experienceLevel") String experienceLevel
    );

    @Query("SELECT COUNT(j) FROM Job j WHERE j.status = 'ACTIVE'")
    Long countActiveJobs();

    @Query("SELECT j.category, COUNT(j) FROM Job j GROUP BY j.category")
    List<Object[]> countJobsByCategory();
}
