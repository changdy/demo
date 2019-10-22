package com.changdy.springboot.mapper;

import com.changdy.springboot.entity.Player;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by Changdy on 2019/9/8.
 */
@Mapper
public interface TestResultMap {

    // 这个是使用逗号分隔
    List<Player> getPlayerList();

    // 使用Association ,构建了内部的关系
    List<Player> getPlayerWithAssociation();

    // 使用Association ,构建了内部的关系
    List<Player> getPlayerSwords();

    // 使用select ,构建了内部的关系
    List<Player> getPlayerSwordsSelect();
}
