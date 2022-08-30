package com.techelevator.tenmo.dao;
import com.techelevator.tenmo.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcUserDao implements UserDao {

    private static final BigDecimal STARTING_BALANCE = new BigDecimal("1000.00");
    private JdbcTemplate jdbcTemplate;

    public JdbcUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Queries the database by calling the jdbcTemplate's queryForObject method.
     * It uses the takes in the SQL statement we wrote and calls the queryForObject method form the jdbcTemplate class.
     * The method takes in the statement the integer class to mark the return type and the username which is replaces
     * with the wildcard ? in the statement. This Allows it to search by any username.
     * @param username
     * @return integer -> User ID
     */
    @Override
    public int findIdByUsername(String username) {
        String sql = "SELECT user_id FROM tenmo_user WHERE username ILIKE ?;";
        Integer id = jdbcTemplate.queryForObject(sql, Integer.class, username);
        if (id != null) {
            return id;
        } else {
            return -1;
        }
    }

    /**
     * Queries the database queries the database for all users in the database by calling the jdbcTemplate's
     * queryForObject method. It uses the takes in the SQL statement we wrote. Then it loops through the results.
     * It calls the mapRowToUser to instantiate the Users and adds each one to the list.
     * @returns List of Users
     */
    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id, username, password_hash FROM tenmo_user;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while(results.next()) {
            User user = mapRowToUser(results);
            users.add(user);
        }
        return users;
    }

    /**
     * Queries the database by calling the jdbcTemplate's queryForRowSet method.
     * It uses the takes in the SQL statement we wrote and the username as the arguments.
     * Then it calls the mapRowToUser to instantiate the user for the return.
     * @param username
     * @returns a User
     * @throws UsernameNotFoundException
     */
    @Override
    public User findByUsername(String username) throws UsernameNotFoundException {
        String sql = "SELECT user_id, username, password_hash FROM tenmo_user WHERE username ILIKE ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, username);
        if (rowSet.next()){
            return mapRowToUser(rowSet);
        }
        throw new UsernameNotFoundException("User " + username + " was not found.");
    }

    /**
     * This does two queries:
     *
     * First- it creates the user with an Insert SQL statement to add it. It calls the queryForObject method from the
     * jdbcTemplate and takes in the username and password provided and replaces them with the wildcards from the SQL
     * statement. The query returns the USER ID and stores it.
     *
     * Second- It creates the account by using a different Insert SQL statement. It calls the update method from the \
     * jdbcTemplate and puts in the USER ID that was returned from the first query and the constant for the $1,000.00
     * starting balance that each user gets.
     * in for the values
     * @param username
     * @param password
     * @returns true - when it accesses the data and the User and Account are created.
     *
     */
    @Override
    public boolean create(String username, String password) {

        // create user
        String sql = "INSERT INTO tenmo_user (username, password_hash) VALUES (?, ?) RETURNING user_id";
        String password_hash = new BCryptPasswordEncoder().encode(password);
        Integer newUserId;
        try {
            newUserId = jdbcTemplate.queryForObject(sql, Integer.class, username, password_hash);
        } catch (DataAccessException e) {
            return false;
        }

        // create account
        sql = "INSERT INTO account (user_id, balance) values(?, ?)";
        try {
            jdbcTemplate.update(sql, newUserId, STARTING_BALANCE);
        } catch (DataAccessException e) {
            return false;
        }

        return true;
    }

    /**
     * This helper method takes in the queried information from the database and uses it to instantiate the User by
     * calling all the setter methods to set the Id, Username, Password, activation status and authorities.
     * @param rowSet
     * @returns a User
     */
    private User mapRowToUser(SqlRowSet rowSet) {
        User user = new User();
        user.setId(rowSet.getInt("user_id"));
        user.setUsername(rowSet.getString("username"));
        user.setPassword(rowSet.getString("password_hash"));
        user.setActivated(true);
        user.setAuthorities("USER");
        return user;
    }
}
