package com.yang.javafx.entity;


public class AreaObj {
    /**
     * 1省级 2市级 3县区
     */
    private int type;
    private int code;
    private String name;
    /**
     * 上级行政区划编号
     */
    private Integer parentCode;

    public AreaObj() {
    }

    public AreaObj(int code, String name) {
        //1省级 2市级 3县区
        if (String.valueOf(code).endsWith("0000")) {
            this.type = 1;
        } else if (String.valueOf(code).endsWith("00")) {
            this.type = 2;
            this.parentCode = Integer.parseInt(String.valueOf(code).substring(0,2) + "0000");
        }else{
            this.type = 3;
            this.parentCode = Integer.parseInt(String.valueOf(code).substring(0,4) + "00");
        }
        this.code = code;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentCode() {
        return parentCode;
    }

    public void setParentCode(Integer parentCode) {
        this.parentCode = parentCode;
    }

    @Override
    public String toString() {
        return "AreaObj{" +
                "type=" + type +
                ", code=" + code +
                ", name='" + name + '\'' +
                ", parentCode=" + parentCode +
                '}';
    }
}
