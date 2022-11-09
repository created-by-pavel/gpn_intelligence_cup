package ru.crbpavel.vk.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@EqualsAndHashCode
@Getter
public class UserGroupForm {
    @JsonProperty("user_id")
    @NonNull
    private Integer userId;
    @JsonProperty("group_id")
    @NonNull
    private String groupId;

    @JsonCreator
    public UserGroupForm(
            @NonNull @JsonProperty("user_id") Integer userId,
            @NonNull @JsonProperty("group_id") String groupId
    ) {
        this.userId = userId;
        this.groupId = groupId;
    }
}
