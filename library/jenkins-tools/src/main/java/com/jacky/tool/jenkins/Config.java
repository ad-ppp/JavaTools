package com.jacky.tool.jenkins;

import java.util.List;

/**
 * Created by Jacky on 2020/8/15
 */
public final class Config {

    /**
     * name : 123
     * params : [{"key":"123","value":"222"}]
     * showAvailableJobs : false
     */

    private String name;
    private boolean showAvailableJobs;
    private List<ParamsBean> params;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isShowAvailableJobs() {
        return showAvailableJobs;
    }

    public void setShowAvailableJobs(boolean showAvailableJobs) {
        this.showAvailableJobs = showAvailableJobs;
    }

    public List<ParamsBean> getParams() {
        return params;
    }

    public void setParams(List<ParamsBean> params) {
        this.params = params;
    }

    public static class ParamsBean {
        /**
         * key : 123
         * value : 222
         */

        private String key;
        private String value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
