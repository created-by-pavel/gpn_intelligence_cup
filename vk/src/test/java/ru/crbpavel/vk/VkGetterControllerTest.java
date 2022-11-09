package ru.crbpavel.vk;

import com.vk.api.sdk.objects.users.responses.GetResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.http.ResponseEntity;
import ru.crbpavel.vk.controller.VkController;
import ru.crbpavel.vk.model.UserGroupForm;
import ru.crbpavel.vk.model.VkUserInfo;
import ru.crbpavel.vk.service.VkServiceImpl;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class VkGetterControllerTest {
    @InjectMocks
    private VkController vkController;

    private VkServiceImpl vkService;

    @BeforeEach
    void setUp() {
        vkService = mock(VkServiceImpl.class);
        vkController = new VkController(vkService);
    }

    @Test
    public void getVkUserInfo() throws Throwable {
        UserGroupForm userGroupForm = new UserGroupForm(78385, "93559769");
        GetResponse getResponse = new GetResponse();
        getResponse.setFirstName("Валерий");
        getResponse.setLastName("Акинцев");
        getResponse.setNickname(null);
        when(vkService.getVkUserInfo(userGroupForm)).thenReturn(CompletableFuture.completedFuture(getResponse));
        when(vkService.isMember(userGroupForm)).thenReturn(CompletableFuture.completedFuture(true));

        VkUserInfo expectedResult = new VkUserInfo(getResponse.getLastName(), getResponse.getFirstName(), getResponse.getNickname(), true);
        ResponseEntity<VkUserInfo> result = vkController.getVkUserInfo(userGroupForm);

        assertEquals(expectedResult, result.getBody());
    }
}