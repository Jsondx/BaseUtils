package com.ldx.baseutils.ui.bean;

/**
 * Created by babieta on 2018/12/7.
 */

public class Code {

    /**
     * status : 1
     * msg : 2677
     * data : {"token":"121f640295e6b95b57959cf590dee994","uid":"10b37fd02427bc30aaf17698445745fe"}
     */

    private String status;
    private int msg;
    private DataBean data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getMsg() {
        return msg;
    }

    public void setMsg(int msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * token : 121f640295e6b95b57959cf590dee994
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
