package xyz.zimuju.common.rx;

public class RxResponseException extends Exception {

    private RxError rxError = new RxError();

    public RxResponseException(int code) {
        rxError.setCode(code);
        rxError.setMsg(getErrorMsg(code));
    }

    public RxError getRxError() {
        return rxError;
    }


    String getErrorMsg(int code) {
        String msg = "";

        if (code == 999) {
            msg = "无数据";
        } else if (code == 1007) {
            msg = "账号或密码错误，请重新填写";
        } else if (code == 1006) {
            msg = "账号不存在";
        } else if (code == 1000) {
            msg = "鉴权失败";
        } else if (code == 1001) {
            msg = "数据库异常";
        } else if (code == 1002) {
            msg = "客户端数据异常";
        } else if (code == 1007) {
            msg = "密码错误";
        } else if (code == 8000) {
            msg = "后台维护";
        } else if (code == 1020) {
            msg = "强制下线";
        } else if (code == 9000) {
            msg = "其他错误";
        }

        return msg;
    }

}
