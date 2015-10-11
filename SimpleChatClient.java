import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
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
	
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("CENA CHAT");
		GridPane grid = new GridPane();
	    grid.setAlignment(Pos.CENTER);
	    grid.setHgap(10);
	    grid.setVgap(10);
	    grid.setPadding(new Insets(25, 25, 25, 25));
	    
	    TextArea textArea = new TextArea();
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
	        		
	        		//textArea.appendText(message.getText() + "\n");
	        		try {
	    				writer.println(message.getText());
	    				writer.flush();
	    			} catch (Exception ex) {
	    				ex.printStackTrace();
	    			}
	    			message.clear();
	    			message.requestFocus();
	        	}
	        }
        });
        
        grid.add(hb, 0, 30);
        grid.add(btn, 1, 30);
        grid.add(textArea, 0, 0, 20, 20);
        
        setUpNetworking();
		Thread readerThread = new Thread(new IncomingReader());
		readerThread.start();
        
        primaryStage.setScene(new Scene(grid, 600, 500));
        primaryStage.show();
    }
	
	
	public static void main(String[] args) {
		launch(args);
	}

	public void go() {
		JFrame frame = new JFrame("CENA CHAT");
		JPanel mainPanel = new JPanel();
		incoming = new JTextArea(15, 50);
		incoming.setLineWrap(true);
		incoming.setWrapStyleWord(true);
		incoming.setEditable(false);
		JScrollPane qScroller = new JScrollPane(incoming);
		qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		outgoing = new JTextField(20);
		JButton sendButton = new JButton("Send");
		sendButton.addActionListener(new SendButtonListener());
		mainPanel.add(qScroller);
		mainPanel.add(outgoing);
		mainPanel.add(sendButton);
		
		setUpNetworking();
		Thread readerThread = new Thread(new IncomingReader());
		readerThread.start();

		frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
		frame.setSize(400, 500);
		frame.setVisible(true);

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

//	public class SendButtonListener implements ActionListener {
//		public void actionPerformed(ActionEvent ev) {
//			try {
//				writer.println(outgoing.getText());
//				writer.flush();
//			} catch (Exception ex) {
//				ex.printStackTrace();
//			}
//			outgoing.setText("");
//			outgoing.requestFocus();
//		}
//
//	}

	public class IncomingReader implements Runnable {
		public void run() {
			String message;
			try {
				while ((message= reader.readLine()) != null) {
					System.out.println("read " + message);
					//SimpleChatClient.textArea.appendText(message + "\n");
				}
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}


}
