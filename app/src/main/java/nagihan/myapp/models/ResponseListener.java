package nagihan.myapp.models;

public interface ResponseListener<T> {
    void onSuccess(T obj);
    void onError(int errorCode);
}
