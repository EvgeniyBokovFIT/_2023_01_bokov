import exception.FileException;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandLineArgumentsParser {
    private static final String OUTPUT_TO_FILE_OPTION = "f";
    private static final String OUTPUT_TO_CONSOLE_OPTION = "c";
    private static final String OUTPUT_TO_CONSOLE_OPTION_DESC = "вывод в консоль";
    private static final String OUTPUT_TO_FILE_OPTION_DESC = "вывод в файл";
    private static final String FILE_EXTENSION = ".txt";
    private static final String INVALID_FILE_EXTENSION_MESSAGE = "Неверное расширение файла ";
    private static final String INPUT_FILENAME_MISSING_MESSAGE = "В командной строке отсутствует название входного файла";

    private static final Logger log = LoggerFactory.getLogger(CommandLineArgumentsParser.class);

    private String outputFilename;
    private final String inputFilename;
    private OutputType outputType;

    public CommandLineArgumentsParser(String[] args) throws ParseException {
        CommandLine commandLine = new DefaultParser().parse(initOptions(), args);
        if(commandLine.getArgList().size() < 1) {
            log.error(INPUT_FILENAME_MISSING_MESSAGE);
            throw new ParseException(INPUT_FILENAME_MISSING_MESSAGE);
        }

        outputType = OutputType.CONSOLE;
        if(commandLine.hasOption(OUTPUT_TO_FILE_OPTION)) {
            this.outputType = OutputType.FILE;
            this.outputFilename = parseFilename(commandLine.getOptionValue(OUTPUT_TO_FILE_OPTION));
        }

        this.inputFilename = parseFilename(commandLine.getArgList().get(0));
    }

    public String getOutputFilename() {
        return outputFilename;
    }

    public String getInputFilename() {
        return inputFilename;
    }

    public OutputType getOutputType() {
        return outputType;
    }

    private static Options initOptions() {
        Options options = new Options();
        OptionGroup outputStream = new OptionGroup();
        outputStream.addOption(Option.builder(OUTPUT_TO_CONSOLE_OPTION)
                .desc(OUTPUT_TO_CONSOLE_OPTION_DESC)
                .build());
        outputStream.addOption(Option.builder(OUTPUT_TO_FILE_OPTION)
                .argName("outfile")
                .hasArg()
                .desc(OUTPUT_TO_FILE_OPTION_DESC)
                .build());
        outputStream.setRequired(false);
        options.addOptionGroup(outputStream);
        return options;
    }

    private String parseFilename(String filename) {
        if(!filename.endsWith(FILE_EXTENSION)) {
            log.error(INVALID_FILE_EXTENSION_MESSAGE + filename);
            throw new FileException(INVALID_FILE_EXTENSION_MESSAGE, filename);
        }
        return filename;
    }
}
