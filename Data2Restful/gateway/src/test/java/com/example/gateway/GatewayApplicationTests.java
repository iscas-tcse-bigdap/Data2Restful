package com.example.gateway;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.example.gateway.config.RefreshableRateLimitRules;
import com.example.gateway.pojo.Group;
import com.example.gateway.pojo.RateLimit;
import com.example.gateway.service.RateLimitService;
import com.example.gateway.utils.BeanUtil;
import com.example.gateway.utils.Response;
import com.google.common.net.HttpHeaders;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@SpringBootTest
@Slf4j
class GatewayApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void feignTest() throws ExecutionException, InterruptedException {
        Mono<Group> monoInfo = WebClient.builder().build().get()
            .uri(uriBuilder -> uriBuilder.scheme("http").host("localhost").port(8081).path("/api/svc/group/groupByName").queryParam("groupName", "group1").build())
            .header(HttpHeaders.CONTENT_TYPE, "application/json").retrieve().bodyToMono(Group.class);
        CompletableFuture<Group> voidCompletableFuture = CompletableFuture.supplyAsync(monoInfo::block);
        Group groupInfo = voidCompletableFuture.get();
        System.out.println(groupInfo);
    }

    @Test
    void webClientTest() throws InterruptedException {
        AtomicReference<Group> group = new AtomicReference<>(new Group());
        ParameterizedTypeReference<Group> GroupTypeRef = new ParameterizedTypeReference<Group>() {
        };
        Mono<Group> groupMono = webClientGet(URI.create("localhost:9001/api/svc/group/groupByName?groupName=" + "group1"), GroupTypeRef);
        groupMono.subscribe(group::set);
        Thread.sleep(2000);
        System.out.println(group.get());
    }

    private static <T> Mono<T> webClientGet(URI uri, ParameterizedTypeReference<T> responseType) {
        return WebClient.builder().build().get().uri(uri)
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .retrieve()
            .bodyToMono(responseType);
    }

    @Test
    void getRateLimit() {
        RateLimitService rateLimitBean = BeanUtil.getBean(RateLimitService.class);
        List<RateLimit> rateLimitList = rateLimitBean.getRateLimit();
        System.out.println(rateLimitList);
        List<FlowRule> rules = rateLimitList.stream().map(rateLimit -> {
            FlowRule flowRule = new FlowRule();
            flowRule.setResource("/svc/" + rateLimit.getName() + "/**");
            flowRule.setCount(rateLimit.getRateLimit());
            flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);      // 限流模式为 QPS
            flowRule.setStrategy(RuleConstant.STRATEGY_DIRECT);   // 直接拒绝超过阈值的请求
            flowRule.setMaxQueueingTimeMs(0);     // 最大等待排队时间
            return flowRule;
        }).collect(Collectors.toList());
        log.info("正在进行流控规则配置 -> 当前流控规则为:{}", Arrays.toString(rules.toArray()));
    }

    @SneakyThrows
    @Test
    void test2() {
        ParameterizedTypeReference<Response> ResponseTypeRef = new ParameterizedTypeReference<Response>() {
        };
        Mono<Response> aclMono = webClientGet(URI.create("localhost:8081/api/svc/group/acl?groupId=" + 8), ResponseTypeRef);
        Response aclRes = aclMono.block();
        System.out.println(aclRes.getData().get("acl"));
        System.out.println(aclRes.getData().get("acl") == null);
    }

    @Test
    void fuck() {
        Object res = Mono.error(new RuntimeException("runtime err"))
            .onErrorReturn(RuntimeException.class, 1)
            .onErrorReturn(IOException.class, 2)
            .block();
        System.out.println(res);
    }
}
