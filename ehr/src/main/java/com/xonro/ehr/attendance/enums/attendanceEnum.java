package com.xonro.ehr.attendance.enums;

/**
 * 考勤管理枚举
 * @author haolh
 * @date 2018-2-1
 */
public enum attendanceEnum {
    test("type001", "", "保荐人键值");

    private String code;
    private String value;
    private String desc;
    attendanceEnum(String code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
