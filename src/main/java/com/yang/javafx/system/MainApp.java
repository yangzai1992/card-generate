package com.yang.javafx.system;

import com.yang.javafx.MainApplication;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;

/*
 * @author yangyang
 * @date 2023/3/21 9:07
 * @概要：
 *      描述
 */
public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // 获取 Maven 项目 resources/fxml/fxml_demo.fxml 注意，无法识别横向，例如 fxml-demo.fxml

        URL resource = MainApplication.class.getResource("fxml/mainPanel.fxml");
        if (resource == null) {
            throw new RuntimeException("未找到fxml资源");
        }
        // 此时需要注意， fxml里最外层标签是 AnchorPane 故使用AnchorPane对象获取变量
        AnchorPane anchorPane = FXMLLoader.load(resource);
        // 将 AnchorPane 加入到场景
        stage.setScene(new Scene(anchorPane));
        stage.setTitle("大陆身份证号码生成器");
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }

}

