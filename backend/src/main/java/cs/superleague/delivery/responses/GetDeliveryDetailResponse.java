package cs.superleague.delivery.responses;

import cs.superleague.delivery.dataclass.DeliveryDetail;

import java.util.List;

public class GetDeliveryDetailResponse {
    private String message;
    private List<DeliveryDetail> detail;

    public GetDeliveryDetailResponse(String message, List<DeliveryDetail> detail) {
        this.message = message;
        this.detail = detail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DeliveryDetail> getDetail() {
        return detail;
    }

    public void setDetail(List<DeliveryDetail> detail) {
        this.detail = detail;
    }
}
