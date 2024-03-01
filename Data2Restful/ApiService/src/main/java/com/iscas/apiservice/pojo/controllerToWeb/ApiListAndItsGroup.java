package com.iscas.apiservice.pojo.controllerToWeb;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Set;

@Data
@NoArgsConstructor
public class ApiListAndItsGroup {
    private ArrayList<ApiOutline> apiOutlines;
    private Set<String> groups;

    public ApiListAndItsGroup(ArrayList<ApiOutline> outlines, Set<String> groupSet) {
        this.apiOutlines = outlines;
        this.groups = groupSet;
    }
}
