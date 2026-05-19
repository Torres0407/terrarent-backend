package com.terrarent.repository;

import com.terrarent.entity.Report;
import com.terrarent.entity.Report.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReportRepository extends JpaRepository<Report, UUID> {
    List<Report> findByStatus(ReportStatus status);
    long countByStatus(ReportStatus status);
}
