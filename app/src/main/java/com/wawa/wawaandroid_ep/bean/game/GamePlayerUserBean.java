package com.wawa.wawaandroid_ep.bean.game;

/**
 * 作者：create by 张金 on 2021/7/9 18:18
 * 邮箱：564813746@qq.com
 */
public class GamePlayerUserBean {

    /**
     * user_id : 10
     * nickname : 张金6
     * avatar : http://robot.t.seafarer.me/public/upload/images/avatar/2021/03/15/2fd7f47156820f8d46fab1e2a69a9549.jpg
     */

    private int user_id;
    private String nickname;
    private String avatar;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
