package com.residentia.repository;

import com.residentia.entity.Booking;
import com.residentia.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByProperty(Property property);
    List<Booking> findByPropertyOwnerId(Long ownerId);
    List<Booking> findByPropertyId(Long propertyId);
    List<Booking> findByTenantEmail(String tenantEmail);
}
