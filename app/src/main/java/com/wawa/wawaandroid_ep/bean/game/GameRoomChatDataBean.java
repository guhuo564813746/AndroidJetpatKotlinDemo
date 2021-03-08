package com.wawa.wawaandroid_ep.bean.game;

import java.util.List;

/**
 * 作者：create by 张金 on 2021/3/5 18:25
 * 邮箱：564813746@qq.com
 */
public class GameRoomChatDataBean {

    /**
     * user_id : 1234
     * user_nickname : 玩家1
     * msg_list : [{"type":1,"timestamp":1611398160,"body":{"text":"hello world"}},{"type":1,"timestamp":1611398165,"body":{"text":"hello robot!"}}]
     */
    public static final int CONTENT_TEXT=1;
    private int user_id;
    private String user_nickname;
    private List<MsgListBean> msg_list;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public List<MsgListBean> getMsg_list() {
        return msg_list;
    }

    public void setMsg_list(List<MsgListBean> msg_list) {
        this.msg_list = msg_list;
    }

    public static class MsgListBean {
        /**
         * type : 1
         * timestamp : 1611398160
         * body : {"text":"hello world"}
         */

        private int type;
        private int timestamp;
        private BodyBean body;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(int timestamp) {
            this.timestamp = timestamp;
        }

        public BodyBean getBody() {
            return body;
        }

        public void setBody(BodyBean body) {
            this.body = body;
        }

        public static class BodyBean {
            /**
             * text : hello world
             */

            private String text;

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }
        }
    }
}
