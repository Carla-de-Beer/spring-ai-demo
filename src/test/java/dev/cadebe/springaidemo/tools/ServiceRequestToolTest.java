package dev.cadebe.springaidemo.tools;

import dev.cadebe.springaidemo.model.ServiceRequestDto;
import dev.cadebe.springaidemo.repository.entities.ServiceRequest;
import dev.cadebe.springaidemo.service.ServiceRequestService;
import dev.cadebe.springaidemo.springai.tools.ServiceRequestTool;
import dev.cadebe.springaidemo.validator.ServiceRequestValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.model.ToolContext;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static dev.cadebe.springaidemo.enums.ServiceRequestCategory.HARDWARE;
import static dev.cadebe.springaidemo.enums.ServiceRequestPriority.HIGH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceRequestToolTest {

    @Mock
    ServiceRequestService serviceRequestService;

    @Mock
    ServiceRequestValidator validator;

    @InjectMocks
    ServiceRequestTool serviceRequestTool;

    @Test
    void createServiceRequest_success() {
        ServiceRequestDto dto = new ServiceRequestDto("Laptop issue", "My laptop screen is cracked", HARDWARE.name(), HIGH.name());

        ToolContext toolContext = mock(ToolContext.class);
        when(toolContext.getContext()).thenReturn(Map.of("requesterName", "alice"));

        when(validator.validate(dto)).thenReturn(Collections.emptyList());

        ServiceRequest saved = new ServiceRequest();
        saved.setId(42L);
        when(serviceRequestService.createServiceRequest(dto, "alice")).thenReturn(saved);

        String result = serviceRequestTool.createServiceRequest(dto, toolContext);

        assertThat(result).contains("Service request created with id: 42");
        verify(serviceRequestService, times(1)).createServiceRequest(dto, "alice");
    }

    @Test
    void createServiceRequest_validationFails() {
        ServiceRequestDto dto = new ServiceRequestDto(null, "", null, null);

        ToolContext toolContext = mock(ToolContext.class);
        when(toolContext.getContext()).thenReturn(Map.of("requesterName", "alice"));

        when(validator.validate(dto)).thenReturn(List.of("description is required"));

        String result = serviceRequestTool.createServiceRequest(dto, toolContext);

        assertThat(result).contains("Invalid service request");
        verifyNoInteractions(serviceRequestService);
    }
}
