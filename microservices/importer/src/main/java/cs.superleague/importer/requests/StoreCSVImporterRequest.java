package cs.superleague.importer.requests;

public class StoreCSVImporterRequest {

    private String file;

    public StoreCSVImporterRequest(String file) {
        this.file = file;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

}
