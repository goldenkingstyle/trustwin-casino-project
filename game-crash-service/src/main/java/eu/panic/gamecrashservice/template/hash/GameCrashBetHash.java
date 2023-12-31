package eu.panic.gamecrashservice.template.hash;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eu.panic.gamecrashservice.template.dto.UserDto;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash("crash_bets_hash")
@Data
public class GameCrashBetHash {
    @Id
    @Indexed
    @JsonIgnore
    private String username;
    private UserDto user;
    private Long bet;
    @Indexed
    private Boolean isTaken;
    private Double coefficient;
    private Long win;
    private Long timestamp;
}
