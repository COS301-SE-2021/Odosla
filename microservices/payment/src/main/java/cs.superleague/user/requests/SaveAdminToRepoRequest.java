package cs.superleague.user.requests;

import cs.superleague.user.dataclass.Admin;

import java.io.Serializable;

public class SaveAdminToRepoRequest implements Serializable {
    private Admin admin;

    public SaveAdminToRepoRequest(Admin admin) {
        this.admin = admin;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
}
