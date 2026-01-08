package controller.admin;

import lombok.RequiredArgsConstructor;
import model.response.AuthenticationResponse;
import model.response.BookingResponse;
import model.response.property.PropertyResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import service.admin.AdminService;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users")
    public Page<AuthenticationResponse.UserResponse> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return adminService.getAllUsers(page, size);
    }

    @PutMapping("/users/{id}/verify")
    public void verifyUser(@PathVariable String id) {
        adminService.verifyUser(id);
    }

    @PutMapping("/users/{id}/suspend")
    public void suspendUser(@PathVariable String id) {
        adminService.suspendUser(id);
    }

    @GetMapping("/properties")
    public Page<PropertyResponse> getAllProperties(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return adminService.getAllProperties(page, size);
    }

    @PutMapping("/properties/{id}/approve")
    public void approveProperty(@PathVariable String id) {
        adminService.approveProperty(id);
    }

    @PutMapping("/properties/{id}/reject")
    public void rejectProperty(@PathVariable String id, @RequestParam String reason) {
        adminService.rejectProperty(id, reason);
    }

    @GetMapping("/bookings")
    public Page<BookingResponse> getAllBookings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return adminService.getAllBookings(page, size);
    }
}
