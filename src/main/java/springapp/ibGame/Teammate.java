package springapp.ibGame;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * just because i can't in js maps...
 */
@Data
@AllArgsConstructor
@ToString
public class Teammate {
    private String name;
    private String character;
}
