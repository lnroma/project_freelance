package com.naumoff.rnc.dto.users;

public class DashboardStats {
    private Integer totalOrders;
    private Integer completedOrders;
    private Integer inProgressOrders;
    private Integer pendingOrders;


    public Integer getCompletedOrders() {
        return completedOrders;
    }

    public void setCompletedOrders(Integer completedOrders) {
        this.completedOrders = completedOrders;
    }

    public Integer getInProgressOrders() {
        return inProgressOrders;
    }

    public void setInProgressOrders(Integer inProgressOrders) {
        this.inProgressOrders = inProgressOrders;
    }

    public Integer getPendingOrders() {
        return pendingOrders;
    }

    public void setPendingOrders(Integer pendingOrders) {
        this.pendingOrders = pendingOrders;
    }

    public Integer getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(Integer totalOrders) {
        this.totalOrders = totalOrders;
    }
}
