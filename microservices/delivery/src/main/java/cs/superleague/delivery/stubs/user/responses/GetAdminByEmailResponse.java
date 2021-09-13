package cs.superleague.delivery.stubs.user.responses;

import cs.superleague.delivery.stubs.user.dataclass.Admin;

public class GetAdminByEmailResponse {
    private final Admin admin;
    private final boolean success;

    public GetAdminByEmailResponse(Admin admin, boolean success) {
        this.admin = admin;
        this.success = success;
    }

    public Admin getAdmin() {
        return admin;
    }

    public boolean isSuccess() {
        return success;
    }
}
