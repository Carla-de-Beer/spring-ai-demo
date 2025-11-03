package dev.cadebe.springaidemo.repository;

import dev.cadebe.springaidemo.repository.entities.ServiceRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Long> {

}
