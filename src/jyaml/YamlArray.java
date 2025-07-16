package jyaml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class YamlArray {

    private final List<Object> list = new ArrayList<>();

    public YamlArray() {}

    public YamlArray add(Object value) {
        list.add(value == null ? YamlNull.INSTANCE : value);
        return this;
    }

    public int size() {
        return list.size();
    }

    public boolean isObject(int index) {
        return list.get(index) instanceof YamlObject;
    }

    public boolean isPrimitive(int index) {
        return !(list.get(index) instanceof YamlObject);
    }



    public Object get(int index) { return list.get(index); }
    public String getString(int index) { return (String) list.get(index); }

    public int getInt(int index) {
        Object value = list.get(index);

        if (value instanceof Number) {
            return ((Number) value).intValue();
        }

        throw new YamlException("Value at index: " + index + " is not a number");
    }

    public boolean getBoolean(int index) {
        Object value = list.get(index);

        if (value instanceof Boolean) {
            return (boolean) value;
        }

        throw new YamlException("Value at index: " + index + " is not a boolean");
    }

    public double getDouble(int index) {
        Object value = list.get(index);

        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }

        throw new YamlException("Value at index: " + index + " is not a double");
    }

    public YamlObject getObject(int index) { return (YamlObject) list.get(index); }
    public YamlArray getArray(int index) { return (YamlArray) list.get(index); }

    
    public Object get(int index, Object default_value) {
        return list.get(index) != null ? list.get(index) : default_value;
    }

    public String getString(int index, String default_value) {
        Object value = list.get(index);

        if (value instanceof String) {
            return (String) value;
        }

        return default_value;
    }

    public int getInt(int index, int default_value) {
        Object value = list.get(index);

        if (value instanceof Number) {
            return ((Number) value).intValue();
        }

        return default_value;
    }

    public boolean getBoolean(int index, boolean default_value) {
        Object value = list.get(index);

        if (value instanceof Boolean) {
            return (boolean) value;
        }

        return default_value;
    }

    public double getDouble(int index, double default_value) {
        Object value = list.get(index);

        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }

        return default_value;
    }

    public YamlObject getObject(int index, YamlObject default_value) {
        Object value = list.get(index);

        if (value instanceof YamlObject) {
            return (YamlObject) value;
        }

        return default_value;
    }

    public YamlArray getArray(int index, YamlArray default_value) {
        Object value = list.get(index);

        if (value instanceof YamlArray) {
            return (YamlArray) value;
        }

        return default_value;
    }
    

    public void yamlToString(StringBuilder sb, int currentIndent, int indent) {
        String prefix = " ".repeat(currentIndent);

        for (Object value : list) {
            if (value instanceof YamlObject) {
                sb.append(prefix).append("-\n");
                ((YamlObject) value).yamlToString(sb, currentIndent + indent, indent);
            } else if (value instanceof YamlArray) {
                sb.append(prefix).append("-\n");
                ((YamlArray) value).yamlToString(sb, currentIndent + indent, indent);
            } else if (value == YamlNull.INSTANCE) {
                sb.append(prefix).append("-\n");
            } else {
                sb.append(prefix).append("- ").append(value).append("\n");
            }
        }
    }
}
