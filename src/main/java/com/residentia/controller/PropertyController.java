package com.residentia.controller;

import com.residentia.dto.PropertyDTO;
import com.residentia.entity.Property;
import com.residentia.service.PropertyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/owner")
@Tag(name = "Property Management", description = "APIs for Property CRUD operations")
@CrossOrigin(origins = "*", maxAge = 3600)
@SecurityRequirement(name = "Bearer Authentication")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @PostMapping("/pgs")
    @Operation(summary = "Create a new property", description = "Add a new property/PG listing")
    @ApiResponse(responseCode = "201", description = "Property created successfully")
    @ApiResponse(responseCode = "404", description = "Owner not found")
    public ResponseEntity<?> createProperty(@RequestBody PropertyDTO propertyDTO, HttpServletRequest request) {
        try {
            Long ownerId = (Long) request.getAttribute("ownerId");
            if (ownerId == null) {
                throw new RuntimeException("Owner ID not found in request");
            }
            Property property = propertyService.createProperty(ownerId, propertyDTO);

            PropertyDTO response = new PropertyDTO();
            response.setPropertyId(property.getId());
            response.setOwnerId(property.getOwner().getId());
            response.setPropertyName(property.getPropertyName());
            response.setCity(property.getCity());
            response.setRentAmount(Double.valueOf(property.getRentAmount()));
            response.setStatus(property.getStatus());

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Failed to create property: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/pgs")
    @Operation(summary = "Get all properties of owner", description = "Retrieve all properties owned by logged-in owner")
    @ApiResponse(responseCode = "200", description = "Properties retrieved successfully")
    public ResponseEntity<?> getOwnerProperties(HttpServletRequest request) {
        try {
            Long ownerId = (Long) request.getAttribute("ownerId");
            if (ownerId == null) {
                throw new RuntimeException("Owner ID not found in request");
            }
            List<PropertyDTO> properties = propertyService.getOwnerProperties(ownerId);
            return ResponseEntity.ok(properties);
        } catch (Exception e) {
            log.error("Failed to fetch properties: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/pgs/{propertyId}")
    @Operation(summary = "Get property by ID", description = "Retrieve specific property details")
    @ApiResponse(responseCode = "200", description = "Property retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Property not found")
    public ResponseEntity<?> getPropertyById(@PathVariable Long propertyId) {
        try {
            PropertyDTO property = propertyService.getPropertyById(propertyId);
            return ResponseEntity.ok(property);
        } catch (Exception e) {
            log.error("Failed to fetch property: {}", e.getMessage());
            throw e;
        }
    }

    @PutMapping("/pgs/{propertyId}")
    @Operation(summary = "Update property", description = "Update property/PG listing details")
    @ApiResponse(responseCode = "200", description = "Property updated successfully")
    @ApiResponse(responseCode = "404", description = "Property not found")
    public ResponseEntity<?> updateProperty(@PathVariable Long propertyId, @RequestBody PropertyDTO propertyDTO) {
        try {
            Property property = propertyService.updateProperty(propertyId, propertyDTO);

            PropertyDTO response = new PropertyDTO();
            response.setPropertyId(property.getId());
            response.setOwnerId(property.getOwner().getId());
            response.setPropertyName(property.getPropertyName());
            response.setCity(property.getCity());
            response.setRentAmount(Double.valueOf(property.getRentAmount()));
            response.setStatus(property.getStatus());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to update property: {}", e.getMessage());
            throw e;
        }
    }

    @DeleteMapping("/pgs/{propertyId}")
    @Operation(summary = "Delete property", description = "Delete a property/PG listing")
    @ApiResponse(responseCode = "200", description = "Property deleted successfully")
    @ApiResponse(responseCode = "404", description = "Property not found")
    public ResponseEntity<?> deleteProperty(@PathVariable Long propertyId) {
        try {
            propertyService.deleteProperty(propertyId);
            return ResponseEntity.ok(new MessageResponse("Property deleted successfully"));
        } catch (Exception e) {
            log.error("Failed to delete property: {}", e.getMessage());
            throw e;
        }
    }
}
