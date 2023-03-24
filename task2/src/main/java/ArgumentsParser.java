import exception.FileException;
import org.apache.commons.cli.*;

public class ArgumentsParser {
    private static final String OUTPUT_TO_FILE_OPTION = "f";
    private static final String OUTPUT_TO_CONSOLE_OPTION = "c";
    private static final String FILE_EXTENSION = ".txt";

    private String outputFilename;
    private String inputFilename;
    private OutputType outputType;

    private Options options;

    public void parse(String[] args) throws ParseException {
        CommandLineParser parser = new DefaultParser();
        initOptions();

        CommandLine commandLine = parser.parse(this.options, args);

        if(commandLine.getArgList().size() < 1) {
            throw new ParseException("Не указан входной файл");
        }

        outputType = OutputType.Console;
        if(commandLine.hasOption(OUTPUT_TO_FILE_OPTION)) {
            this.outputType = OutputType.File;
            this.outputFilename = parseFilename(commandLine.getOptionValue(OUTPUT_TO_FILE_OPTION));
        }

        this.inputFilename = parseFilename(commandLine.getArgList().get(0));
    }

    private void initOptions() {
        this.options = new Options();
        OptionGroup outputStream = new OptionGroup();
        outputStream.addOption(Option.builder(OUTPUT_TO_CONSOLE_OPTION)
                .desc("output to console ")
                .build());
        outputStream.addOption(Option.builder(OUTPUT_TO_FILE_OPTION)
                .argName("outfile")
                .hasArg()
                .desc("output to file")
                .build());
        outputStream.setRequired(false);
        this.options.addOptionGroup(outputStream);
    }

    private String parseFilename(String filename) {
        if(!filename.endsWith(FILE_EXTENSION)) {
            throw new FileException("Неверное расширение файла " + filename);
        }
        return filename;
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
}
