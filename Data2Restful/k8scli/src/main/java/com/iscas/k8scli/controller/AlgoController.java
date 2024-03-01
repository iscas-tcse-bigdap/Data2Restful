package com.iscas.k8scli.controller;

import com.iscas.k8scli.response.Result;
import com.iscas.k8scli.services.algoService.AlgoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: AlgoController
 * @Description:
 * @Author: wzc
 * @Date: 2023/4/21 14:29
 */
@RestController
@RequestMapping(value = "/api/algo")
public final class AlgoController {

    @Autowired
    private AlgoService k8sService;

    @GetMapping(value = "/public")
    public Result publicAlgo(@RequestParam("type")String type,
                             @RequestParam("code")String code) {

        Result result = k8sService.publicAlgo(type, code);
        return result;
    }

    @GetMapping(value = "/public2")
    public Result publicAlgo2(@RequestParam("type")String type,
                             @RequestParam("code")String code) {

        Result result = k8sService.publicAlgo(type, code);
        return result;
    }
}
