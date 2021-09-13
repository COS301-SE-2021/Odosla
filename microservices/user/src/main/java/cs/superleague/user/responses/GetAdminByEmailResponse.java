package cs.superleague.user.responses;

import cs.superleague.user.dataclass.Admin;

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
