package eu.panic.authservice.template.payload;

import eu.panic.authservice.template.enums.Gender;

public record ChangePersonalDataRequest(
        String birthday,
        Gender gender
) {
}
