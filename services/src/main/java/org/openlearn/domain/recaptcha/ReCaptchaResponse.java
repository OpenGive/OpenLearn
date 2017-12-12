package org.openlearn.domain.recaptcha;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class ReCaptchaResponse {

    private Boolean success;

    @JsonProperty("error-codes")
    private ArrayList<String> errorCodes;

    public Boolean isSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public ArrayList<String> getErrorCodes() {
        return errorCodes;
    }

    public void setErrorCodes(ArrayList<String> errorCodes) {
        this.errorCodes = errorCodes;
    }

    @Override
    public String toString() {
        return "ReCaptchaResponse{" +
            "success=" + success +
            ", errorCodes=" + errorCodes +
            '}';
    }

}
