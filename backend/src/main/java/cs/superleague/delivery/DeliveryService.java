package cs.superleague.delivery;

import cs.superleague.delivery.exceptions.InvalidRequestException;
import cs.superleague.delivery.requests.*;
import cs.superleague.delivery.responses.*;

public interface DeliveryService {

    AddDeliveryDetailResponse addDeliveryDetail(AddDeliveryDetailRequest request);

    CreateDeliveryResponse createDelivery(CreateDeliveryRequest request) throws InvalidRequestException;

    GetDeliveriesResponse getDeliveries(GetDeliveriesRequest request);

    GetDeliveryCostResponse getDeliveryCost(GetDeliveryCostRequest request);

    GetDeliveryDetailResponse getDeliveryDetail(GetDeliveryDetailRequest request);

    GetDeliveryStatusResponse getDeliveryStatus(GetDeliveryStatusRequest request);

    TrackDeliveryResponse trackDelivery(TrackDeliveryRequest request);

    UpdateDeliveryStatusResponse updateDeliveryStatus(UpdateDeliveryStatusRequest request);
}
