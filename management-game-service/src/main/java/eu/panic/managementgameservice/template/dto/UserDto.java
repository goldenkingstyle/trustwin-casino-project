package eu.panic.managementgameservice.template.dto;
import eu.panic.managementgameservice.template.enums.AuthorizeType;
import eu.panic.managementgameservice.template.enums.Gender;
import eu.panic.managementgameservice.template.enums.Rank;
import eu.panic.managementgameservice.template.enums.Role;
import lombok.*;
import org.springframework.data.relational.core.mapping.Column;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private Data data;
    private PersonalData personalData;
    private RefData refData;
    private Role role;
    private Long balance;
    private Boolean isMultiAccount;
    private Boolean isAccountNonLocked;
    private String ipAddress;
    private Long registeredAt;
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PersonalData{
        private String nickname;
        private String birthday;
        private Gender gender;
    }
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Data{
        private AuthorizeType authorizeType;
        private String serverSeed;
        private String clientSeed;
        private Rank rank;
    }
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RefData{
        @Column("invited")
        private Long invited;
        @Column("earned")
        private Long earned;
        @Column("level")
        private Integer level;
        @Column("ref_link")
        private String refLink;
        @Column("invited_by")
        private String invitedBy;
    }
}
