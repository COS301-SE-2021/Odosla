package cs.superleague.notifications.requests;

public class SendPDFEmailRequest {
    private String email;
    private byte [] PDF;

    public SendPDFEmailRequest() {
    }

    public SendPDFEmailRequest(String email, byte[] PDF) {
        this.email = email;
        this.PDF = PDF;
    }

    public String getEmail() {
        return email;
    }

    public byte[] getPDF() {
        return PDF;
    }
}
