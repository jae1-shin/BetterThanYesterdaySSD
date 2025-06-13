package command.console;

import command.common.Command;
import command.common.CommandResult;
import command.common.ConsoleService;

public class HelpCommand extends Command {

    public static final int EXPECTED_ARGUMENT_COUNT = 1;

    public HelpCommand(ConsoleService service) {
        super(service);
    }

    public String argumentsValidCheck(String[] args) {
        if(isInValidArgumentCount(args, EXPECTED_ARGUMENT_COUNT)) return INVALID_ARGUMENT_NUMBER_MSG;
        return VALID_ARGUMENT;
    }

    @Override
    public CommandResult doExecute(String[] args) {

        logger.result("Team: BetterThanYesterday");
        logger.result("Members: 신재원, 정혜원, 문영민, 조효민, 류지우, 서인규");
        logger.result("");
        logger.result("");
        logger.result("Available commands:");
        logger.result("write {lba} {data} - Write data to the specified LBA");
        logger.result("read {lba} - Read data from the specified LBA");
        logger.result("exit - Exit the console");
        logger.result("erase {lba} {size}- erase data from the specified LBA to size");
        logger.result("erase_range {start_lba} {end_lba}- erase data between the specified LBA");
        logger.result("help - Display this help message");
        logger.result("fullwrite {data} - Write the same data to all LBAs");
        logger.result("fullread - Read all LBAs and display their values");
        logger.result("1_FullWriteAndReadCompare");
        logger.result("2_PartialLBAWrite");
        logger.result("3_WriteReadAging");
        logger.result("4_EraseAndWriteAging");
        logger.result("");
        logger.result("Note: ");
        logger.result("{lba} must be an integer between 0 and 99");
        logger.result("{data} must be 4-byte unsigned hexadecimal (0x00000000 - 0xFFFFFFFF)");
        return CommandResult.PASS;
    }
}