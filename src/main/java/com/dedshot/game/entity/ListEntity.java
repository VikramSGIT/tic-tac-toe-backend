package com.dedshot.game.entity;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ListEntity<T> {
    private int count;
    private List<T> data;
    
    public ListEntity(List<T> object) {
        data = object;
        count = object.size();
    }
}
