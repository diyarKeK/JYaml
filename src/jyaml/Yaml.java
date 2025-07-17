package jyaml;

import javax.imageio.IIOException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Yaml {

    private static class IndexWrapper {
        int value;
        IndexWrapper(int v) { value = v; }
    }

    public static YamlObject parseFile(File file) {
        return parse(file.getPath());
    }

    public static YamlObject parseYaml(String content) {
        List<String> lines = List.of(content.split("\n"));
        List<String> cleaned = new ArrayList<>();

        for (String line : lines) {
            if (!line.trim().isEmpty() && !line.trim().startsWith("#")) {
                cleaned.add(line);
            }
        }

        return parseBlock(cleaned, new IndexWrapper(0), 0);
    }

    public static YamlObject parse(String path) {
        if (!path.endsWith(".yaml") && !path.endsWith(".yml"))
            throw new YamlException("File: " + path + " is not Yaml File");

        try {
            List<String> lines = Files.readAllLines(Path.of(path));
            List<String> cleaned = new ArrayList<>();

            for (String line : lines) {
                if (!line.trim().isEmpty() && !line.trim().startsWith("#")) {
                    cleaned.add(line);
                }
            }

            return parseBlock(cleaned, new IndexWrapper(0), 0);
        } catch (IOException e) {
            throw new YamlException("Failed to read Yaml file: " + e.getMessage());
        }
    }

    private static YamlObject parseBlock(List<String> lines, IndexWrapper index, int baseIndent) {
        YamlObject yaml = new YamlObject();

        while (index.value < lines.size()) {
            String rawLine = lines.get(index.value);
            int indent = countIndent(rawLine);

            if (indent < baseIndent)
                break;

            String line = rawLine.trim();

            if (isKeyValue(line)) {
                String[] key_value = splitKeyValue(line);
                String key = key_value[0].trim();
                String value = key_value[1].trim();

                if (value.isEmpty()) {
                    if (index.value + 1 < lines.size()) {
                        String nextRaw = lines.get(index.value + 1);
                        int nextIndent = countIndent(nextRaw);

                        if (nextIndent > indent) {
                            index.value++;
                            String nextLine = lines.get(index.value).trim();

                            if (nextLine.startsWith("- ")) {
                                YamlArray array = parseArray(lines, index, nextIndent);
                                yaml.put(key, array);
                            } else {
                                YamlObject nested = parseBlock(lines, index, nextIndent);
                                yaml.put(key, nested);
                            }
                            continue;
                        }
                    }

                    yaml.put(key, YamlNull.INSTANCE);
                } else {
                    yaml.put(key, parseScalar(value));
                }
            }

            index.value++;
        }

        return yaml;
    }

    private static YamlArray parseArray(List<String> lines, IndexWrapper index, int baseIndent) {
        YamlArray arr = new YamlArray();

        while (index.value < lines.size()) {
            String rawLine = lines.get(index.value);
            int indent = countIndent(rawLine);

            if (indent < baseIndent) break;

            String line = rawLine.trim();

            if (line.startsWith("- ")) {
                String value = line.substring(2).trim();

                if (value.isEmpty()) {

                    index.value++;
                    arr.add(parseBlock(lines, index, indent + 2));
                    continue;
                } else if (value.contains(":")) {
                    String[] kv = value.split(":", 2);
                    YamlObject elementObj = new YamlObject();
                    elementObj.put(kv[0].trim(), parseScalar(kv[1].trim()));

                    if (index.value + 1 < lines.size()) {
                        String nextLine = lines.get(index.value + 1);
                        int nextIndent = countIndent(nextLine);

                        if (nextIndent > indent) {

                            index.value++;
                            YamlObject nested = parseBlock(lines, index, nextIndent);

                            for (String key : nested.keySet()) {
                                Object val = nested.get(key);
                                elementObj.put(key, val);
                            }
                            arr.add(elementObj);
                            continue;
                        }
                    }
                    arr.add(elementObj);
                } else {
                    arr.add(parseScalar(value));
                }
            } else {
                break;
            }

            index.value++;
        }

        return arr;
    }

    private static Object parseScalar(String value) {
        if ("true".equalsIgnoreCase(value)) return true;
        if ("false".equalsIgnoreCase(value)) return false;
        if (value.matches("-?\\d+\\.\\d+")) return Double.parseDouble(value);
        if (value.matches("-?\\d+")) return Integer.parseInt(value);
        if ("null".equalsIgnoreCase(value) || "~".equals(value)) return YamlNull.INSTANCE;

        return value;
    }

    private static boolean isKeyValue(String line) {
        return line.contains(":");
    }

    private static String[] splitKeyValue(String line) {
        String[] parts = line.split(":", 2);
        return new String[]
                { parts[0].trim(), parts[1].trim() };
    }

    private static boolean isListItem(String line) {
        return line.startsWith("- ");
    }

    private static String parseListItemValue(String line) {
        return line.substring(2).trim();
    }

    private static int countIndent(String line) {
        int count = 0;

        for (char c : line.toCharArray()) {
            if (c == ' ') count++;
            else break;
        }

        return count;
    }




    public static void write(File file,YamlObject obj) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(obj.toString());
        }
    }

    public static void write(String file_path, YamlObject obj) throws IOException {
        try (FileWriter writer = new FileWriter(file_path)) {
            writer.write(obj.toString());
        }
    }

    public static void write(File file,YamlObject obj, int indent) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(obj.toString(indent));
        }
    }

    public static void write(String file_path, YamlObject obj, int indent) throws IOException {
        try (FileWriter writer = new FileWriter(file_path)) {
            writer.write(obj.toString(indent));
        }
    }
}
