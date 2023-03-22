/*
 * @author yangyang
 * @date 2023/3/19 13:35
 * @概要：
 *      描述
 */
module com.yang.javafx {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.yang.javafx.controller to javafx.fxml;
    exports com.yang.javafx.system to javafx.graphics;
}