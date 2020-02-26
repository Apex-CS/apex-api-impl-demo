package com.fis.api.product.BPPRetrieveProductDetail;

import java.util.HashMap;
import java.util.Map;

public class MessageOnlyErrorFlow {
    public static final String ERROR_CHANNEL_BEAN_NAME = "api.error.channel";

    public static Map<String, String> ERROR_MAPPING = new HashMap<String, String>(){
        {
            put("PRODAPI-FAT-0022", "PRODAPI-FAT-0022 - SOR Validation Error.");
            put("PRODAPI-VAL-0009", "PRODAPI-VAL-0009 - Invalid Value Passed. Transaction cannot be completed.");
            put("PRODAPI-VAL-0019", "PRODAPI-VAL-0019 - Value entered must be numeric");
            put("PRODAPI-VAL-0021", "PRODAPI-VAL-0021 - Failed to parse the request. Invalid datatype.");
            put("PRODAPI-VAL-0041", "PRODAPI-VAL-0041 - Maximum field length validation Error");
            put("PRODAPI-VAL-0042", "PRODAPI-VAL-0042 - Invalid value for the selected product. Transaction cannot be completed");
            put("PRODAPI-VAL-0043", "PRODAPI-VAL-0043 - Field must not be empty, blank or zeros");
            put("PRODAPI-VAL-0011", "PRODAPI-VAL-0011 - Missing Required fields");
            put("PRODAPI-VAL-0044", "PRODAPI-VAL-0044 - Data entered exeeds maximum. Default assumed.");
            put("PRODAPI-VAL-0045", "PRODAPI-VAL-0045 - At least one search criteria is required.");
            put("PRODAPI-VAL-0046", "PRODAPI-VAL-0046 - Changes not allowed to Pending approval product");
            put("PRODAPI-VAL-0047", "PRODAPI-VAL-0047 - Data entered is not supported.Transaction cannot be completed.");
            put("PRODAPI-VAL-0048", "PRODAPI-VAL-0048 - Product restrictions. Transaction cannot be completed.");
            put("PRODAPI-SYS-0001", "PRODAPI-SYS-0001 - System Error");
        }
    };
}
