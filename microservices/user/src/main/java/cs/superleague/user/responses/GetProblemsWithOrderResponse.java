package cs.superleague.user.responses;

import cs.superleague.payment.dataclass.CartItem;

public class GetProblemsWithOrderResponse {
    private CartItem currentProductWithProblem;
    private CartItem alternativeProduct;
    private boolean problem;
    private String message;

    public GetProblemsWithOrderResponse(CartItem currentProductWithProblem, CartItem alternativeProduct, boolean problem, String message) {
        this.currentProductWithProblem = currentProductWithProblem;
        this.alternativeProduct = alternativeProduct;
        this.problem = problem;
        this.message = message;
    }

    public CartItem getCurrentProductWithProblem() {
        return currentProductWithProblem;
    }

    public void setCurrentProductWithProblem(CartItem currentProductWithProblem) {
        this.currentProductWithProblem = currentProductWithProblem;
    }

    public CartItem getAlternativeProduct() {
        return alternativeProduct;
    }

    public void setAlternativeProduct(CartItem alternativeProduct) {
        this.alternativeProduct = alternativeProduct;
    }

    public boolean isProblem() {
        return problem;
    }

    public void setProblem(boolean problem) {
        this.problem = problem;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
