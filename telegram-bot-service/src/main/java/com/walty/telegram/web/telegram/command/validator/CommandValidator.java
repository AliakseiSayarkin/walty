package com.walty.telegram.web.telegram.command.validator;

import java.util.Optional;

public interface CommandValidator {

    Optional<String> isValid(String input);
}
