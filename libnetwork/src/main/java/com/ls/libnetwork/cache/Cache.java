package com.ls.libnetwork.cache;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

/**
 * 序列化
 */
@Entity(tableName = "cache")
public class Cache implements Serializable {

    @NonNull
    @PrimaryKey
    public String key;

    public byte[] data;

}
