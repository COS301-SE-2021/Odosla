package cs.superleague.delivery.stub.responses;

import cs.superleague.delivery.stub.dataclass.Admin;

public class FindAdminByEmailResponse {

    private final Admin admin;

    public FindAdminByEmailResponse() {
        this.admin = null;
    }

    public FindAdminByEmailResponse(Admin admin) {
        this.admin = admin;
    }

    public Admin getAdmin() {
        return admin;
    }
}
