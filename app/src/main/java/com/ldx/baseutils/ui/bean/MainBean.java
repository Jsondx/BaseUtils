package com.ldx.baseutils.ui.bean;

/**
 * Created by babieta on 2018/12/4.
 */

public class MainBean {

    /**
     * msg : 5616
     * status : 1
     * data : {"token":"36815801d226adfd38cd4e1600184d5f","uid":"10b37fd02427bc30aaf17698445745fe"}
     */

    private int msg;
    private String status;
    private DataBean data;

    public int getMsg() {
        return msg;
    }

    public void setMsg(int msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * token : 36815801d226adfd38cd4e1600184d5f
         * uid : 10b37fd02427bc30aaf17698445745fe
         */

        private String token;
        private String uid;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }
    }
}
