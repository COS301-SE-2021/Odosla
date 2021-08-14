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
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-08-13T18:57:36.804039800+02:00[Africa/Harare]")
public class UserMakeGroceryListRequest   {
  @JsonProperty("customerID")
  private String customerID = null;

  @JsonProperty("barcodes")
  private List barcodes = null;

  @JsonProperty("name")
  private String name = null;

  public UserMakeGroceryListRequest customerID(String customerID) {
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

  public UserMakeGroceryListRequest barcodes(List barcodes) {
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

  public UserMakeGroceryListRequest name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
  **/
  @ApiModelProperty(value = "")
  
    public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserMakeGroceryListRequest userMakeGroceryListRequest = (UserMakeGroceryListRequest) o;
    return Objects.equals(this.customerID, userMakeGroceryListRequest.customerID) &&
        Objects.equals(this.barcodes, userMakeGroceryListRequest.barcodes) &&
        Objects.equals(this.name, userMakeGroceryListRequest.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(customerID, barcodes, name);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserMakeGroceryListRequest {\n");
    
    sb.append("    customerID: ").append(toIndentedString(customerID)).append("\n");
    sb.append("    barcodes: ").append(toIndentedString(barcodes)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
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
