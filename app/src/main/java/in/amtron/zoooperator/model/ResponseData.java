package in.amtron.zoooperator.model;


public class ResponseData<T> {

    private String status;
    private T data;
    private String msg;
    private int code;

    public ResponseData(String status, T data, String msg, int code) {
        this.status = status;
        this.data = data;
        this.msg = msg;
        this.code = code;
    }


    public String getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }
}
