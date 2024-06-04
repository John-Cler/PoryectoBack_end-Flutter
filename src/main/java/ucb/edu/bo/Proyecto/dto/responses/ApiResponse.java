package ucb.edu.bo.Proyecto.dto.responses;

public class ApiResponse<T> {
    private int status;
    private boolean success;
    private String message;
    private T data;


    public ApiResponse() {
        this.status = 500;
        this.success = false;
        this.message = "Error interno del servidor";
        this.data = null;
    }

    public ApiResponse(boolean success, String message) {
        this.status = 200;
        this.success = success;
        this.message = message;
        this.data = null;
    }

    public ApiResponse(boolean success, String message, T data) {
        this.status = 200;
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public ApiResponse(boolean success, String message, T data, int status) {
        this.status = 200;
        this.success = success;
        this.message = message;
        this.data = data;
    }



    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
