package com.gfa.dtos;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class TfRequestDto {
    @Digits(integer = Short.MAX_VALUE, fraction = 0)
    private final short count;
    @NotBlank
    @Pattern(regexp = "^[a-z]{2}-[a-z]{4,9}-\\d$")
    private final String region;
    @NotBlank
    private final String os;
    @NotBlank
    private final String size;

    public TfRequestDto(short count, String region, String os, String size) {
        this.count = count;
        this.region = region;
        this.os = os;
        this.size = size;
    }

    public int getCount() {
        return count;
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
