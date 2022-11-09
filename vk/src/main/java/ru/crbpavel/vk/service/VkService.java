package ru.crbpavel.vk.service;

import com.vk.api.sdk.objects.users.responses.GetResponse;
import ru.crbpavel.vk.exception.VkException;
import ru.crbpavel.vk.model.UserGroupForm;

import java.util.concurrent.CompletableFuture;

public interface VkService {
    CompletableFuture<GetResponse> getVkUserInfo(UserGroupForm userGroupForm) throws VkException;

    CompletableFuture<Boolean> isMember(UserGroupForm userGroupForm) throws VkException;
}
