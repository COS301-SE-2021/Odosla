package cs.superleague.delivery;

import cs.superleague.delivery.exceptions.DeliveryDoesNotExistException;
import cs.superleague.delivery.exceptions.DeliveryException;
import cs.superleague.delivery.exceptions.InvalidRequestException;
import cs.superleague.delivery.requests.*;
import cs.superleague.delivery.responses.*;

import java.net.URISyntaxException;

public interface DeliveryService {

    AddDeliveryDetailResponse addDeliveryDetail(AddDeliveryDetailRequest request) throws InvalidRequestException;

    AssignDriverToDeliveryResponse assignDriverToDelivery(AssignDriverToDeliveryRequest request) throws InvalidRequestException, URISyntaxException;

    CreateDeliveryResponse createDelivery(CreateDeliveryRequest request) throws InvalidRequestException, URISyntaxException;

    GetDeliveriesResponse getDeliveries(GetDeliveriesRequest request) throws InvalidRequestException;

    GetDeliveryCostResponse getDeliveryCost(GetDeliveryCostRequest request) throws InvalidRequestException;

    GetDeliveryDetailResponse getDeliveryDetail(GetDeliveryDetailRequest request) throws InvalidRequestException;

    GetDeliveryStatusResponse getDeliveryStatus(GetDeliveryStatusRequest request) throws InvalidRequestException;

    GetNextOrderForDriverResponse getNextOrderForDriver(GetNextOrderForDriverRequest request) throws InvalidRequestException;

    RemoveDriverFromDeliveryResponse removeDriverFromDelivery(RemoveDriverFromDeliveryRequest request) throws InvalidRequestException, URISyntaxException;

    TrackDeliveryResponse trackDelivery(TrackDeliveryRequest request) throws InvalidRequestException, URISyntaxException;

    UpdateDeliveryStatusResponse updateDeliveryStatus(UpdateDeliveryStatusRequest request) throws InvalidRequestException, URISyntaxException;

    GetDeliveryDriverByOrderIDResponse getDeliveryDriverByOrderID(GetDeliveryDriverByOrderIDRequest request) throws InvalidRequestException, URISyntaxException;

    GetDeliveryByUUIDResponse getDeliveryByUUID(GetDeliveryByUUIDRequest request) throws DeliveryException;

    GetAdditionalStoresDeliveryCostResponse getAdditionalStoresDeliveryCost(GetAdditionalStoresDeliveryCostRequest request) throws InvalidRequestException, DeliveryDoesNotExistException, URISyntaxException;
}
