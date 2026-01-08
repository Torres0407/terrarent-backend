package repository;
import jakarta.transaction.Transactional;
import model.entity.Property;
import model.constants.PropertyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Long>,
        JpaSpecificationExecutor<Property> {
//    List<Property> findByLandlordId(Long landlordId);
//
//    @Modifying
//    @Transactional
//    @Query("""
//        UPDATE Property p
//        SET p.status = :status, p.adminNote = :reason
//        WHERE p.id = :propertyId
//    """)
//    void updateStatusByAdmin(
//            Long propertyId,
//            PropertyStatus status,
//            String reason
//    );
//
//    @Modifying
//    @Transactional
//    @Query("""
//        UPDATE Property p
//        SET p.status = model.enums.PropertyStatus.UNLISTED_BY_ADMIN
//        WHERE p.landlord.id = :landlordId
//    """)
//    void unlistAllByLandlord(Long landlordId);


    List<Property> findByLandlordId(Long landlordId);

    @Modifying
    @Transactional
    @Query("""
        UPDATE Property p
        SET p.status = :status,
            p.adminNote = :reason
        WHERE p.id = :propertyId
    """)
    void updateStatusByAdmin(
            Long propertyId,
            PropertyStatus status,
            String reason
    );

    @Modifying
    @Transactional
    @Query("""
        UPDATE Property p
        SET p.status = model.enums.PropertyStatus.UNLISTED_BY_ADMIN
        WHERE p.landlordId = :landlordId
    """)
    void unlistAllByLandlord(Long landlordId);
}

