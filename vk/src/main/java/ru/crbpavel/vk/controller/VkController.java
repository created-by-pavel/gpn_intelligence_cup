package ru.crbpavel.vk.controller;

import com.vk.api.sdk.objects.users.responses.GetResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.crbpavel.vk.model.UserGroupForm;
import ru.crbpavel.vk.model.VkUserInfo;
import ru.crbpavel.vk.service.VkService;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@AllArgsConstructor
public class VkController {
    private final VkService vkService;

    @GetMapping(path = "/getUserInfo")
    public ResponseEntity<VkUserInfo> getVkUserInfo(@RequestBody UserGroupForm userGroupForm) throws Throwable {
        log.info("getting info about vkUser: {}", userGroupForm.getUserId());
        CompletableFuture<GetResponse> userInfo = vkService.getVkUserInfo(userGroupForm);
        CompletableFuture<Boolean> isMember = vkService.isMember(userGroupForm);

        var vkUserInfo = new VkUserInfo(userInfo.get().getLastName(), userInfo.get().getFirstName(), userInfo.get().getNickname(), isMember.get());
        return ResponseEntity.ok(vkUserInfo);
    }
}
