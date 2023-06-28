# Excel-ORM

Excel-ORM is a Java utility class based on EasyExcel, which is used to read and write Excel files. It provides a simple
way to map data between Java objects and Excel files. This tool provides functionality to read, query, insert, update,
and delete data from Excel files. Please refer to the following instructions for specific usage.

## Usage

### Add Dependency

In the `pom.xml` file, add the following dependency:

```xml

<dependency>
    <groupId>org.yohann.excel</groupId>
    <artifactId>excel-orm</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### Create Entity Class

Create a Java entity class that represents the data to be mapped from Excel. This class should contain fields that
correspond to the columns in the Excel file, and should implement the `Excel` interface.

```java
import com.alibaba.excel.annotation.ExcelProperty;
import org.yohann.excel.annotation.ExcelFile;
import org.yohann.excel.entity.Excel;

@ExcelFile(path = "a/b/c/d/e/f/g", filename = "person.xls")
public class Person implements Excel {
    @ExcelProperty("Name")
    private String name;
    @ExcelProperty("Age")
    private Integer age;
    @ExcelProperty("Gender")
    private String gender;
    // ... getter and setter methods omitted
}
```

### Create ExcelMapper

Create a Java interface or abstract class that extends `ExcelMapper` and specifies the generic type as the entity class
created in the previous step.

```java
import org.yohann.excel.mapper.ExcelMapper;

public interface PersonMapper extends ExcelMapper<Person> {
    // ... method declarations omitted
}
```

### Example Case

```java
/**
 * This class demonstrates the usage of TestExcel and TestExcelMapper classes to manipulate Excel data.
 */
public class Test {
    public static void main(String[] args) {
        // Create a new TestExcelMapper instance to manipulate Excel data
        TestExcelMapper testMapper = new TestExcelMapper();

        // Insert some test data into the Excel file
        TestExcel excel = new TestExcel();
        excel.setName("Tony");
        excel.setAge(18);
        excel.setBirthDate(new Date());
        testMapper.insert(excel);

        excel.setName("Pony");
        excel.setAge(20);
        testMapper.insert(excel);

        excel.setName("Jack");
        excel.setAge(36);
        testMapper.insert(excel);

        // Search for test data using a criteria that matches names containing "ony"
        Criteria criteria = new Criteria()
                .like("name", "ony");
        List<TestExcel> testExcels = testMapper.get(criteria);

        // Search for test data using a criteria that matches ages greater than 20 and birthdate less than or equal to today's date
        Criteria criteria2 = new Criteria()
                .greater("age", 20)
                .lessEquals("birthDate", new Date());
        List<TestExcel> testExcels2 = testMapper.get(criteria2);
    }
}

/**
 * This class represents the Excel data model for the TestExcelMapper class.
 */
@ExcelFile(path = "test/abc/af", filename = "test_excel.xlsx")
class TestExcel extends Excel {

    @ExcelProperty("Name")
    private String name;
    @ExcelProperty("Age")
    private Integer age;
    @ExcelProperty("Birth Date")
    private Date birthDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
}

/**
 * This class represents the Excel data mapper for the TestExcel class.
 */
class TestExcelMapper extends AbstractExcelMapper<TestExcel> {

}
```

### Criteria Methods

The `Criteria` class provides several methods that can be used to filter data when querying an Excel file. The following
table lists the methods and their purposes:

| Method Name | Purposes                                                                                                                                                                                       | Example |
|-------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------|
| `Criteria()` | Constructs an empty `Criteria` object.                                                                                                                                                         | `Criteria criteria = new Criteria();` |
| `Criteria(String fieldName, Object value)` | Constructs a `Criteria` object with a single `Matcher` that matches the given field name and value.                                                                                            | `Criteria criteria = new Criteria("name", "john");` |
| `like(String fieldName, Object value)` | Adds a `Matcher` to the `Criteria` object that matches the given field name and value using a partial matching algorithm, only supports matching against `String` objects.                     | `criteria.like("name", "john");` |
| `less(String fieldName, Object value)` | Adds a `Matcher` to the `Criteria` object that matches the given field name and value using a less-than comparison, only supports matching against `Number` and `Date` objects.                | `criteria.less("age", 30);` |
| `lessEquals(String fieldName, Object value)` | Adds a `Matcher` to the `Criteria` object that matches the given field name and value using a less-than-or-equal-to comparison, only supports matching against `Number` and `Date` objects.    | `criteria.lessEquals("salary", 10000.0);` |
| `greater(String fieldName, Object value)` | Adds a `Matcher` to the `Criteria` object that matches the given field name and value using a greater-than comparison, only supports matching against `Number` and `Date` objects.             | `criteria.greater("score", 80);` |
| `greaterEquals(String fieldName, Object value)` | Adds a `Matcher` to the `Criteria` object that matches the given field name and value using a greater-than-or-equal-to comparison, only supports matching against `Number` and `Date` objects. | `criteria.greaterEquals("date", someDate);` |
| `isNull(String fieldName)` | Adds a `Matcher` to the `Criteria` object that matches the given field name to `null`.                                                                                                         | `criteria.isNull("address");` |
| `notNull(String fieldName)` | Adds a `Matcher` to the `Criteria` object that matches the given field name to not `null`.                                                                                                     | `criteria.notNull("name");` |

# Excel-ORM

Excel-ORM 是一个基于 EasyExcel 的 Java 实用工具类，用于读写 Excel 文件。它提供了一种简单的方式，将 Java 对象与 Excel 文件之间进行数据映射。该工具提供了从 Excel
文件中读取、查询、插入、更新和删除数据的功能。请参考以下说明来了解具体用法。

## 用法

### 添加依赖

在 `pom.xml` 文件中添加以下依赖：

```xml

