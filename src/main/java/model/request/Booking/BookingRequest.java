package model.request.Booking;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingRequest {
    private String propertyId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int numberOfGuests;
    private PaymentInfo paymentInfo;

    public static class PaymentInfo {
        private String method;
        private String token;

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}