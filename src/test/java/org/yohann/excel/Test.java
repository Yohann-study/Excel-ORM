package org.yohann.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import org.yohann.excel.annotation.ExcelFile;
import org.yohann.excel.entity.Excel;
import org.yohann.excel.mapper.AbstractExcelMapper;
import org.yohann.excel.query.Criteria;

import java.util.Date;
import java.util.List;

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