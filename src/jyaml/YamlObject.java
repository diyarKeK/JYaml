package jyaml;

import java.util.*;

public class YamlObject {

    private final Map<String, Object> data = new LinkedHashMap<>();

    public YamlObject() {}


    public YamlObject put(String key, Object value) {
        data.put(key, value == null ? YamlNull.INSTANCE : value);
        return this;
    }

    public boolean has(String key) {
        return data.containsKey(key);
    }

    public Set<String> keySet() {
        return data.keySet();
    }

    public boolean isNull(String key) {
        return data.get(key) == null || data.get(key) == YamlNull.INSTANCE;
    }



    public Object get(String key) { return data.get(key); }

    public String getString(String key) {
        Object value = data.get(key);
        if (value == null || value == YamlNull.INSTANCE) return null;
        if (value instanceof String) return (String) value;
        throw new ClassCastException(value.getClass() + " cannot be cast to String");
    }

    public int getInt(String key) {
        Object value = data.get(key);

        if (value instanceof Number) {
            return ((Number) value).intValue();
        }

        throw new YamlException("Value at '" + key + "' is not a number");
    }

    public boolean getBoolean(String key) {
        Object value = data.get(key);

        if (value instanceof Boolean) {
            return (boolean) value;
        }

        throw new YamlException("Value at '" + key + "' is not a boolean");
    }

    public double getDouble(String key) {
        Object value = data.get(key);

        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }

        throw new YamlException("Value at '" + key + "' is not a double");
    }

    public YamlObject getObject(String key) {
        Object value = data.get(key);
        if (value == null || value == YamlNull.INSTANCE) return null;
        if (value instanceof YamlObject) return (YamlObject) value;
        throw new YamlException(
                "Value for key '" + key + "' is " + value.getClass().getSimpleName() +
                        ", not YamlObject"
        );
    }

    public YamlArray getArray(String key) {
        Object value = data.get(key);
        if (value == null || value == YamlNull.INSTANCE) return null;
        if (value instanceof YamlArray) return (YamlArray) value;
        throw new YamlException(
                "Value for key '" + key + "' is " + value.getClass().getSimpleName() +
                        ", not YamlArray"
        );
    }


    public Object get(String key, Object default_value) {
        return data.getOrDefault(key, default_value);
    }

    public String getString(String key, String default_value) {
        Object value = data.get(key);

        if (value instanceof String) {
            return (String) value;
        }

        return default_value;
    }

    public int getInt(String key, int default_value) {
        Object value = data.get(key);

        if (value instanceof Number) {
            return ((Number) value).intValue();
        }

        return default_value;
    }

    public boolean getBoolean(String key, boolean default_value) {
        Object value = data.get(key);

        if (value instanceof Boolean) {
            return (boolean) value;
        }

        return default_value;
    }

    public double getDouble(String key, double default_value) {
        Object value = data.get(key);

        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }

        return default_value;
    }

    public YamlObject getObject(String key, YamlObject default_value) {
        Object value = data.get(key);

        if (value instanceof YamlObject) {
            return (YamlObject) value;
        }

        return default_value;
    }

    public YamlArray getArray(String key, YamlArray default_value) {
        Object value = data.get(key);

        if (value instanceof YamlArray) {
            return (YamlArray) value;
        }

        return default_value;
    }

    @Override
    public String toString() {
        return toString(2);
    }

    public String toString(int indent) {
        StringBuilder sb = new StringBuilder();
        yamlToString(sb, 0, indent);
        return sb.toString();
    }

    void yamlToString(StringBuilder sb, int currentIndent, int indent) {
        String prefix = " ".repeat(currentIndent);
        for (String key : data.keySet()) {
            Object value = data.get(key);

            if (value instanceof YamlObject) {
                sb.append(prefix).append(key).append(":\n");
                ((YamlObject) value).yamlToString(sb, currentIndent + indent, indent);
            } else if (value instanceof YamlArray) {
                sb.append(prefix).append(key).append(":\n");
                ((YamlArray) value).yamlToString(sb, currentIndent + indent, indent);
            } else if (value == YamlNull.INSTANCE) {
                sb.append(prefix).append(key).append(":\n");
            } else {
                sb.append(prefix).append(key).append(": ").append(value).append("\n");
            }
        }
    }
}
