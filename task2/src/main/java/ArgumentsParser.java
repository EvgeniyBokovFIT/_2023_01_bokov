import exception.FileException;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArgumentsParser {
    private static final String OUTPUT_TO_FILE_OPTION = "f";
    private static final String OUTPUT_TO_CONSOLE_OPTION = "c";
    private static final String FILE_EXTENSION = ".txt";
    private static final Logger log = LoggerFactory.getLogger(ArgumentsParser.class);

    private String outputFilename;
    private String inputFilename;
    private OutputType outputType;
    private Options options;

    public void parse(String[] args) throws ParseException {
        CommandLineParser parser = new DefaultParser();
        initOptions();

        CommandLine commandLine = parser.parse(this.options, args);

        if(commandLine.getArgList().size() < 1) {
            final String inputFilenameMissingMessage = "В командной строке отсутствует название входного файла";
            log.error(inputFilenameMissingMessage);
            throw new ParseException(inputFilenameMissingMessage);
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

    private void initOptions() {
        this.options = new Options();
        OptionGroup outputStream = new OptionGroup();
        outputStream.addOption(Option.builder(OUTPUT_TO_CONSOLE_OPTION)
                .desc("вывод в консоль")
                .build());
        outputStream.addOption(Option.builder(OUTPUT_TO_FILE_OPTION)
                .argName("outfile")
                .hasArg()
                .desc("вывод в файл")
                .build());
        outputStream.setRequired(false);
        this.options.addOptionGroup(outputStream);
    }

    private String parseFilename(String filename) {
        if(!filename.endsWith(FILE_EXTENSION)) {
            final String invalidFileExtensionMessage = "Неверное расширение файла";
            log.error(invalidFileExtensionMessage + ". Название файла: \"{}\"", filename);
            throw new FileException(invalidFileExtensionMessage);
        }
        return filename;
    }
}
