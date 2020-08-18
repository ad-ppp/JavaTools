package com.jacky.tool.jenkins;

import java.util.List;

/**
 * Created by Jacky on 2020/8/15
 */
public final class Configuration {
    /**
     * name : android.test.blackboard.release.multijob
     * params : [{"key":"x_branch","value":"v566"},{"key":"m_branch","value":"v566"}]
     * local : {"user":"jacky","token":"123","url":"xiaoheiban"}
     * showAvailableJobs : false
     */

    private String name;
    private LocalBean local;
    private boolean showAvailableJobs;
    private List<ParamsBean> params;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalBean getLocal() {
        return local;
    }

    public void setLocal(LocalBean local) {
        this.local = local;
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

    public static class LocalBean {
        /**
         * user : jacky
         * token : 123
         * url : xiaoheiban
         */

        private String user;
        private String token;
        private String url;

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class ParamsBean {
        /**
         * key : x_branch
         * value : v566
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
