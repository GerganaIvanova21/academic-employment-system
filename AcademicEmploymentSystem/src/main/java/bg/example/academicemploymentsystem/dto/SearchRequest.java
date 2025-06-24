package bg.example.academicemploymentsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {

    private String query;       // това, което търсим (име, имейл и т.н.)
    private String searchType;  // например "name" или "email"

}
