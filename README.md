# 🟡 JYaml

**JYaml** is a lightweight YAML parser and writer for Java.  
It provides a simple API similar to `org.json`, but for YAML files.

![License](https://img.shields.io/github/license/diyarKeK/JYaml?color=yellow)
![Stars](https://img.shields.io/github/stars/diyarKeK/JYaml?style=social)

---

## Features

✅ Parse `.yaml` or `.yml` files easily  
✅ Lightweight – no external dependencies  
✅ `YamlObject`, `YamlArray`, and `YamlNull` types  
✅ Getter methods with default values  
✅ Read YAML from file or string  
✅ Write YAML back to file or string  

---

## Installation

Right now, you can [download the latest release](https://github.com/diyarKeK/JYaml/releases) and include the `jyaml-x.x.jar` in your project.

(Coming soon: Maven Central & Gradle dependency)

---

## Quick Start

### Example YAML (`config.yaml`)
```yaml
name: JYaml
version: 1.0
enabled: true
items:
  - simple string
  - 123
  - true
  - name: Nested
    value: 42
````

### Java Code

```java
import jyaml.Yaml;
import jyaml.YamlArray;
import jyaml.YamlException;
import jyaml.YamlNull;
import jyaml.YamlObject;

public class Main {
    public static void main(String[] args) {
        YamlObject root = Yaml.parse("config.yaml");

        System.out.println("Name: " + root.getString("name"));
        System.out.println("Version: " + root.getDouble("version"));
        System.out.println("Enabled: " + root.getBoolean("enabled"));

        YamlArray items = root.getArray("items");
        System.out.println("First item: " + items.getString(0));
    }
}
```

**Output:**

```
Name: JYaml
Version: 1.0
Enabled: true
First item: simple string
```

---

## 🛠 API Overview

* **Yaml.parse(String path)** → `YamlObject`
* **Yaml.parseYaml(String content)** → parse from raw string
* **YamlObject** → key-value YAML mapping

  * `getString(key)`, `getInt(key)`, `getBoolean(key)`, `getObject(key)`, `getArray(key)`
  * Default-value getters: `getString(key, defaultValue)`
* **YamlArray** → list of YAML values

  * `get(index)`, `getString(index)`, `getObject(index)` etc.
* **Yaml.write(YamlObject)** → write YAML to string/file

---

## ⚖️ License

JYaml is released under the [MIT License](LICENSE).

---

## Contributing

Pull requests and issues are welcome! Feel free to improve the parser, add features or optimize performance.

---

## ⭐ Why JYaml?

* **Simple** like `org.json`
* **No heavy dependencies** like SnakeYAML
* Perfect for small projects and configs

If you like this project, give it a **⭐ on GitHub**!
