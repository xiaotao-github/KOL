package com.xigua.dto;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

@Component
public class Game {
    //游戏ID
    String id;
    //游戏名称
    String game_name;
    //游戏CP
    String game_cp;
    //版本号
    String version_number;
    //游戏简介
    String game_introduction;
    //游戏截图
    String game_image;
    //位置
    int position;
    //状态
    int state;
    //评分
    BigDecimal score;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGame_name() {
        return game_name;
    }

    public void setGame_name(String game_name) {
        this.game_name = game_name;
    }

    public String getGame_cp() {
        return game_cp;
    }

    public void setGame_cp(String game_cp) {
        this.game_cp = game_cp;
    }

    public String getVersion_number() {
        return version_number;
    }

    public void setVersion_number(String version_number) {
        this.version_number = version_number;
    }

    public String getGame_introduction() {
        return game_introduction;
    }

    public void setGame_introduction(String game_introduction) {
        this.game_introduction = game_introduction;
    }

    public String getGame_image() {
        return game_image;
    }

    public void setGame_image(String game_image) {
        this.game_image = game_image;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id='" + id + '\'' +
                ", game_name='" + game_name + '\'' +
                ", game_cp='" + game_cp + '\'' +
                ", version_number='" + version_number + '\'' +
                ", game_introduction='" + game_introduction + '\'' +
                ", game_image='" + game_image + '\'' +
                ", position=" + position +
                ", state=" + state +
                ", score=" + score +
                '}';
    }
}
