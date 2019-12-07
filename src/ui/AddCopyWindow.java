package ui;

import java.util.HashMap;
import java.util.List;

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

public class AddCopyWindow extends Stage implements LibWindow {
	public static final AddCopyWindow INSTANCE = new AddCopyWindow();

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

	private void addCopy(String isbn) {
		ControllerInterface c = new SystemController();
		List<String> bookList = c.allBookIds();
		DataAccess da = new DataAccessFacade();
		HashMap<String, Book> map = da.readBooksMap();
		Book currentBook = null;
		if (bookList.contains(isbn.trim())) {
			currentBook = map.get(isbn);
			currentBook.addCopy();
			da.saveBooks(currentBook);
		}else {
			messageBar.setFill(Start.Colors.red);
        	messageBar.setText("Book not Found");
		}
	}

	/* This class is a singleton */
	private AddCopyWindow() {
	}

	public void init() {
		GridPane grid = new GridPane();
		grid.setId("top-container");
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Text scenetitle = new Text("Add Copy Window");
		scenetitle.setFont(Font.font("Harlow Solid Italic", FontWeight.NORMAL, 20)); // Tahoma
		grid.add(scenetitle, 0, 0, 1, 1);

		Label bookISBN = new Label("book ISBN:");
		grid.add(bookISBN, 0, 1);
		TextField ISBNTextField = new TextField();
		grid.add(ISBNTextField, 1, 1);

		Button backBtn = new Button("<= Back to Main");

		backBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Start.hideAllWindows();
				Start.retAdminWindow().show();
			}
		});
		Button addCopy = new Button("Add Copy");

		addCopy.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				messageBar.setFill(Start.Colors.green);
	        	messageBar.setText("Book Added");
				addCopy(ISBNTextField.getText());

			}
		});
		addCopy.setAlignment(Pos.CENTER);
		HBox hBack = new HBox(40);
		hBack.setAlignment(Pos.BOTTOM_LEFT);
		addCopy.setPrefWidth(150);
		backBtn.setPrefWidth(200);
		hBack.getChildren().addAll(backBtn,addCopy);
		grid.add(hBack, 0,8);
		
		VBox messageBox = new VBox(10);
		messageBox.setAlignment(Pos.BOTTOM_RIGHT);
		messageBox.getChildren().addAll(messageBar);
		grid.add(messageBox,1, 5);
		
		Scene scene = new Scene(grid, 500, 400);
		scene.getStylesheets().add(getClass().getResource("library.css").toExternalForm());
		setScene(scene);
		isInitialized(true);
	}
}
