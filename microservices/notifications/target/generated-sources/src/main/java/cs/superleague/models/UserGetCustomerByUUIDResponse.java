package cs.superleague.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import cs.superleague.models.CustomerObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * This object is output
 */
@ApiModel(description = "This object is output")
@Validated
public class UserGetCustomerByUUIDResponse   {
  @JsonProperty("customer")
  private CustomerObject customer = null;

  @JsonProperty("timestamp")
  private String timestamp = null;

  @JsonProperty("message")
  private String message = null;

  public UserGetCustomerByUUIDResponse customer(CustomerObject customer) {
    this.customer = customer;
    return this;
  }

  /**
   * Get customer
   * @return customer
  **/
  @ApiModelProperty(value = "")
  
    @Valid
    public CustomerObject getCustomer() {
    return customer;
  }

  public void setCustomer(CustomerObject customer) {
    this.customer = customer;
  }

  public UserGetCustomerByUUIDResponse timestamp(String timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
  **/
  @ApiModelProperty(value = "")
  
    public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public UserGetCustomerByUUIDResponse message(String message) {
    this.message = message;
    return this;
  }

  /**
   * Get message
   * @return message
  **/
  @ApiModelProperty(value = "")
  
    public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserGetCustomerByUUIDResponse userGetCustomerByUUIDResponse = (UserGetCustomerByUUIDResponse) o;
    return Objects.equals(this.customer, userGetCustomerByUUIDResponse.customer) &&
        Objects.equals(this.timestamp, userGetCustomerByUUIDResponse.timestamp) &&
        Objects.equals(this.message, userGetCustomerByUUIDResponse.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(customer, timestamp, message);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserGetCustomerByUUIDResponse {\n");
    
    sb.append("    customer: ").append(toIndentedString(customer)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
