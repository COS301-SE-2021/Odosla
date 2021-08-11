package cs.superleague.delivery;

import cs.superleague.delivery.exceptions.InvalidRequestException;
import cs.superleague.delivery.requests.*;
import cs.superleague.delivery.responses.*;

public interface DeliveryService {

    AddDeliveryDetailResponse addDeliveryDetail(AddDeliveryDetailRequest request) throws InvalidRequestException;

    AssignDriverToDeliveryResponse assignDriverToDelivery(AssignDriverToDeliveryRequest request) throws InvalidRequestException;

    CreateDeliveryResponse createDelivery(CreateDeliveryRequest request) throws InvalidRequestException;

    GetDeliveriesResponse getDeliveries(GetDeliveriesRequest request) throws InvalidRequestException;

    GetDeliveryCostResponse getDeliveryCost(GetDeliveryCostRequest request) throws InvalidRequestException;

    GetDeliveryDetailResponse getDeliveryDetail(GetDeliveryDetailRequest request) throws InvalidRequestException;

    GetDeliveryStatusResponse getDeliveryStatus(GetDeliveryStatusRequest request) throws InvalidRequestException;

    GetNextOrderForDriverResponse getNextOrderForDriver(GetNextOrderForDriverRequest request) throws InvalidRequestException;

    RemoveDriverFromDeliveryResponse removeDriverFromDelivery(RemoveDriverFromDeliveryRequest request) throws InvalidRequestException;

    TrackDeliveryResponse trackDelivery(TrackDeliveryRequest request) throws InvalidRequestException;

    UpdateDeliveryStatusResponse updateDeliveryStatus(UpdateDeliveryStatusRequest request) throws InvalidRequestException;
}
