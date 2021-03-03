package com.wawa.baselib.utils.enumpk;

public enum LanguageType {

    CHINESE("zh-CN"),
    ENGLISH("en"),
    THAILAND("zh-TW"),
    HONGKONG("zh-HK"),
    SPAIN("es"),
    JAPAN("ja"),
    MALAY("ms"),
    THAI("th"),
    INDIA("inc"),
    INNID("in"),
    KH("km"),
    VN("vi");

    private String language;

    LanguageType(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language == null ? "" : language;
    }
}