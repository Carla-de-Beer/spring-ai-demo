package dev.cadebe.springaidemo.springai.tools;

import dev.cadebe.springaidemo.model.ServiceRequestDto;
import dev.cadebe.springaidemo.repository.entities.ServiceRequest;
import dev.cadebe.springaidemo.service.ServiceRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import dev.cadebe.springaidemo.validator.ServiceRequestValidator;

@Slf4j
@Component
@RequiredArgsConstructor
public class ServiceRequestTool {

    private final ServiceRequestService serviceRequestService;
    private final ServiceRequestValidator validator;

    @Tool(description = "Create a new service request in the system given title, description, category, priority, and requesterName")
    public String createServiceRequest(@ToolParam(description = "Details to create an IT service request") ServiceRequestDto serviceRequest,
                                       ToolContext toolContext) {
        String requester = (String) toolContext.getContext().get("requesterName");

        // Validate input before creating the request
        var errors = validator.validate(serviceRequest);
        if (!errors.isEmpty()) {
            log.warn("Invalid service request payload: {}", errors);
            return "Invalid service request: " + String.join(", ", errors);
        }

        ServiceRequest createdServiceRequest = serviceRequestService.createServiceRequest(serviceRequest, requester);

        log.info("Service request successfully created for: {}", serviceRequest);

        return "Service request created with id: " + createdServiceRequest.getId();
    }
}
