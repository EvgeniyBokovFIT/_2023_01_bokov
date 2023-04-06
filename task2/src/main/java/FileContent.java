import java.util.List;

public record FileContent (
        String firstLine,
        List<String> parameters
){}
