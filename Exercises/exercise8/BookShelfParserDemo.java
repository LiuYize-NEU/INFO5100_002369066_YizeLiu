package Exercises.exercise8;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.json.*;
import java.io.*;
import java.util.*;

public class BookShelfParserDemo {
    public static void main(String[] args) throws Exception {
        // Sample XML input
        String xmlData = """
        <BookShelf>
            <Book>
                <title>Effective Java</title>
                <publishedYear>2008</publishedYear>
                <numberOfPages>416</numberOfPages>
                <authors>
                    <author>Joshua Bloch</author>
                </authors>
            </Book>
            <Book>
                <title>Clean Code</title>
                <publishedYear>2009</publishedYear>
                <numberOfPages>464</numberOfPages>
                <authors>
                    <author>Robert C. Martin</author>
                </authors>
            </Book>
            <Book>
                <title>Java Concurrency in Practice</title>
                <publishedYear>2006</publishedYear>
                <numberOfPages>384</numberOfPages>
                <authors>
                    <author>Brian Goetz</author>
                    <author>Tim Peierls</author>
                </authors>
            </Book>
        </BookShelf>
        """;

        System.out.println("=== XML Parser Output ===");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(xmlData)));
        printXMLBooks(doc);

        // Add new book entry programmatically
        Element newBook = doc.createElement("Book");
        Element title = doc.createElement("title");
        title.setTextContent("Java: The Complete Reference");
        Element year = doc.createElement("publishedYear");
        year.setTextContent("2021");
        Element pages = doc.createElement("numberOfPages");
        pages.setTextContent("1248");
        Element authors = doc.createElement("authors");
        Element author1 = doc.createElement("author");
        author1.setTextContent("Herbert Schildt");
        authors.appendChild(author1);

        newBook.appendChild(title);
        newBook.appendChild(year);
        newBook.appendChild(pages);
        newBook.appendChild(authors);
        doc.getDocumentElement().appendChild(newBook);

        System.out.println("\n=== XML After Adding New Book ===");
        printXMLBooks(doc);

        String jsonData = """
        {
            "BookShelf": [
                {
                    "title": "Effective Java",
                    "publishedYear": 2008,
                    "numberOfPages": 416,
                    "authors": ["Joshua Bloch"]
                },
                {
                    "title": "Clean Code",
                    "publishedYear": 2009,
                    "numberOfPages": 464,
                    "authors": ["Robert C. Martin"]
                },
                {
                    "title": "Java Concurrency in Practice",
                    "publishedYear": 2006,
                    "numberOfPages": 384,
                    "authors": ["Brian Goetz", "Tim Peierls"]
                }
            ]
        }
        """;

        System.out.println("\n=== JSON Parser Output ===");
        JsonReader jsonReader = Json.createReader(new StringReader(jsonData));
        JsonObject bookshelf = jsonReader.readObject();
        jsonReader.close();
        printJSONBooks(bookshelf);

        // Add new book to JSON
        JsonArray oldBooks = bookshelf.getJsonArray("BookShelf");
        JsonObject newBookJson = Json.createObjectBuilder()
                .add("title", "Java: The Complete Reference")
                .add("publishedYear", 2021)
                .add("numberOfPages", 1248)
                .add("authors", Json.createArrayBuilder().add("Herbert Schildt"))
                .build();

        JsonArrayBuilder updatedBooksBuilder = Json.createArrayBuilder();
        for (JsonValue book : oldBooks) {
            updatedBooksBuilder.add(book);
        }
        updatedBooksBuilder.add(newBookJson);

        JsonObject updatedBookshelf = Json.createObjectBuilder()
                .add("BookShelf", updatedBooksBuilder)
                .build();

        System.out.println("\n=== JSON After Adding New Book ===");
        printJSONBooks(updatedBookshelf);
    }

    public static void printXMLBooks(Document doc) {
        NodeList books = doc.getElementsByTagName("Book");
        for (int i = 0; i < books.getLength(); i++) {
            Element book = (Element) books.item(i);
            String title = book.getElementsByTagName("title").item(0).getTextContent();
            String year = book.getElementsByTagName("publishedYear").item(0).getTextContent();
            String pages = book.getElementsByTagName("numberOfPages").item(0).getTextContent();
            NodeList authors = book.getElementsByTagName("author");

            System.out.println("Book: " + title);
            System.out.println("  Year: " + year);
            System.out.println("  Pages: " + pages);
            System.out.print("  Authors: ");
            for (int j = 0; j < authors.getLength(); j++) {
                System.out.print(authors.item(j).getTextContent());
                if (j < authors.getLength() - 1) System.out.print(", ");
            }
            System.out.println("\n");
        }
    }

    public static void printJSONBooks(JsonObject jsonObj) {
        JsonArray books = jsonObj.getJsonArray("BookShelf");
        for (JsonValue bookVal : books) {
            JsonObject book = bookVal.asJsonObject();
            System.out.println("Book: " + book.getString("title"));
            System.out.println("  Year: " + book.getInt("publishedYear"));
            System.out.println("  Pages: " + book.getInt("numberOfPages"));
            System.out.print("  Authors: ");
            JsonArray authors = book.getJsonArray("authors");
            for (int i = 0; i < authors.size(); i++) {
                System.out.print(authors.getString(i));
                if (i < authors.size() - 1) System.out.print(", ");
            }
            System.out.println("\n");
        }
    }
}
