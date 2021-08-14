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
 * Generic schema for a store
 */
@ApiModel(description = "Generic schema for a store")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-08-14T15:42:59.236683100+02:00[Africa/Harare]")
public class StoreObject   {
  @JsonProperty("storeID")
  private String storeID = null;

  @JsonProperty("storeBrand")
  private String storeBrand = null;

  @JsonProperty("isOpen")
  private Boolean isOpen = null;

  @JsonProperty("maxShoppers")
  private Integer maxShoppers = null;

  @JsonProperty("maxOrders")
  private Integer maxOrders = null;

  @JsonProperty("openingTime")
  private Integer openingTime = null;

  @JsonProperty("closingTime")
  private Integer closingTime = null;

  public StoreObject storeID(String storeID) {
    this.storeID = storeID;
    return this;
  }

  /**
   * Get storeID
   * @return storeID
  **/
  @ApiModelProperty(value = "")
  
    public String getStoreID() {
    return storeID;
  }

  public void setStoreID(String storeID) {
    this.storeID = storeID;
  }

  public StoreObject storeBrand(String storeBrand) {
    this.storeBrand = storeBrand;
    return this;
  }

  /**
   * Get storeBrand
   * @return storeBrand
  **/
  @ApiModelProperty(value = "")
  
    public String getStoreBrand() {
    return storeBrand;
  }

  public void setStoreBrand(String storeBrand) {
    this.storeBrand = storeBrand;
  }

  public StoreObject isOpen(Boolean isOpen) {
    this.isOpen = isOpen;
    return this;
  }

  /**
   * Get isOpen
   * @return isOpen
  **/
  @ApiModelProperty(value = "")
  
    public Boolean isIsOpen() {
    return isOpen;
  }

  public void setIsOpen(Boolean isOpen) {
    this.isOpen = isOpen;
  }

  public StoreObject maxShoppers(Integer maxShoppers) {
    this.maxShoppers = maxShoppers;
    return this;
  }

  /**
   * Get maxShoppers
   * @return maxShoppers
  **/
  @ApiModelProperty(value = "")
  
    public Integer getMaxShoppers() {
    return maxShoppers;
  }

  public void setMaxShoppers(Integer maxShoppers) {
    this.maxShoppers = maxShoppers;
  }

  public StoreObject maxOrders(Integer maxOrders) {
    this.maxOrders = maxOrders;
    return this;
  }

  /**
   * Get maxOrders
   * @return maxOrders
  **/
  @ApiModelProperty(value = "")
  
    public Integer getMaxOrders() {
    return maxOrders;
  }

  public void setMaxOrders(Integer maxOrders) {
    this.maxOrders = maxOrders;
  }

  public StoreObject openingTime(Integer openingTime) {
    this.openingTime = openingTime;
    return this;
  }

  /**
   * Get openingTime
   * @return openingTime
  **/
  @ApiModelProperty(value = "")
  
    public Integer getOpeningTime() {
    return openingTime;
  }

  public void setOpeningTime(Integer openingTime) {
    this.openingTime = openingTime;
  }

  public StoreObject closingTime(Integer closingTime) {
    this.closingTime = closingTime;
    return this;
  }

  /**
   * Get closingTime
   * @return closingTime
  **/
  @ApiModelProperty(value = "")
  
    public Integer getClosingTime() {
    return closingTime;
  }

  public void setClosingTime(Integer closingTime) {
    this.closingTime = closingTime;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StoreObject storeObject = (StoreObject) o;
    return Objects.equals(this.storeID, storeObject.storeID) &&
        Objects.equals(this.storeBrand, storeObject.storeBrand) &&
        Objects.equals(this.isOpen, storeObject.isOpen) &&
        Objects.equals(this.maxShoppers, storeObject.maxShoppers) &&
        Objects.equals(this.maxOrders, storeObject.maxOrders) &&
        Objects.equals(this.openingTime, storeObject.openingTime) &&
        Objects.equals(this.closingTime, storeObject.closingTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(storeID, storeBrand, isOpen, maxShoppers, maxOrders, openingTime, closingTime);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StoreObject {\n");
    
    sb.append("    storeID: ").append(toIndentedString(storeID)).append("\n");
    sb.append("    storeBrand: ").append(toIndentedString(storeBrand)).append("\n");
    sb.append("    isOpen: ").append(toIndentedString(isOpen)).append("\n");
    sb.append("    maxShoppers: ").append(toIndentedString(maxShoppers)).append("\n");
    sb.append("    maxOrders: ").append(toIndentedString(maxOrders)).append("\n");
    sb.append("    openingTime: ").append(toIndentedString(openingTime)).append("\n");
    sb.append("    closingTime: ").append(toIndentedString(closingTime)).append("\n");
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
