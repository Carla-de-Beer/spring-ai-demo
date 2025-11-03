package dev.cadebe.springaidemo.validator;

import dev.cadebe.springaidemo.model.ServiceRequestDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ServiceRequestValidator {

    public List<String> validate(ServiceRequestDto dto) {
        List<String> errors = new ArrayList<>();

        if (dto == null) {
            errors.add("serviceRequest is null");
            return errors;
        }

        // At minimum we require a description (the model should always provide one)
        if (dto.description() == null || dto.description().isBlank()) {
            errors.add("description is required");
        }

        // Validate category if provided (allow OTHER by default)
        if (dto.category() != null && dto.category().isBlank()) {
            errors.add("category, if provided, must not be blank");
        }

        // Validate priority if provided
        if (dto.priority() != null && dto.priority().isBlank()) {
            errors.add("priority, if provided, must not be blank");
        }

        return errors;
    }
}
