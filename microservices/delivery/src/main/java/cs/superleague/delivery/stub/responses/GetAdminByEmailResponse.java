package cs.superleague.delivery.stub.responses;

import cs.superleague.delivery.stub.dataclass.Admin;

public class GetAdminByEmailResponse {
    private final Admin admin;
    private final boolean success;

    public GetAdminByEmailResponse() {
        this.admin = null;
        this.success = false;
    }

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
