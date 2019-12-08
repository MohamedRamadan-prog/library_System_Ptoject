package ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import business.Address;
import business.Author;
import business.Book;
import business.ControllerInterface;
import business.LibraryMember;
import business.SystemController;
import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AddBookWindow extends Stage implements LibWindow {
	public static final AddBookWindow INSTANCE = new AddBookWindow();

	private boolean isInitialized = false;

	public boolean isInitialized() {
		return isInitialized;
	}

	public void isInitialized(boolean val) {
		isInitialized = val;
	}

	private Text messageBar = new Text();

	public void clearIsbn() {
		messageBar.setText("");

	}

	private void addBook(String isbn, String title, String authors, String maxCheckoutLength) {
		DataAccess da = new DataAccessFacade();
		List<Author> authorsList = new ArrayList<Author>();
		int maxLength = 0;
		String authorsAsArray[] = authors.split("\\s+");
		for (int i = 0; i < authorsAsArray.length; i++) {
			String[] fields = authorsAsArray[i].split(",");
			String add[] = fields[3].split(":");
			Address addres = new Address(add[0], add[1], add[2], add[3]);
			Author auth = new Author(fields[0], fields[1], fields[2], addres, fields[4]);
			authorsList.add(auth);
		}
		try {
			maxLength = Integer.parseInt(maxCheckoutLength);
		} catch (NumberFormatException e) {
			System.out.println("max checkout out length must be a number");
		}
		Book currentBook = new Book(isbn, title, maxLength, authorsList);
		da.saveBooks(currentBook);
	}

	/* This class is a singleton */
	private AddBookWindow() {
	}

	public void init() {
		GridPane grid = new GridPane();
		grid.setId("top-container");
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Text scenetitle = new Text("Add Book Window");
		scenetitle.setFont(Font.font("Harlow Solid Italic", FontWeight.NORMAL, 20)); // Tahoma
		grid.add(scenetitle, 0, 0, 1, 1);

		Label bookISBN = new Label("book ISBN:");
		grid.add(bookISBN, 0, 1);
		TextField ISBNTextField = new TextField();
		grid.add(ISBNTextField, 0, 2);

		Label title = new Label("title:");
		grid.add(title, 0, 3);
		TextField titleTextField = new TextField();
		grid.add(titleTextField, 0, 4);

		Label authors = new Label("Author(s) (firstname,lastname,telephone,Street:City:State:Zip,Bio)):");
		grid.add(authors, 0, 5);
		TextField authorsTextField = new TextField();
		grid.add(authorsTextField, 0, 6);

		Label maxCheckoutLength = new Label("Max checkout length (7 or 21):");
		grid.add(maxCheckoutLength, 0, 7);
		TextField maxCheckoutLengthTextField = new TextField();
		grid.add(maxCheckoutLengthTextField, 0, 8);
		Button backBtn = new Button("<= Back to Main");

		backBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Start.hideAllWindows();
				Start.retAdminWindow().show();
			}
		});
		Button addBook = new Button("Add Book");

		addBook.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				messageBar.setFill(Start.Colors.green);
				messageBar.setText("Book Added");
				addBook(ISBNTextField.getText(), authorsTextField.getText(), authorsTextField.getText(),
						maxCheckoutLengthTextField.getText());
				Start.hideAllWindows();
				Start.retAdminWindow().show();
			}
		});
		
		addBook.setAlignment(Pos.CENTER);
		HBox hBack = new HBox(40);
		hBack.setAlignment(Pos.BOTTOM_LEFT);
		addBook.setPrefWidth(150);
		backBtn.setPrefWidth(200);
		hBack.getChildren().addAll(backBtn, addBook);
		grid.add(hBack, 0, 11);

		VBox messageBox = new VBox(10);
		messageBox.setAlignment(Pos.BOTTOM_RIGHT);
		messageBox.getChildren().addAll(messageBar);
		grid.add(messageBox, 1, 8);

		Scene scene = new Scene(grid, 600, 600);
		scene.getStylesheets().add(getClass().getResource("library.css").toExternalForm());
		setScene(scene);
		isInitialized(true);
	}
}
