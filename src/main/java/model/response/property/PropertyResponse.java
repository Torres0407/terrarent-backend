package model.response.property;
import lombok.*;
import model.entity.Booking;
import model.entity.Property;
import model.response.BookingResponse;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyResponse {
    private String id;
    private String renterId;
    private String propertyId;
    private String status;

    public static PropertyResponse fromEntity(Property property) {
        return new PropertyResponse(
                property.getId(),
                property.getTitle(),
                property.getLocation(),
                property.getStatus()
        );
    }
}