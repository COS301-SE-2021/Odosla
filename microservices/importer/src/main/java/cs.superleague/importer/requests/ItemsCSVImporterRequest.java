package cs.superleague.importer.requests;

public class ItemsCSVImporterRequest {

    private String file;

    public ItemsCSVImporterRequest(String file) {
        this.file = file;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

}
