package model.request.Booking;
import model.constants.BookingStatus;


public class BookingStatusUpdateRequest {
    private BookingStatus status;

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
}
