package com.walty.telegram.web.telegram.command;

import com.walty.telegram.service.ParsingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Component
@AllArgsConstructor
public final class CommandResolver {

    private ParsingService parsingService;
    private Command defaultResponseCommand;
    private final Map<CommandType, Command> commands;

    public Command resolve(String input) {
        var commandOptional = parsingService.getCommand(input);
        if (commandOptional.isPresent()) {
            return getCommand(commandOptional.get().toUpperCase()).orElse(defaultResponseCommand);
        }

        return defaultResponseCommand;
    }

    private Optional<Command> getCommand(String command) {
        return Arrays.stream(CommandType.values())
                .filter(commandType -> commandType.getCommandName().toUpperCase().equals(command))
                .map(commands::get)
                .findFirst();
    }
}
