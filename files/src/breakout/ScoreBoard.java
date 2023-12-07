package breakout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//This class will handle the user login
//Rank will display the data from all players' record


public class ScoreBoard extends JPanel{
	//The username that used to login
	private String username;
	private String password;
	private boolean isLogged;
	private int score;
	private String[] scoreRank;
	
	private static final String BASE_URL = "http://localhost:8090";
	
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton loginButton;
	private JButton logoutButton;

	private JPanel loginPanel;
	
	private HttpClient httpClient;

	
	public ScoreBoard() {
		username = "Not logged in";
		isLogged = false;
		score = 0;
		scoreRank = new String[]{"Player1: 100", "Player2: 80", "Player3: 50"};
		
		setPreferredSize(new Dimension(300, 600));
		
		// Initialize login components
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");
        
        // Set layout manager
        setLayout(new BorderLayout());
        
        // Create login panel
        loginPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        
        
        
        loginPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));
        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(usernameField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passwordField);
        loginPanel.add(new JLabel(""));
        loginPanel.add(loginButton);
        
        System.out.println(loginPanel.getSize());
        // Add login panel to the center of the ScoreBoard
        add(loginPanel, BorderLayout.SOUTH);
        
        
        // Add action listener to the login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	//System.out.println("clicked");
                username = usernameField.getText();
                password = String.valueOf(passwordField.getPassword());
                String status = loggin(username, password);
                updateLoginPanel(status);
                repaint();  
            }
        });
	}
	
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the background image
        drawBackgroundImage(g);

        // Draw score and other information
        g.setColor(Color.RED);
        g.setFont(new Font("SansSerif", Font.PLAIN, 20));
        
        //if not logged, ask user to login
        g.drawString("High Score board ", 60, 100);

        if (!isLogged) {
        	String[] loginInfo = {
                    "Please log in",
                    "Or Your Score",
                    "would not be saved",
                    "New users will",
                    "automatically register",
                    "if the username",
                    "has not been taken"
            };
        	// Draw each line of login information
            for (int i = 0; i < loginInfo.length; i++) {
                g.drawString(loginInfo[i], 30, 160 + i * 30);
            }
        } else {
        	// If logged in, display score rank
        	//call to fetch high score from server
        	fetchScoreRank();
            for (int i = 0; i < scoreRank.length; i++) {
                g.drawString(scoreRank[i], 20, 150 + i * 30);
            }
            
        }
        
        //repaint();
        //drawRanks(g);
    }
	
	public void updateLoginPanel(String status) {
		if(status.equals("LOGIN_SUCCESS") || status.equals("REGIS_SUCCESS")) {
			isLogged = true;
			loginPanel.removeAll();
			//loginPanel.setLayout(new GridLayout(1, 2, 10, 10));
	        loginPanel.add(new JLabel(username));
	        
	        logoutButton = new JButton("Logout");
	        logoutButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	//System.out.println("clicked");
	                usernameField.setText("");
	                passwordField.setText("");
	                username = usernameField.getText();
	                password = String.valueOf(passwordField.getPassword());
	                updateLogoutPanel();
	                repaint();  
	            }
	        });
	        loginPanel.add(logoutButton);
		} else if(status.equals("TIMEOUT")) {
			JOptionPane.showMessageDialog(null, "Server is not responding");
		} else if(status.equals("WRONG")) {
			JOptionPane.showMessageDialog(null, "Wrong username or password! Please try again");
		} else if(status.equals("EMPTY")) {
			JOptionPane.showMessageDialog(null, "Please fill in username/password to login in!");
		}
    
        loginPanel.revalidate();
        loginPanel.repaint();
	}
	
	public void updateLogoutPanel() {
		loginPanel.removeAll();
		//loginPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));
        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(usernameField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passwordField);
        loginPanel.add(new JLabel(""));
        loginPanel.add(loginButton);
        isLogged = false;
		//JOptionPane.showMessageDialog(null, "Logged out!");
		loginPanel.revalidate();
        loginPanel.repaint();
	}
	
	public String loggin(String username, String password) {
		String endpointUrl = BASE_URL + "/gameusers/loginUser";
		
		String jsonBody = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);
		// Create an instance of HttpClient
        httpClient = HttpClient.newHttpClient();

        // Create an HttpRequest with the endpoint URL and set the HTTP method to POST
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpointUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                .build();
		
        try {
            // Send the request and retrieve the response
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Print the response status code and body
            System.out.println("Response Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());
            String responseBody = response.body();
            
            if ("LOGIN_SUCCESS".equals(responseBody)) {
                return "LOGIN_SUCCESS";
            } else if ("REGIS_SUCCESS".equals(responseBody)){
            	return "REGIS_SUCCESS";
            } else if ("WRONG".equals(responseBody)) {
                return "WRONG";
            } else if("EMPTY".equals(responseBody)){
            	return "EMPTY";
            } else {
                return "TIMEOUT";
            }
    		
        } catch (Exception e) {
            //e.printStackTrace();
            return "TIMEOUT";
        }
        
        //call fecth
	}
	
	private void drawBackgroundImage(Graphics g) {
	    // Load the background image
		try {
	        Image backgroundImage = ImageIO.read(new File("src/breakout/1.jpg"));
	        // Scale the image to fit the panel
	        Image scaledImage = backgroundImage.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
	        // Draw the scaled image
	        g.drawImage(scaledImage, 0, 0, this);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	} 
	
	public void fetchScoreRank() {
		//this function will ask server to get a list of all current high score
		//then it will sort by desc locally, pick the highest 5 scores. update the scoreRank
		// String[] scoreRank
		String endpointUrl = BASE_URL + "/gameusers/getRank";
		
		// Create an instance of HttpClient
        httpClient = HttpClient.newHttpClient();

        // Create an HttpRequest with the endpoint URL and set the HTTP method to POST
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpointUrl))
                .header("Content-Type", "application/json")
                .GET()
                .build();
		
        try {
            // Send the request and retrieve the response
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Print the response status code and body
            System.out.println("Response Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());
            String responseBody = response.body();
            
            scoreRank = parser(responseBody);
            System.out.println(scoreRank); 
    		
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
		
	}
	
	public String[] parser(String body) {
		Pattern pattern = Pattern.compile("score='(\\d+)'.*?player=(\\w+)");
        Matcher matcher = pattern.matcher(body);
        Map<String, Integer> playerScores = new HashMap<>();

        while (matcher.find()) {
        	String player = matcher.group(2);
            int score = Integer.parseInt(matcher.group(1));
            System.out.println("player: " + player);
            System.out.println("score "+ score);
            playerScores.put(player, Math.max(playerScores.getOrDefault(player, 0), score));
        }

        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(playerScores.entrySet());
        sortedEntries.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));

        String[] scoreRank = new String[sortedEntries.size()];
        for (int i = 0; i < sortedEntries.size(); i++) {
            scoreRank[i] = String.format("\"%s: %s\"", sortedEntries.get(i).getKey(), sortedEntries.get(i).getValue());
        }

        return scoreRank;
	}
	
	public void uploadScore(boolean status, int score) {
		String code = status ? "SUCCESS" : "FAILED";
		String endpointUrl = BASE_URL + "/gameusers/saveHistory";
		
		String jsonBody = String.format("{\"username\":\"%s\",\"status\":\"%s\",\"score\":%d}", username, code, score);

		// Create an instance of HttpClient
        httpClient = HttpClient.newHttpClient();

        // Create an HttpRequest with the endpoint URL and set the HTTP method to POST
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpointUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                .build();
		
        try {
        	// Send the request and retrieve the response
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Print the response status code and body
            System.out.println("Response Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());
            
    		
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        //after uploading, make a fectch to server
        fetchScoreRank();
        repaint();
	}
}
