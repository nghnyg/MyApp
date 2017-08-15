package nagihan.myapp.models;


public class BaseServiceResponse {

    private String isSuccess;
    private Integer errorCode;
    private String message;

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getIsSuccess() {
        return Boolean.parseBoolean(isSuccess);
    }

    public void setIsSucces(String isSucces) {
        this.isSuccess = isSuccess;
    };

}
