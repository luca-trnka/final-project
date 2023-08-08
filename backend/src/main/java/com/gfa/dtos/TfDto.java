package com.gfa.dtos;

public class TfDto {
    private final Long id;
    private final String name;
    private final String region;
    private final String os;
    private final String size;

    public TfDto(Long id, String name, String region, String os, String size) {
        this.id = id;
        this.name = name;
        this.region = region;
        this.os = os;
        this.size = size;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRegion() {
        return region;
    }

    public String getOs() {
        return os;
    }

    public String getSize() {
        return size;
    }
}
