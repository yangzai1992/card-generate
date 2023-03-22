package com.yang.javafx.controller;

import com.yang.javafx.entity.AreaObj;
import com.yang.javafx.util.IdCardGenerator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainPanelController implements Initializable {
    @FXML
    private Button getBtn;
    @FXML
    private Button aboutBtn;
    @FXML
    private Button explainBtn;
    @FXML
    private TextArea text;
    @FXML
    private TextField card;
    @FXML
    private TextArea cardText;
    @FXML
    public ToggleGroup sexToggleGroup;
    @FXML
    public ComboBox provinceComboBox;
    @FXML
    public ComboBox cityComboBox;
    @FXML
    public ComboBox areaComboBox;
    @FXML
    public DatePicker birthDatePicker;

    private Integer provinceCode;
    private Integer cityCode;
    private Integer areaCode;
    private String gend;

    private List<AreaObj> provinceObjs = new ArrayList<>();
    private List<AreaObj> cityObjs = new ArrayList<>();
    private List<AreaObj> areaObjs = new ArrayList<>();

    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
    private StringConverter dateConverter = new StringConverter<LocalDate>() {
        @Override
        public String toString(LocalDate date) {
            return date != null ?  date.format(dateFormatter) : "";
        }

        @Override
        public LocalDate fromString(String string) {
            if (string != null && !string.isEmpty()) {
                return LocalDate.parse(string, dateFormatter);
            }
            return null;
        }
    };

    private void intAreaData() {
        //设置可见行数, 超过显示滚动条
        provinceComboBox.setVisibleRowCount(15);
        cityComboBox.setVisibleRowCount(10);
        areaComboBox.setVisibleRowCount(15);
        for (Integer key : IdCardGenerator.areaCode.keySet()) {
            AreaObj areaObj = new AreaObj(key, IdCardGenerator.areaCode.get(key));
            if (areaObj.getType() == 1) {
                provinceObjs.add(areaObj);
            }
            if (areaObj.getType() == 2) {
                cityObjs.add(areaObj);
            }
            if (areaObj.getType() == 3) {
                areaObjs.add(areaObj);
            }
        }
        provinceObjs = provinceObjs.stream().sorted(Comparator.comparing(AreaObj::getCode)).collect(Collectors.toList());
        cityObjs = cityObjs.stream().sorted(Comparator.comparing(AreaObj::getCode)).collect(Collectors.toList());
        areaObjs = areaObjs.stream().sorted(Comparator.comparing(AreaObj::getCode)).collect(Collectors.toList());

        //初始化省以及自治区
        provinceComboBox.getItems().setAll(provinceObjs);
        provinceComboBox.getSelectionModel().selectFirst();
        provinceCode = ((AreaObj) provinceComboBox.getSelectionModel().getSelectedItem()).getCode();
        //初始化城市
        List<AreaObj> tempCityList = cityObjs.stream().filter(it -> provinceCode.equals(it.getParentCode())).collect(Collectors.toList());
        cityComboBox.getItems().setAll(tempCityList);
        cityComboBox.getSelectionModel().selectFirst();
        cityCode = tempCityList.get(0).getCode();
        //初始化县
        List<AreaObj> tempAreaList = areaObjs.stream().filter(it -> cityCode.equals(it.getParentCode())).collect(Collectors.toList());
        areaComboBox.getItems().setAll(tempAreaList);
        areaComboBox.getSelectionModel().selectFirst();
        areaCode = tempAreaList.get(0).getCode();
    }


    private IdCardGenerator idCardGenerator = new IdCardGenerator();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //初始化出生地
        intAreaData();
        //添加地区选择框事件监听
        addComboBoxChangeEventHandler();
        //设置默认出生日期
        birthDatePicker.setValue(LocalDate.now());
        birthDatePicker.setConverter(dateConverter);
        //birthDatePicker.setEditable(false);
        birthDatePicker.getEditor().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("event" + event);
            }
        });

        //设置性别监听事件
        sexToggleGroup.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> observableValue, Toggle oldValue, Toggle newValue) -> {
            RadioButton radioButton = (RadioButton) newValue;
            if (radioButton != null) {
                gend = radioButton.getText();
            }
        });
        gend = ((RadioButton) sexToggleGroup.getSelectedToggle()).getText();
    }


    public void addComboBoxChangeEventHandler(){
        StringConverter stringConverter = new StringConverter<AreaObj>() {
            @Override
            public String toString(AreaObj object) {
                if (object == null) {
                    return "";
                }
                return object.getName();
            }
            @Override
            public AreaObj fromString(String string) {
                return null;
            }
        };
        provinceComboBox.setConverter(stringConverter);
        cityComboBox.setConverter(stringConverter);
        areaComboBox.setConverter(stringConverter);

        provinceComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<AreaObj>() {
            @Override
            public void changed(ObservableValue<? extends AreaObj> observable, AreaObj oldValue, AreaObj newValue) {
                if (provinceCode != null || provinceCode != newValue.getCode()) {
                    provinceCode = newValue.getCode();
                    List<AreaObj> tempCityList = cityObjs.stream().filter(it -> provinceCode.equals(it.getParentCode())).collect(Collectors.toList());
                    cityComboBox.getItems().setAll(tempCityList);
                    if (cityComboBox.getSelectionModel().getSelectedItem() != null) {
                        cityCode = ((AreaObj) cityComboBox.getSelectionModel().getSelectedItem()).getCode();
                    }
                    if (cityCode != null) {
                        areaComboBox.getSelectionModel().selectFirst();
                        List<AreaObj> tempAreaList = areaObjs.stream().filter(it -> cityCode.equals(it.getParentCode())).collect(Collectors.toList());
                        areaComboBox.getItems().setAll(tempAreaList);
                    }
                    cityComboBox.getSelectionModel().selectFirst();
                    areaComboBox.getSelectionModel().selectFirst();
                }
            }
        });

        cityComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<AreaObj>() {
            @Override
            public void changed(ObservableValue<? extends AreaObj> observable, AreaObj oldValue, AreaObj newValue) {
                if (newValue != null) {
                    if (cityCode == null || cityCode != newValue.getCode()) {
                        cityCode = newValue.getCode();
                        List<AreaObj> tempAreaList = areaObjs.stream().filter(it -> cityCode.equals(it.getParentCode())).collect(Collectors.toList());
                        areaComboBox.getItems().setAll(tempAreaList);
                        areaComboBox.getSelectionModel().selectFirst();
                    }
                }
            }
        });
        areaComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<AreaObj>() {
            @Override
            public void changed(ObservableValue<? extends AreaObj> observable, AreaObj oldValue, AreaObj newValue) {
                if (newValue != null) {
                    areaCode = newValue.getCode();
                }
            }
        });
    }

    // 添加初始化事件
    public void generateCardActionEventHandler(ActionEvent actionEvent) {
        //解决手动输入时间的问题
        try{
            birthDatePicker.setValue(LocalDate.parse(birthDatePicker.getEditor().getCharacters().toString(), dateFormatter));
        }catch (Exception e){
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            StringBuilder generater = new StringBuilder();
            generater.append(areaCode);
            generater.append(birthDatePicker.getValue().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
            String randomCode = idCardGenerator.randomCode();
            while (true) {
                if (Integer.parseInt(randomCode) % 2 == 0 && "女".equals(gend)) {
                    break;
                }
                if (Integer.parseInt(randomCode) % 2 == 1 && "男".equals(gend)) {
                    break;
                }
                randomCode = idCardGenerator.randomCode();
            }
            generater.append(randomCode);
            generater.append(idCardGenerator.calcTrailingNumber(generater.toString().toCharArray()));
            stringBuilder.append(generater.toString());
            stringBuilder.append("\n");
        }
        // 将值赋予标签显示
        text.setText(stringBuilder.toString());
    }

    public void explainActionEventHandler(ActionEvent actionEvent) {
        String cardStr = card.getCharacters().toString();
        boolean b = IdCardGenerator.isCard(cardStr);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("校验结果：" + (b ? "正确\n" : "错误\n"));

        if (b) {
            stringBuffer.append("出  生 地：");
            stringBuffer.append(IdCardGenerator.areaCode.get(Integer.parseInt(cardStr.substring(0, 2) + "0000")));
            stringBuffer.append(IdCardGenerator.areaCode.get(Integer.parseInt(cardStr.substring(0, 4) + "00")));
            stringBuffer.append(IdCardGenerator.areaCode.get(Integer.parseInt(cardStr.substring(0, 6))) + "\n");
            stringBuffer.append("出生年月：" + cardStr.substring(6, 10) + "年" + cardStr.substring(10, 12) + "月" + cardStr.substring(12, 14) + "日\n");
            stringBuffer.append("性      别：" + (Integer.parseInt(cardStr.substring(16, 17)) % 2 == 0 ? "女" : "男"));
        }
        // 将值赋予标签显示
        cardText.setText(stringBuffer.toString());
    }

    public void aboutActionEventHandler(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("关于 大陆身份证号码生成器");
        alert.setHeaderText(null);
        alert.setContentText("本程序仅限娱乐，欢迎各位交流！");
        alert.showAndWait();
    }

}
