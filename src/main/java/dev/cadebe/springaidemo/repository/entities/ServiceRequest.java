package dev.cadebe.springaidemo.repository.entities;

import dev.cadebe.springaidemo.enums.ServiceRequestCategory;
import dev.cadebe.springaidemo.enums.ServiceRequestPriority;
import dev.cadebe.springaidemo.enums.ServiceRequestStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "service_request")
public class ServiceRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "CATEGORY")
    private ServiceRequestCategory category;

    @Column(name = "STATUS")
    private ServiceRequestStatus status;

    @Column(name = "PRIORITY")
    private ServiceRequestPriority priority;

    @Column(name = "REQUESTER_NAME")
    private String requesterName;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt = LocalDateTime.now();

}
