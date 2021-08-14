/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.18).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package cs.superleague.api;

import cs.superleague.models.NotificationSendDirectEmailNotificationRequest;
import cs.superleague.models.NotificationSendDirectEmailNotificationResponse;
import cs.superleague.models.NotificationSendEmailNotificationRequest;
import cs.superleague.models.NotificationSendEmailNotificationResponse;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.CookieValue;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-08-14T15:42:59.236683100+02:00[Africa/Harare]")
@Api(value = "notification", description = "the notification API")
public interface NotificationApi {

    @ApiOperation(value = "Endpoint for sending a direct email notification", nickname = "sendDirectEmailNotification", notes = "Refer to summary", response = NotificationSendDirectEmailNotificationResponse.class, tags={ "notification", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Returns whether the email was sent", response = NotificationSendDirectEmailNotificationResponse.class) })
    @RequestMapping(value = "/notification/sendDirectEmailNotification",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<NotificationSendDirectEmailNotificationResponse> sendDirectEmailNotification(@ApiParam(value = "The input body required by this request" ,required=true )  @Valid @RequestBody NotificationSendDirectEmailNotificationRequest body
);


    @ApiOperation(value = "Endpoint for sending an email notification", nickname = "sendEmailNotification", notes = "Refer to summary", response = NotificationSendEmailNotificationResponse.class, tags={ "notification", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Returns whether the email was sent", response = NotificationSendEmailNotificationResponse.class) })
    @RequestMapping(value = "/notification/sendEmailNotification",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<NotificationSendEmailNotificationResponse> sendEmailNotification(@ApiParam(value = "The input body required by this request" ,required=true )  @Valid @RequestBody NotificationSendEmailNotificationRequest body
);

}
