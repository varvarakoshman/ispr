package com.isp.dto;

public class CDSHooksCardsGeneral {
    private static int questionnaireId = 0;
    private static int linkId = 0;

    public static String stringQuestionnaireId() {
        return String.format("qId%s", String.valueOf(questionnaireId++));
    }

    public static String stringLinkId() {
        // TODO atomic variable
        return String.format("lId%s", String.valueOf(linkId++));
    }
}