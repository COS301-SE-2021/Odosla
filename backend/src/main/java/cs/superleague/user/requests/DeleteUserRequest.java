package cs.superleague.user.requests;

import java.util.UUID;

public class DeleteUserRequest {

    private UUID userId;

    public DeleteUserRequest(UUID userId) {
        this.userId = userId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
