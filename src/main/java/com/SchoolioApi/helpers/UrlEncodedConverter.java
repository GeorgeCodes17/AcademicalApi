package com.SchoolioApi.helpers;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class UrlEncodedConverter {
    public Map<String,String> convert(String urlEncoded) {
        Map<String,String> accountMap = new HashMap<>();
        String encodedParams = URLDecoder.decode(urlEncoded, StandardCharsets.UTF_8);

        String[] params = encodedParams.split("&");
        for (String param : params) {
            String[] keyValue = param.split("=");
            accountMap.put(keyValue[0], keyValue[1]);
        }
        return accountMap;
    }
}
