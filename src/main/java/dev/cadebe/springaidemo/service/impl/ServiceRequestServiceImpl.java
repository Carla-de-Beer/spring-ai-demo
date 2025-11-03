package dev.cadebe.springaidemo.service.impl;

import dev.cadebe.springaidemo.enums.ServiceRequestCategory;
import dev.cadebe.springaidemo.enums.ServiceRequestPriority;
import dev.cadebe.springaidemo.enums.ServiceRequestStatus;
import dev.cadebe.springaidemo.model.ServiceRequestDto;
import dev.cadebe.springaidemo.repository.ServiceRequestRepository;
import dev.cadebe.springaidemo.repository.entities.ServiceRequest;
import dev.cadebe.springaidemo.service.ServiceRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServiceRequestServiceImpl implements ServiceRequestService {

    private final ServiceRequestRepository serviceRequestRepository;

    public ServiceRequest createServiceRequest(ServiceRequestDto request, String requesterName) {
        ServiceRequest.ServiceRequestBuilder builder = ServiceRequest.builder()
                .description(request.description())
                .status(ServiceRequestStatus.valueOf(request.toString().toUpperCase()))
                .requesterName(requesterName);

        if (request.title() != null) {
            builder.title(request.title());
        }

        if (request.priority() != null) {
            builder.priority(ServiceRequestPriority.valueOf(request.priority().toUpperCase()));
        }

        if (request.category() != null) {
            try {
                builder.category(ServiceRequestCategory.valueOf(request.category().toUpperCase()));
            } catch (IllegalArgumentException ex) {
                // ignore unknown category and leave null
            }
        }

        ServiceRequest serviceRequest = builder.build();

        return serviceRequestRepository.save(serviceRequest);
    }

}
