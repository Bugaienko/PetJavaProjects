package ua.bugaienko.springmvcapp.dao;

import org.springframework.jdbc.core.RowMapper;
import ua.bugaienko.springmvcapp.models.Person;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Sergii Bugaienko
 */

public class PersonLibMapper implements RowMapper<Person> {
    @Override
    public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
        Person person = new Person();

        person.setPersonId(rs.getInt("person_id"));
        person.setFullName(rs.getString("full_name"));
        person.setYearBirthday(rs.getInt("year_birthday"));

        return person;
    }
}
