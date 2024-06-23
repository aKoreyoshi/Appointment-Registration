package com.mac.ghpt.model.enums;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月10日, 16:39:17
 */
public enum DictEnum {
    CERTIFICATES_TYPE("CertificatesType", "证件类型"),
    PROVINCE("Province", "省份"),
    NATION("Nation", "民族"),
    ;

    private String dictCode;
    private String msg;

    DictEnum(String dictCode, String msg) {
        this.dictCode = dictCode;
        this.msg = msg;
    }

    public String getDictCode() {
        return dictCode;
    }

    public void setDictCode(String dictCode) {
        this.dictCode = dictCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}