package com.example.testprojecttui.dto;

import lombok.Data;

@Data
public class Branch {
    private String name;
    private Commit commit;
}
