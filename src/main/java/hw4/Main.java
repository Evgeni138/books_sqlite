package hw4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final String URL = "jdbc:sqlite:C:/Example/books_sqlite/src/main/java/hw4/books.db";

    public static void main(String[] args) {
        try(Connection connection = DriverManager.getConnection(URL)) {
            createTable(connection);

            addData(connection, 1L, "The Tale of Tsar Saltan", "Pushkin");
            addData(connection, 2L, "The Tale of the Fisherman and the Fish", "Pushkin");
            addData(connection, 3L, "There's a green oak by the Lukomorye", "Pushkin");
            addData(connection, 4L, "The Tale of the Golden Cockerel", "Pushkin");
            addData(connection, 5L, "The Golden Key, or The Adventures of Pinocchio", "Tolstoy");
            addData(connection, 6L, "Snow house", "Tolstoy");
            addData(connection, 7L, "Vaska the cat", "Tolstoy");
            addData(connection, 8L, "Grey Neck", "Mamin-Siberyak");
            addData(connection, 9L, "Alyonushka's tales", "Mamin-Siberyak");
            addData(connection, 10L, "About the brave hare", "Mamin-Siberyak");

            selectBooksByAuthor(connection, "Pushkin");
            List<List<String>> books = getAllBooks(connection);
            System.out.println(books);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTable(Connection connection) {
        String dropTableSQL = "DROP TABLE IF EXISTS books";
        String createTableSQL = "CREATE TABLE books " +
                "(id bigint PRIMARY KEY, name varchar, author varchar)";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(dropTableSQL);
            statement.executeUpdate(createTableSQL);
            System.out.println("The table created successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addData(Connection connection, long id, String name, String author) {
        String insertSQL = "INSERT INTO books (id, name, author) VALUES (?, ?, ?)";

        try(PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setLong(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, author);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("The book added!");
            } else {
                System.out.println("Failed to add the book!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static void selectBooksByAuthor(Connection connection, String wantedAuthor) {
        String selectSQL = "SELECT * FROM books WHERE author = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setString(1, wantedAuthor);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    long id = resultSet.getLong("id");
                    String name = resultSet.getString("name");
                    String author = resultSet.getString("author");
                    System.out.println("Book ID: " + id + ", Name: " + name + ", Author: " + author);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static List<List<String>> getAllBooks(Connection connection) {
        String selectAllSQL = "SELECT * FROM books";
        List<List<String>> booksList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectAllSQL)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    List<String> book = new ArrayList<>();
                    long id = resultSet.getLong("id");
                    book.add(String.valueOf(id));
                    String name = resultSet.getString("name");
                    book.add(name);
                    String author = resultSet.getString("author");
                    book.add(author);
                    System.out.println("Book ID: " + id + ", Name: " + name + ", Author: " + author);
                    booksList.add(book);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return booksList;
    }
}
