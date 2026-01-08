package model.request.property;
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyRequest {
    private String title;
    private String description;
    private Double price;
    private String location;
    private Integer bedrooms;
    private List<String> amenities;
}
