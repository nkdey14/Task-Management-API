package com.task_management_api.integration;

public class JsonPathUtils {

    // Very small helper to extract "id" field value from a JSON object string.
    // This is intentionally simple for tests and assumes the response contains "id":"..."
    public static String extractId(String json) {
        if (json == null) return null;
        int idx = json.indexOf("\"id\"");
        if (idx == -1) return null;
        int colon = json.indexOf(':', idx);
        if (colon == -1) return null;
        int firstQuote = json.indexOf('"', colon);
        int secondQuote = json.indexOf('"', firstQuote + 1);
        if (firstQuote == -1 || secondQuote == -1) return null;
        return json.substring(firstQuote + 1, secondQuote);
    }
}

