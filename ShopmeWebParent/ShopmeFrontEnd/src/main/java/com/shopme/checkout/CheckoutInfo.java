package com.shopme.checkout;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

@Builder
@AllArgsConstructor
public class CheckoutInfo {
	private float productCost;
	private float productTotal;
	private float shippingCostTotal;
	private float paymentTotal;
	private int deliverDays;
	private boolean codSupported;

	public float getProductCost() {
		return productCost;
	}

	public void setProductCost(float productCost) {
		this.productCost = productCost;
	}

	public float getProductTotal() {
		return productTotal;
	}

	public void setProductTotal(float productTotal) {
		this.productTotal = productTotal;
	}

	public float getShippingCostTotal() {
		return shippingCostTotal;
	}

	public void setShippingCostTotal(float shippingCostTotal) {
		this.shippingCostTotal = shippingCostTotal;
	}

	public float getPaymentTotal() {
		return paymentTotal;
	}

	public void setPaymentTotal(float paymentTotal) {
		this.paymentTotal = paymentTotal;
	}

	public int getDeliverDays() {
		return deliverDays;
	}

	public void setDeliverDays(int deliverDays) {
		this.deliverDays = deliverDays;
	}

	public LocalDateTime getDeliverDate() {
		LocalDateTime now = LocalDateTime.now();

		return now.plusDays(deliverDays);
	}

	public boolean isCodSupported() {
		return codSupported;
	}

	public void setCodSupported(boolean codSupported) {
		this.codSupported = codSupported;
	}
	
	public String getPaymentTotal4PayPal() {
		DecimalFormat formatter = new DecimalFormat("###,###.##");
		return formatter.format(paymentTotal);
	}

}
