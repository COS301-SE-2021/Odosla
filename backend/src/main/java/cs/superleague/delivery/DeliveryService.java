package cs.superleague.delivery;

import cs.superleague.delivery.exceptions.InvalidRequestException;
import cs.superleague.delivery.requests.*;
import cs.superleague.delivery.responses.*;
import cs.superleague.payment.exceptions.PaymentException;

public interface DeliveryService {

    AddDeliveryDetailResponse addDeliveryDetail(AddDeliveryDetailRequest request) throws InvalidRequestException;

    AssignDriverToDeliveryResponse assignDriverToDelivery(AssignDriverToDeliveryRequest request) throws InvalidRequestException, cs.superleague.user.exceptions.InvalidRequestException, PaymentException;

    CreateDeliveryResponse createDelivery(CreateDeliveryRequest request) throws InvalidRequestException;

    GetDeliveriesResponse getDeliveries(GetDeliveriesRequest request) throws InvalidRequestException;

    GetDeliveryCostResponse getDeliveryCost(GetDeliveryCostRequest request) throws InvalidRequestException;

    GetDeliveryDetailResponse getDeliveryDetail(GetDeliveryDetailRequest request) throws InvalidRequestException;

    GetDeliveryStatusResponse getDeliveryStatus(GetDeliveryStatusRequest request) throws InvalidRequestException;

    GetNextOrderForDriverResponse getNextOrderForDriver(GetNextOrderForDriverRequest request) throws InvalidRequestException, cs.superleague.user.exceptions.InvalidRequestException;

    RemoveDriverFromDeliveryResponse removeDriverFromDelivery(RemoveDriverFromDeliveryRequest request) throws InvalidRequestException, PaymentException;

    TrackDeliveryResponse trackDelivery(TrackDeliveryRequest request) throws InvalidRequestException;

    UpdateDeliveryStatusResponse updateDeliveryStatus(UpdateDeliveryStatusRequest request) throws InvalidRequestException, PaymentException;

    GetDeliveryDriverByOrderIDResponse getDeliveryDriverByOrderID(GetDeliveryDriverByOrderIDRequest request) throws InvalidRequestException;
}
