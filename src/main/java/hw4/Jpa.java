package hw4;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Jpa {
    private static final String URL = "jdbc:sqlite:C:/Example/books_sqlite/src/main/java/hw4/books.db";

    public static void main(String[] args) {
        final SessionFactory sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml").buildSessionFactory();

        try(Connection connection = DriverManager.getConnection(URL)){
            List<List<String>> booksFromBase = getAllBooks(connection);

            List<Book> books = booksFromBase.stream()
                    .map(bookData -> {
                        long id = Long.parseLong(bookData.get(0));
                        String name = bookData.get(1);
                        String author = bookData.get(2);
                        return new Book(id, name, author);
                    })
                    .toList();

//            try (Session session = sessionFactory.openSession()) {
//                session.beginTransaction();
//
//                for (Book book : books) {
//                    session.persist(book);
//                }
//
//                session.getTransaction().commit();
//            }
//
//            try (Session session = sessionFactory.openSession()) {
//                List<Book> bookList = session.createQuery("FROM Book", Book.class)
//                        .getResultList();
//                System.out.println(bookList);
//            }

            try (Session session = sessionFactory.openSession()) {
                List<Book> books2 = session.createQuery("select b from Book b where author = 'Pushkin'", Book.class)
                        .getResultList();

                System.out.println(books2);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static List<List<String>> getAllBooks(Connection connection) {
        String selectAllSQL = "SELECT * FROM book";
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
