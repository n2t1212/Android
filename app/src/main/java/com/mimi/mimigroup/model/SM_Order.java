package com.mimi.mimigroup.model;

public class SM_Order {
    String OrderID;
    String OrderCode;
    String CustomerID;
    String CustomerCode;
    String CustomerName;
    String CustomerAddress;
    String OrderDate;
    String RequestDate;
    Integer MaxDebt;
    Double OriginMoney;
    Double VAT;
    Double VATMoney;
    Double TotalMoney;
    Integer OrderStatus;
    String OrderStatusDesc;
    String ApproveDate;
    String HandleStaff;
    String DeliveryDesc;
    Double Longitude;
    Double Latitude;
    String LocationAddress;
    Boolean IsSample;
    Boolean IsPost;
    String PostDay;
    String OrderNotes;
    Integer SeqnoCode;

    public SM_Order() {
    }

    public SM_Order(String orderId, String orderCode, String customerId, String customerCode,String customerName,String orderDate,
                    String requestDate,Integer maxDebt,Double originMoney,Double vat,Double vatmoney,Double totalMoney, Integer orderStatus,
                    String approveDate,String handleStaff,String deliveryDesc,Double longitude,Double latitude,String locationAddress,
                    boolean isPost,String postDay,Boolean IsSample) {
        this.OrderID=orderId;
        this.OrderCode=orderCode;
        this.CustomerID=customerId;
        this.CustomerCode=customerCode;
        this.CustomerName=customerName;
        this.OrderDate=orderDate;
        this.RequestDate=requestDate;
        this.MaxDebt=maxDebt;
        this.OriginMoney=originMoney;
        this.VAT=vat;
        this.VATMoney=vatmoney;
        this.TotalMoney=totalMoney;
        this.OrderStatus=orderStatus;
        this.ApproveDate=approveDate;
        this.HandleStaff=handleStaff;
        this.DeliveryDesc=deliveryDesc;
        this.Longitude=longitude;
        this.Latitude=latitude;
        this.LocationAddress=locationAddress;
        this.IsPost=isPost;
        this.PostDay=postDay;
        this.IsSample = IsSample;
    }


    public String getOrderID() {
        return OrderID;
    }
    public void setOrderID(String orderId) {
        OrderID = orderId;
    }

    public String getOrderCode() {
        return OrderCode;
    }
    public void setOrderCode(String orderCode) {
        OrderCode = orderCode;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String customerId) {
        CustomerID = customerId;
    }

    public String getCustomerCode() {
        return CustomerCode;
    }
    public void setCustomerCode(String customerCode) {
        CustomerCode = customerCode;
    }

    public String getCustomerName() {
        return CustomerName;
    }
    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getCustomerAddress() {
        return CustomerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        CustomerAddress = customerAddress;
    }

    public String getOrderDate() {
        return OrderDate;
    }
    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public String getRequestDate() {
        return RequestDate;
    }
    public void setRequestDate(String requestDate) {
        RequestDate = requestDate;
    }


    public Integer getMaxDebt() {
        return MaxDebt;
    }
    public void setMaxDebt(Integer maxDebt) {
        MaxDebt = maxDebt;
    }

    public Double getOriginMoney() {
        return OriginMoney;
    }
    public void setOriginMoney(Double originMoney) {
        OriginMoney = originMoney;
    }

    public Double getVAT() {
        return VAT;
    }
    public void setVAT(Double VAT) {
        this.VAT = VAT;
    }

    public Double getVATMoney() {
        return VATMoney;
    }
    public void setVATMoney(Double VATMoney) {
        this.VATMoney = VATMoney;
    }

    public Double getTotalMoney() {
        return TotalMoney;
    }
    public void setTotalMoney(Double totalMoney) {
        TotalMoney = totalMoney;
    }

    public Integer getOrderStatus() {
        return OrderStatus;
    }
    public void setOrderStatus(Integer orderStatus) {
        OrderStatus = orderStatus;
    }

    public String getApproveDate() {
        return ApproveDate;
    }
    public void setApproveDate(String approveDate) {
        ApproveDate = approveDate;
    }

    public String getHandleStaff() {
        return HandleStaff;
    }
    public void setHandleStaff(String handleStaff) {
        HandleStaff = handleStaff;
    }

    public String getDeliveryDesc() {
        return DeliveryDesc;
    }
    public void setDeliveryDesc(String deliveryDesc) {
        DeliveryDesc = deliveryDesc;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public Double getLatitude() {
        return Latitude;
    }
    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public String getLocationAddress() {
        return LocationAddress;
    }
    public void setLocationAddress(String locationAddress) {
        LocationAddress = locationAddress;
    }

    public Boolean getPost() {
        return IsPost;
    }
    public void setPost(Boolean post) {
        IsPost = post;
    }

    public String getPostDay() {
        return PostDay;
    }
    public void setPostDay(String postDay) {
        PostDay = postDay;
    }

    public String getOrderNotes() {
        return OrderNotes;
    }

    public void setOrderNotes(String orderNotes) {
        OrderNotes = orderNotes;
    }

    public Integer getSeqnoCode() {
        return SeqnoCode;
    }

    public void setSeqnoCode(Integer seqnoCode) {
        SeqnoCode = seqnoCode;
    }

    public String getOrderStatusDesc() {
        return OrderStatusDesc;
    }

    public void setOrderStatusDesc(String orderStatusDesc) {
        OrderStatusDesc = orderStatusDesc;
    }

    public Boolean getSample() {
        return IsSample;
    }

    public void setSample(Boolean sample) {
        IsSample = sample;
    }
}