<dependency>
    <groupId>org.yohann.excel</groupId>
    <artifactId>excel-orm</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### 创建实体类

创建一个 Java 实体类，表示要从 Excel 中映射的数据。该类应包含与 Excel 文件中的列对应的字段，并实现 `Excel` 接口。

```java
import com.alibaba.excel.annotation.ExcelProperty;
import org.yohann.excel.annotation.ExcelFile;
import org.yohann.excel.entity.Excel;

@ExcelFile(path = "a/b/c/d/e/f/g", filename = "person.xls")
public class Person implements Excel {
    @ExcelProperty("Name")
    private String name;
    @ExcelProperty("Age")
    private Integer age;
    @ExcelProperty("Gender")
    private String gender;
    // ... getter and setter methods omitted
}
```

### 创建 ExcelMapper

创建一个 Java 接口或抽象类，它继承 `ExcelMapper` 并将泛型类型指定为前一步中创建的实体类。

```java
import org.yohann.excel.mapper.ExcelMapper;

public interface PersonMapper extends ExcelMapper<Person> {
    // ... method declarations omitted
}
```

### 示例案例

```java
/**
 * 该类演示了 TestExcel 和 TestExcelMapper 类的用法，以操作 Excel 数据。
 */
public class Test {
    public static void main(String[] args) {
        // 创建一个新的 TestExcelMapper 实例来操作 Excel 数据
        TestExcelMapper testMapper = new TestExcelMapper();

        // 向 Excel 文件中插入一些测试数据
        TestExcel excel = new TestExcel();
        excel.setName("Tony");
        excel.setAge(18);
        excel.setBirthDate(new Date());
        testMapper.insert(excel);

        excel.setName("Pony");
        excel.setAge(20);
        testMapper.insert(excel);

        excel.setName("Jack");
        excel.setAge(36);
        testMapper.insert(excel);

        // 使用匹配包含 "ony" 的名称的条件搜索测试数据
        Criteria criteria = new Criteria()
                .like("name", "ony");
        List<TestExcel> testExcels = testMapper.get(criteria);

        // 使用匹配年龄大于 20 并出生日期小于或等于今天日期的条件搜索测试数据
        Criteria criteria2 = new Criteria()
                .greater("age", 20)
                .lessEquals("birthDate", new Date());
        List<TestExcel> testExcels2 = testMapper.get(criteria2);
    }
}

/**
 * 该类表示 TestExcelMapper 类的 Excel 数据模型。
 */
@ExcelFile(path = "test/abc/af", filename = "test_excel.xlsx")
class TestExcel extends Excel {

    @ExcelProperty("Name")
    private String name;
    @ExcelProperty("Age")
    private Integer age;
    @ExcelProperty("Birth Date")
    private Date birthDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
}

/**
 * 该类表示 TestExcel 类的 Excel 数据映射器。
 */
class TestExcelMapper extends AbstractExcelMapper<TestExcel> {

}
```

### Criteria 方法

`Criteria` 类提供了几种方法，可用于在查询 Excel 文件时过滤数据。以下表格列出了方法及其用途：

| 方法名 | 用途                                                                                                                                                                                       | 示例 |
|-------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------|
| `Criteria()` | 构造一个空的 `Criteria` 对象。                                                                                                                                                         | `Criteria criteria = new Criteria();` |
| `Criteria(String fieldName, Object value)` | 构造一个具有单个 `Matcher` 的 `Criteria` 对象，该 `Matcher` 匹配给定的字段名和值。                                                                                            | `Criteria criteria = new Criteria("name", "john");` |
| `like(String fieldName, Object value)` | 向 `Criteria` 对象添加一个 `Matcher`，使用部分匹配算法匹配给定的字段名和值，仅支持匹配 `String` 对象。                     | `criteria.like("name", "john");` |
| `less(String fieldName, Object value)` | 向 `Criteria` 对象添加一个 `Matcher`，使用小于比较匹配给定的字段名和值，仅支持匹配 `Number` 和 `Date` 对象。                | `criteria.less("age", 30);` |
| `lessEquals(String fieldName, Object value)` | 向 `Criteria` 对象添加一个 `Matcher`，使用小于或等于比较匹配给定的字段名和值，仅支持匹配 `Number` 和 `Date` 对象。    | `criteria.lessEquals("salary", 10000.0);` |
| `greater(String fieldName, Object value)` | 向 `Criteria` 对象添加一个 `Matcher`，使用大于比较匹配给定的字段名和值，仅支持匹配 `Number` 和 `Date` 对象。             | `criteria.greater("score", 80);` |
| `greaterEquals(String fieldName, Object value)` | 向 `Criteria` 对象添加一个 `Matcher`，使用大于或等于比较匹配给定的字段名和值，仅支持匹配 `Number` 和 `Date` 对象。 | `criteria.greaterEquals("date", someDate);` |
| `isNull(String fieldName)` | 向 `Criteria` 对象添加一个 `Matcher`，将给定的字段名匹配为 `null`。                                                                                                         | `criteria.isNull("address");` |
| `notNull(String fieldName)` | 向 `Criteria` 对象添加一个 `Matcher`，将给定的字段名匹配为非 `null`。                                                                                                     | `criteria.notNull("name");` |