package model.response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.entity.Booking;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {
    private String id;
    private String renterId;
    private String propertyId;
    private String status;

    public static BookingResponse fromEntity(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getRenter().getId(),
                booking.getProperty().getId(),
                booking.getStatus().name()
        );
    }
}
