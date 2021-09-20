package cs.superleague.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import cs.superleague.models.CartItemObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * This object is returned as output
 */
@ApiModel(description = "This object is returned as output")
@Validated
public class RecommendationGetCartRecommendationResponse   {
  @JsonProperty("recommendations")
  @Valid
  private List<CartItemObject> recommendations = null;

  @JsonProperty("isSuccess")
  private Boolean isSuccess = null;

  @JsonProperty("message")
  private String message = null;

  public RecommendationGetCartRecommendationResponse recommendations(List<CartItemObject> recommendations) {
    this.recommendations = recommendations;
    return this;
  }

  public RecommendationGetCartRecommendationResponse addRecommendationsItem(CartItemObject recommendationsItem) {
    if (this.recommendations == null) {
      this.recommendations = new ArrayList<CartItemObject>();
    }
    this.recommendations.add(recommendationsItem);
    return this;
  }

  /**
   * Get recommendations
   * @return recommendations
  **/
  @ApiModelProperty(value = "")
      @Valid
    public List<CartItemObject> getRecommendations() {
    return recommendations;
  }

  public void setRecommendations(List<CartItemObject> recommendations) {
    this.recommendations = recommendations;
  }

  public RecommendationGetCartRecommendationResponse isSuccess(Boolean isSuccess) {
    this.isSuccess = isSuccess;
    return this;
  }

  /**
   * Get isSuccess
   * @return isSuccess
  **/
  @ApiModelProperty(value = "")
  
    public Boolean isIsSuccess() {
    return isSuccess;
  }

  public void setIsSuccess(Boolean isSuccess) {
    this.isSuccess = isSuccess;
  }

  public RecommendationGetCartRecommendationResponse message(String message) {
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
    RecommendationGetCartRecommendationResponse recommendationGetCartRecommendationResponse = (RecommendationGetCartRecommendationResponse) o;
    return Objects.equals(this.recommendations, recommendationGetCartRecommendationResponse.recommendations) &&
        Objects.equals(this.isSuccess, recommendationGetCartRecommendationResponse.isSuccess) &&
        Objects.equals(this.message, recommendationGetCartRecommendationResponse.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(recommendations, isSuccess, message);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RecommendationGetCartRecommendationResponse {\n");
    
    sb.append("    recommendations: ").append(toIndentedString(recommendations)).append("\n");
    sb.append("    isSuccess: ").append(toIndentedString(isSuccess)).append("\n");
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
