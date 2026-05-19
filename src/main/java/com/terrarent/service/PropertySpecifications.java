package com.terrarent.service;

import com.terrarent.entity.Property;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class PropertySpecifications {

    public static Specification<Property> hasAddressLike(String address) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("address")), "%" + address.toLowerCase() + "%");
    }

    public static Specification<Property> hasPriceGreaterThanOrEqualTo(BigDecimal minPrice) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("annualPrice"), minPrice);
    }

    public static Specification<Property> hasPriceLessThanOrEqualTo(BigDecimal maxPrice) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("annualPrice"), maxPrice);
    }

    public static Specification<Property> hasBedroomsGreaterThanOrEqualTo(Integer minBedrooms) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("bedrooms"), minBedrooms);
    }

    public static Specification<Property> hasBedroomsLessThanOrEqualTo(Integer maxBedrooms) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("bedrooms"), maxBedrooms);
    }

    public static Specification<Property> hasBathroomsGreaterThanOrEqualTo(Integer minBathrooms) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("bathrooms"), minBathrooms);
    }

    public static Specification<Property> hasBathroomsLessThanOrEqualTo(Integer maxBathrooms) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("bathrooms"), maxBathrooms);
    }

    public static Specification<Property> hasStatus(Property.PropertyStatus status) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), status);
    }
    // Add more specifications as needed for other filters
}
