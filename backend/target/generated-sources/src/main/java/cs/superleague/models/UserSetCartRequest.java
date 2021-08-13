package cs.superleague.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * This object is expected as input
 */
@ApiModel(description = "This object is expected as input")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-08-13T15:49:46.017974400+02:00[Africa/Harare]")
public class UserSetCartRequest   {
  @JsonProperty("customerID")
  private String customerID = null;

  @JsonProperty("barcodes")
  private List barcodes = null;

  public UserSetCartRequest customerID(String customerID) {
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

  public UserSetCartRequest barcodes(List barcodes) {
    this.barcodes = barcodes;
    return this;
  }

  /**
   * Get barcodes
   * @return barcodes
  **/
  @ApiModelProperty(value = "")
  
    @Valid
    public List getBarcodes() {
    return barcodes;
  }

  public void setBarcodes(List barcodes) {
    this.barcodes = barcodes;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserSetCartRequest userSetCartRequest = (UserSetCartRequest) o;
    return Objects.equals(this.customerID, userSetCartRequest.customerID) &&
        Objects.equals(this.barcodes, userSetCartRequest.barcodes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(customerID, barcodes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserSetCartRequest {\n");
    
    sb.append("    customerID: ").append(toIndentedString(customerID)).append("\n");
    sb.append("    barcodes: ").append(toIndentedString(barcodes)).append("\n");
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
