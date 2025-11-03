package dev.cadebe.springaidemo.model;

/**
 * DTO used when creating a service request. Fields are optional depending on what the prompt/tool provides.
 */
public record ServiceRequestDto(String title,
                                String description,
                                String category,
                                String priority) {
}
