import command.CommandService;
import command.context.CommandContext;
import command.context.CommandContextFactory;
import command.context.EmptyCommandContext;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.*;

class CommandContextFactoryTest {

    @Test
    void 정의되지_않은_커맨드_요청인_경우_EmptyCommandContext_반환() throws IOException {
        CommandContext commandContext = CommandContextFactory.getCommandContext(new String[]{"X"});

        assertThat(commandContext.getClass()).isEqualTo(EmptyCommandContext.class);
    }

    @Test
    void EmptyCommand_execute는_어떤_동작도_하지_않는다() throws IOException {
        CommandService.execute(new EmptyCommandContext());
    }
}