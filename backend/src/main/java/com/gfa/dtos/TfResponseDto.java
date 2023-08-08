package com.gfa.dtos;

import java.util.List;

public class TfResponseDto {
    private final List<TfDto> instances;

    public TfResponseDto(List<TfDto> instances) {
        this.instances = instances;
    }

    public List<TfDto> getInstances() {
        return instances;
    }
}
