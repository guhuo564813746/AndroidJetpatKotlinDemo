package com.wawa.wawaandroid_ep.bean.game;

import java.util.List;

/**
 * 作者：create by 张金 on 2021/3/1 16:03
 * 邮箱：564813746@qq.com
 */

@lombok.NoArgsConstructor
@lombok.Data
public class GameRoomUsers {

    /**
     * user_total : 1
     * user_list : [{"user_id":134,"nickname":"秋天","avatar":"https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1529136762&di=4158dcfe546e094ba085a5e0d2ed247a&src=http://img.zcool.cn/community/01460b57e4a6fa0000012e7ed75e83.png"}]
     * player : {"user_id":134,"nickname":"秋天","avatar":"https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1529136762&di=4158dcfe546e094ba085a5e0d2ed247a&src=http://img.zcool.cn/community/01460b57e4a6fa0000012e7ed75e83.png"}
     * queue : {"queue_total":5}
     */

    public Integer user_total;
    public List<UserListBean> user_list;
    public PlayerBean player;
    public QueueBean queue;

    @lombok.NoArgsConstructor
    @lombok.Data
    public static class PlayerBean {
        /**
         * user_id : 134
         * nickname : 秋天
         * avatar : https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1529136762&di=4158dcfe546e094ba085a5e0d2ed247a&src=http://img.zcool.cn/community/01460b57e4a6fa0000012e7ed75e83.png
         */

        public Integer user_id;
        public String nickname;
        public String avatar;
    }

    @lombok.NoArgsConstructor
    @lombok.Data
    public static class QueueBean {
        /**
         * queue_total : 5
         */

        public Integer queue_total;
    }

    @lombok.NoArgsConstructor
    @lombok.Data
    public static class UserListBean {
        /**
         * user_id : 134
         * nickname : 秋天
         * avatar : https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1529136762&di=4158dcfe546e094ba085a5e0d2ed247a&src=http://img.zcool.cn/community/01460b57e4a6fa0000012e7ed75e83.png
         */

        public Integer user_id;
        public String nickname;
        public String avatar;
    }
}
