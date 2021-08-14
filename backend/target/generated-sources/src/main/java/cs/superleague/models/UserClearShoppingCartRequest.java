package cs.superleague.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * This object is expected as input
 */
@ApiModel(description = "This object is expected as input")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-08-14T17:26:44.499818600+02:00[Africa/Harare]")
public class UserClearShoppingCartRequest   {
  @JsonProperty("customerID")
  private String customerID = null;

  public UserClearShoppingCartRequest customerID(String customerID) {
    this.customerID = customerID;
    return this;
  }

  /**
   * generated token used to identify the caller of the endpoint
   * @return customerID
  **/
  @ApiModelProperty(value = "generated token used to identify the caller of the endpoint")
  
    public String getCustomerID() {
    return customerID;
  }

  public void setCustomerID(String customerID) {
    this.customerID = customerID;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserClearShoppingCartRequest userClearShoppingCartRequest = (UserClearShoppingCartRequest) o;
    return Objects.equals(this.customerID, userClearShoppingCartRequest.customerID);
  }

  @Override
  public int hashCode() {
    return Objects.hash(customerID);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserClearShoppingCartRequest {\n");
    
    sb.append("    customerID: ").append(toIndentedString(customerID)).append("\n");
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
