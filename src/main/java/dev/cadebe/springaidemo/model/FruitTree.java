package dev.cadebe.springaidemo.model;

import java.util.List;

public record FruitTree(
    String name,
    List<String> varieties,
    String harvestSeason,
    String notes) {}