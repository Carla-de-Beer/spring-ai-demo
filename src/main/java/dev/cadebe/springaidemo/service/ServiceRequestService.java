package dev.cadebe.springaidemo.service;

import dev.cadebe.springaidemo.model.ServiceRequestDto;
import dev.cadebe.springaidemo.repository.entities.ServiceRequest;

public interface ServiceRequestService {

    ServiceRequest createServiceRequest(ServiceRequestDto request, String username);
}
