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
 * This object is output
 */
@ApiModel(description = "This object is output")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-08-14T15:01:46.157134600+02:00[Africa/Harare]")
public class ShoppingAddShopperResponse   {
  @JsonProperty("success")
  private Boolean success = null;

  @JsonProperty("date")
  private String date = null;

  @JsonProperty("message")
  private String message = null;

  public ShoppingAddShopperResponse success(Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * generated token used to identify the caller of the endpoint
   * @return success
  **/
  @ApiModelProperty(value = "generated token used to identify the caller of the endpoint")
  
    public Boolean isSuccess() {
    return success;
  }

  public void setSuccess(Boolean success) {
    this.success = success;
  }

  public ShoppingAddShopperResponse date(String date) {
    this.date = date;
    return this;
  }

  /**
   * Get date
   * @return date
  **/
  @ApiModelProperty(value = "")
  
    public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public ShoppingAddShopperResponse message(String message) {
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
    ShoppingAddShopperResponse shoppingAddShopperResponse = (ShoppingAddShopperResponse) o;
    return Objects.equals(this.success, shoppingAddShopperResponse.success) &&
        Objects.equals(this.date, shoppingAddShopperResponse.date) &&
        Objects.equals(this.message, shoppingAddShopperResponse.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, date, message);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShoppingAddShopperResponse {\n");
    
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    date: ").append(toIndentedString(date)).append("\n");
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
