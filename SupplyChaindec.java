package com.example.supplychaindec;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class SupplyChain extends Application {

    public static final int width = 700, height = 600, headerBar = 50;
    Pane bodypane = new Pane();
    public static int bodyWidth, bodyHeight;
    Login login = new Login();

    ProductDetails productDetails = new ProductDetails();
    Button globalLogin;
    Label customerEmailLabel = null;
    String customerEmail = null;

    private GridPane headerBar() {
        TextField searchText = new TextField();
        Button searchButton = new Button("Search");

        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String productName = searchText.getText();

                //clear body amd put this new in the body
                bodypane.getChildren().clear();
                bodypane.getChildren().add(productDetails.getProductsByName(productName));
            }
        });
        globalLogin = new Button("Log In");
        globalLogin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                bodypane.getChildren().clear();
                bodypane.getChildren().add(loginPage());
                globalLogin.setDisable(true);

            }
        });

        customerEmailLabel = new Label("Welcome User");

        GridPane gridPane = new GridPane();
        gridPane.setMinSize(bodypane.getMinWidth(), headerBar - 10);
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setStyle("-fx-background-color: #C0C0C0");
        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(searchText, 0, 0);
        gridPane.add(searchButton, 1, 0);
        gridPane.add(globalLogin, 2, 0);
        gridPane.add(customerEmailLabel, 3, 0);

        return gridPane;
    }

    private GridPane loginPage() {
        Label emailLabel = new Label("Email");
        Label passwordLabel = new Label("Password");
        Label messageLabel = new Label("I am message");

        Button loginButton = new Button("Login");
        TextField emailTextField = new TextField();
        PasswordField passwordField = new PasswordField();

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String email = emailTextField.getText();
                String password = passwordField.getText();
                // messageLabel.setText(email + " $$ " + password);

                if (login.customerLogin(email, password)) {
                    messageLabel.setText("Login Successful");
                    customerEmail = email;
                    globalLogin.setDisable(true);
                    customerEmailLabel.setText("Welcome : " + customerEmail);
                    bodypane.getChildren().add(productDetails.getAllProducts());
                } else {
                    messageLabel.setText("Login Failed");
                }
            }
        });

        GridPane gridPane = new GridPane();
        gridPane.setMinSize(bodypane.getMinWidth(), bodypane.getMinHeight());
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setStyle("-fx-background-color: #C0C0C0");

        gridPane.setAlignment(Pos.CENTER);
        //first is x and second y
        gridPane.add(emailLabel, 0, 0);
        gridPane.add(emailTextField, 1, 0);
        gridPane.add(passwordLabel, 0, 1);
        gridPane.add(passwordField, 1, 1);
        gridPane.add(loginButton, 0, 2);
        gridPane.add(messageLabel, 1, 2);

        return gridPane;
    }

    private GridPane footerBar() {

        Button addToCartButton = new Button("Add To Cart");
        Button buyNowButton = new Button("Buy Now");
        Label messageLabel = new Label("");
        buyNowButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Product selectedProduct = productDetails.getSelectedProduct();
                if(Order.placeOrder(customerEmail, selectedProduct)){
                    messageLabel.setText("Ordered");

                }
                else{
                    messageLabel.setText("Order Fail");
                }
            }
        });


        GridPane gridPane = new GridPane();
        gridPane.setMinSize(bodypane.getMinWidth(), headerBar - 10);
        gridPane.setVgap(5);
        gridPane.setHgap(50);
        gridPane.setStyle("-fx-background-color: #C0C0C0");
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setTranslateY(headerBar + height + 5);

        gridPane.add(addToCartButton, 0, 0);
        gridPane.add(buyNowButton, 1, 0);
        gridPane.add(messageLabel, 2, 0);


        return gridPane;
    }
       private Pane createContent(){
        Pane root = new Pane();
        root.setPrefSize(width, height+2*headerBar+10);

        bodypane.setMinSize(width,height);
        bodypane.setTranslateY(headerBar);
        bodypane.getChildren().addAll(productDetails.getAllProducts());

        root.getChildren().addAll(headerBar(), bodypane, footerBar());

        return root;
    }
    @Override
    public void start(Stage stage) throws IOException {
        //FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(createContent());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
