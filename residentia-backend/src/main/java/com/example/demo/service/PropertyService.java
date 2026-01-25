package com.example.demo.service;

import com.example.demo.entity.Owner;
import com.example.demo.entity.Property;
import com.example.demo.repository.PropertyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;

    public PropertyService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    public Property addProperty(Property property, Owner owner) {
        property.setOwner(owner);
        return propertyRepository.save(property);
    }

    public List<Property> getOwnerProperties(Owner owner) {
        return propertyRepository.findByOwner(owner);
    }

    public void deleteProperty(Long id) {
        propertyRepository.deleteById(id);
    }
    
    public Property getById(Long id) {
        return propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));
    }

}
