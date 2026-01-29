package com.residentia.service;

import com.residentia.dto.PropertyDTO;
import com.residentia.entity.Owner;
import com.residentia.entity.Property;
import com.residentia.exception.ResourceNotFoundException;
import com.residentia.repository.OwnerRepository;
import com.residentia.repository.PropertyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    public Property createProperty(Long ownerId, PropertyDTO propertyDTO) {
        log.info("Creating property for owner: {}", ownerId);

        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found with id: " + ownerId));

        Property property = new Property();
        property.setOwner(owner);
        property.setPropertyName(propertyDTO.getPropertyName());
        property.setAddress(propertyDTO.getAddress());
        property.setCity(propertyDTO.getCity());
        property.setState(propertyDTO.getState());
        property.setPincode(propertyDTO.getPincode());
        property.setRentAmount(propertyDTO.getRentAmount().intValue());
        property.setSharingType(propertyDTO.getSharingType());
        property.setMaxCapacity(propertyDTO.getMaxCapacity());
        property.setAvailableBeds(propertyDTO.getAvailableBeds());
        property.setFoodIncluded(propertyDTO.getFoodIncluded());
        property.setDescription(propertyDTO.getDescription());
        property.setStatus(propertyDTO.getStatus() != null ? propertyDTO.getStatus() : "ACTIVE");
        property.setAmenities(propertyDTO.getAmenities());
        property.setImageUrl(propertyDTO.getImageUrl());

        return propertyRepository.save(property);
    }

    public Property updateProperty(Long propertyId, PropertyDTO propertyDTO) {
        log.info("Updating property: {}", propertyId);

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + propertyId));

        if (propertyDTO.getPropertyName() != null) property.setPropertyName(propertyDTO.getPropertyName());
        if (propertyDTO.getAddress() != null) property.setAddress(propertyDTO.getAddress());
        if (propertyDTO.getCity() != null) property.setCity(propertyDTO.getCity());
        if (propertyDTO.getState() != null) property.setState(propertyDTO.getState());
        if (propertyDTO.getPincode() != null) property.setPincode(propertyDTO.getPincode());
        if (propertyDTO.getRentAmount() != null) property.setRentAmount(propertyDTO.getRentAmount().intValue());
        if (propertyDTO.getSharingType() != null) property.setSharingType(propertyDTO.getSharingType());
        if (propertyDTO.getMaxCapacity() != null) property.setMaxCapacity(propertyDTO.getMaxCapacity());
        if (propertyDTO.getAvailableBeds() != null) property.setAvailableBeds(propertyDTO.getAvailableBeds());
        if (propertyDTO.getFoodIncluded() != null) property.setFoodIncluded(propertyDTO.getFoodIncluded());
        if (propertyDTO.getDescription() != null) property.setDescription(propertyDTO.getDescription());
        if (propertyDTO.getStatus() != null) property.setStatus(propertyDTO.getStatus());
        if (propertyDTO.getAmenities() != null) property.setAmenities(propertyDTO.getAmenities());
        if (propertyDTO.getImageUrl() != null) property.setImageUrl(propertyDTO.getImageUrl());

        return propertyRepository.save(property);
    }

    public PropertyDTO getPropertyById(Long propertyId) {
        log.info("Fetching property: {}", propertyId);

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + propertyId));

        return convertToDTO(property);
    }

    public List<PropertyDTO> getOwnerProperties(Long ownerId) {
        log.info("Fetching properties for owner: {}", ownerId);

        List<Property> properties = propertyRepository.findByOwnerId(ownerId);
        return properties.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public void deleteProperty(Long propertyId) {
        log.info("Deleting property: {}", propertyId);

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + propertyId));

        propertyRepository.delete(property);
    }

    private PropertyDTO convertToDTO(Property property) {
        PropertyDTO dto = new PropertyDTO();
        dto.setPropertyId(property.getId());
        
        // Handle null owner - refresh the property to ensure owner is loaded
        if (property.getOwner() == null) {
            log.warn("Owner is null for property: {}. Refreshing from database.", property.getId());
            property = propertyRepository.findById(property.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Property not found"));
        }
        
        dto.setOwnerId(property.getOwner().getId());
        dto.setPropertyName(property.getPropertyName());
        dto.setAddress(property.getAddress());
        dto.setCity(property.getCity());
        dto.setState(property.getState());
        dto.setPincode(property.getPincode());
        dto.setRentAmount(Double.valueOf(property.getRentAmount()));
        dto.setSharingType(property.getSharingType());
        dto.setMaxCapacity(property.getMaxCapacity());
        dto.setAvailableBeds(property.getAvailableBeds());
        dto.setFoodIncluded(property.getFoodIncluded());
        dto.setDescription(property.getDescription());
        dto.setStatus(property.getStatus());
        dto.setMapLink(property.getMapLink());
        dto.setAmenities(property.getAmenities());
        dto.setImageUrl(property.getImageUrl());
        return dto;
    }
}
