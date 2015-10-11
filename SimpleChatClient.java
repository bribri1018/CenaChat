import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;


public class SimpleChatClient extends Application{

	JTextArea incoming;
	JTextField outgoing;
	BufferedReader reader;
	PrintWriter writer;
	Socket sock;
	SoundPlayer soundplayer = new SoundPlayer();

	
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("CENA CHAT");
		GridPane grid = new GridPane();
	    grid.setAlignment(Pos.CENTER);
	    grid.setHgap(10);
	    grid.setVgap(10);
	    grid.setPadding(new Insets(25, 25, 25, 25));
	    
	    final TextArea textArea = new TextArea();
	    textArea.setPrefColumnCount(20);
	    textArea.setPrefRowCount(30);
	    textArea.setWrapText(true);
	    textArea.setEditable(false);
	
        final TextField message = new TextField();
        message.setMinWidth(300);
        message.setPromptText("JOHN CENA");
        HBox hb = new HBox();
        hb.getChildren().add(message);
        
        hb.setSpacing(10);
        
        Button btn = new Button();
	    btn.setText("Send");
        btn.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	        	if (message.getText() != null && !message.getText().isEmpty()) {
	        		
	        		try {
	    				writer.println(message.getText());
	    				writer.flush();
	    			} catch (Exception ex) {
	    				ex.printStackTrace();
	    			}
	    			message.clear();
	    			soundplayer.play(new File("send.wav"));
	    			message.requestFocus();
	        	}
	        }
        });
        
        grid.add(hb, 0, 30);
        grid.add(btn, 1, 30);
        grid.add(textArea, 0, 0, 20, 20);
        
       setUpNetworking();
		Thread readerThread = new Thread(new Runnable() {
			public void run(){
				String message;
				try {
					while ((message= reader.readLine()) != null) {
						System.out.println("read " + message);
						textArea.appendText(message + "\n");
					}
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});

		readerThread.start();
        
        grid.setId("pane");
        Scene scene = new Scene(grid, 600, 500);
        primaryStage.setScene(scene);
        scene.getStylesheets().addAll(SimpleChatClient.class.getResource("style.css").toExternalForm());
        primaryStage.show();
    }
	
	public static void main(String[] args) {
		launch(args);
	}


	public void setUpNetworking() {

		try {
			sock = new Socket("172.20.10.3", 5000);
			InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
			reader = new BufferedReader(streamReader);
			writer = new PrintWriter(sock.getOutputStream());
			System.out.println("networking established");
		} catch(IOException ex) {
			ex.printStackTrace();
		}
	}

}
