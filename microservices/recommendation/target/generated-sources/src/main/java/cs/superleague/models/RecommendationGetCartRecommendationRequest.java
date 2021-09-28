package cs.superleague.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * This object is expected as input
 */
@ApiModel(description = "This object is expected as input")
@Validated
public class RecommendationGetCartRecommendationRequest   {
  @JsonProperty("itemIDs")
  @Valid
  private List<String> itemIDs = null;

  public RecommendationGetCartRecommendationRequest itemIDs(List<String> itemIDs) {
    this.itemIDs = itemIDs;
    return this;
  }

  public RecommendationGetCartRecommendationRequest addItemIDsItem(String itemIDsItem) {
    if (this.itemIDs == null) {
      this.itemIDs = new ArrayList<String>();
    }
    this.itemIDs.add(itemIDsItem);
    return this;
  }

  /**
   * Get itemIDs
   * @return itemIDs
  **/
  @ApiModelProperty(value = "")
  
    public List<String> getItemIDs() {
    return itemIDs;
  }

  public void setItemIDs(List<String> itemIDs) {
    this.itemIDs = itemIDs;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RecommendationGetCartRecommendationRequest recommendationGetCartRecommendationRequest = (RecommendationGetCartRecommendationRequest) o;
    return Objects.equals(this.itemIDs, recommendationGetCartRecommendationRequest.itemIDs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemIDs);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RecommendationGetCartRecommendationRequest {\n");
    
    sb.append("    itemIDs: ").append(toIndentedString(itemIDs)).append("\n");
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
