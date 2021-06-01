public static class Item {
    /* Attributes */
    private String name;
    private double cost;
    private int quantity;
    private String description;
    private String imageUrl;
    private boolean texExempt;
    /* Constructor */

    public Item(String name, double cost, int quantity, String description, String imageUrl, boolean texExempt) {
        this.name = name;
        this.cost = cost;
        this.quantity = quantity;
        this.description = description;
        this.imageUrl = imageUrl;
        this.texExempt = texExempt;
    }
    /* Getters and Setters */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isTexExempt() {
        return texExempt;
    }

    public void setTexExempt(boolean texExempt) {
        this.texExempt = texExempt;
    }
}