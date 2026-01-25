package com.example.demo.controller;

import com.example.demo.entity.Owner;
import com.example.demo.entity.Property;
import com.example.demo.service.OwnerService;
import com.example.demo.service.PropertyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owner/properties")
@CrossOrigin
public class PropertyController {

    private final PropertyService propertyService;
    private final OwnerService ownerService;

    public PropertyController(PropertyService propertyService, OwnerService ownerService) {
        this.propertyService = propertyService;
        this.ownerService = ownerService;
    }

    private Long getLoggedOwnerId() {
        return 1L;
    }

    @PostMapping
    public Property addProperty(@RequestBody Property property) {
        Owner owner = ownerService.getOwner(getLoggedOwnerId());
        return propertyService.addProperty(property, owner);
    }

    @GetMapping
    public List<Property> getProperties() {
        Owner owner = ownerService.getOwner(getLoggedOwnerId());
        return propertyService.getOwnerProperties(owner);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        propertyService.deleteProperty(id);
    }
}
