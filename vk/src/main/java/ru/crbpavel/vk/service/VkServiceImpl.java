package ru.crbpavel.vk.service;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.users.responses.GetResponse;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.crbpavel.vk.exception.VkException;
import ru.crbpavel.vk.model.UserGroupForm;

import java.util.concurrent.CompletableFuture;


@Service
@AllArgsConstructor
public class VkServiceImpl implements VkService {
    private VkApiClient vk;
    private ServiceActor actor;

    @Cacheable(value = "vkUserInfosCache")
    @Async
    public CompletableFuture<GetResponse> getVkUserInfo(UserGroupForm userGroupForm) throws VkException {
        try {
            var userInfo = vk.users().get(actor).userIds(userGroupForm.getUserId().toString()).execute();
            if (userInfo.isEmpty()) {
                throw new VkException("User not found, try to write correct user_id");
            }
            return CompletableFuture.completedFuture(userInfo.get(0));
        } catch (ApiException | ClientException e) {
            throw new VkException(e.getMessage());
        }
    }

    @Cacheable(value = "isMembersCache")
    @Async
    public CompletableFuture<Boolean> isMember(UserGroupForm userGroupForm) throws VkException {
        try {
            var isMember = vk.groups().isMember(actor, userGroupForm.getGroupId()).userId(userGroupForm.getUserId()).execute();
            return CompletableFuture.completedFuture(isMember.getValue().equals("1"));
        } catch (ApiException | ClientException e) {
            throw new VkException(e.getMessage());
        }
    }
}
