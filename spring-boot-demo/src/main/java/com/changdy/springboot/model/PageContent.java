package com.changdy.springboot.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by Changdy on 2018/2/2.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageContent<T> {
    private Integer recordSize;
    private List<T> list;
}